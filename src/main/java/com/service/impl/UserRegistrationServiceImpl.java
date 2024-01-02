package com.service.impl;

import com.azure.core.util.Context;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.AccessTier;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.azure.storage.blob.models.BlobRequestConditions;
import com.azure.storage.blob.models.ParallelTransferOptions;
import com.azure.storage.blob.options.BlobParallelUploadOptions;
import com.constant.ApplicationConfigurationConstants;
import com.domain.DriverProfile;
import com.domain.OnboardingVerificationStatus;
import com.dto.DocumentStoreDto;
import com.dto.DriverProfileDto;
import com.dto.LoginDto;
import com.factory.ProcessorFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.mapper.MapperClass;
import com.repository.DocumentStoreRepo;
import com.repository.DriverProfileRepo;
import com.repository.OnBoardingVerificationStatusRepo;
import com.service.KafkaServiceImpl;
import com.service.UserRegistrationService;
import com.dto.LoginMesage;
import com.validator.ProfileInformationValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.util.*;

@Slf4j
@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {

    @Autowired
    private DriverProfileRepo driverProfileRepo;

    @Autowired
    private DocumentStoreRepo documentStoreRepo;

    @Autowired
    private OnBoardingVerificationStatusRepo onBoardingVerificationStatusRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ProfileInformationValidator profileInformationValidator;

    @Autowired
    private KafkaServiceImpl kafkaService;

    @Autowired
    private ProcessorFactory processorFactory;

    @Autowired
    private MapperClass mapperClass;

    @Value( "${topicName}" )
    String topicName;

    @Value( "${blobConnectionString}")
    String blobConnectionString;

    private final Gson gson = new Gson();

    @Override
    public void addDriver(DriverProfileDto driverProfileDto) {
        //basic validation for all the driver details fields
        profileInformationValidator.validateInfo(driverProfileDto);
        Optional<DriverProfile> driver = driverProfileRepo.findOneByEmail(driverProfileDto.getEmail());
        if(!driver.isPresent()) {
            DriverProfile driverProfile = new DriverProfile();
            driverProfile.setUserId(UUID.randomUUID().toString());
            driverProfile.setAddress(driverProfileDto.getAddress());
            driverProfile.setFirstName(driverProfileDto.getFirstName());
            driverProfile.setEmail(driverProfileDto.getEmail());
            driverProfile.setPassword(this.passwordEncoder.encode(driverProfileDto.getPassword()));
            driverProfileRepo.saveAndFlush(driverProfile);
            //Push a message to kafka which initiates different verification processes
            triggerVerificationProcess(driverProfile.getUserId());
        }
        else{
            log.error("Driver already exists in the system, email id {}", driverProfileDto.getEmail());
            throw new DuplicateKeyException("Driver already exists in the system. Try a different email and sing up");
        }
    }

    @Override
    public LoginMesage loginDriver(LoginDto loginDto) {
        DriverProfile driverProfile = driverProfileRepo.findByEmail(loginDto.getEmail());
        if (driverProfile != null) {
            String password = loginDto.getPassword();
            String encodedPassword = driverProfile.getPassword();
            Boolean isPwdRight = passwordEncoder.matches(password, encodedPassword);
            if (isPwdRight) {
                    log.info("User is log in now.", loginDto.getEmail());
                    return new LoginMesage("Login Success", true);
            } else {
                log.info("Password is incorrect", loginDto.getEmail());
                return new LoginMesage("Password Does Not Match", false);
            }
        }else {
            log.info("Email does not exists in the system.", loginDto.getEmail());
            return new LoginMesage("Email Does Not Exits", false);
        }
    }

    @Override
    public String upload(MultipartFile multipartFile, String userId, Map<String, Object> metadata) {
        Optional<DriverProfile> driver = driverProfileRepo.findOneByUserIdAndIsActiveTrue(userId);
        String fileId = UUID.randomUUID().toString();
        if (driver.isPresent()) {
            log.info("Driver details found in DB:{}", userId);
            uploadFileToBlob(multipartFile, ApplicationConfigurationConstants.CONTAINER_NAME, multipartFile.getName());
            DocumentStoreDto documentStoreDto = new DocumentStoreDto();
            documentStoreDto.setId(fileId);
            documentStoreDto.setDocumentName(multipartFile.getName());
            //process and set the provided metadata related to the upload operation.
            populateMetadata(documentStoreDto, metadata);
            documentStoreRepo.save(mapperClass.documentStoreDtoToDocumentStore(documentStoreDto));
        } else {
            log.error("Driver details not found in DB:{}", userId);
            throw new IllegalStateException("Driver details not found in DB!");
        }
        return fileId;
    }

    private void populateMetadata(DocumentStoreDto documentStoreDto, Map<String, Object> metadata) {
        documentStoreDto.setDocumentType((String) metadata.getOrDefault("docType",""));
        documentStoreDto.setComments((String) metadata.getOrDefault("comment",""));
        documentStoreDto.setUploadedBy((String) metadata.getOrDefault("uploadedBy",""));
        documentStoreDto.setUploadedTs(new Date());
    }

    @Override
    public void markVerified(String type, String userId) {
        Optional<DriverProfile> driver = driverProfileRepo.findOneByUserIdAndIsActiveTrue(userId);
        if (driver.isPresent()) {
            log.info("Driver details found in DB:{}", userId);
            Optional<OnboardingVerificationStatus> onBoardingVerificationStatus = onBoardingVerificationStatusRepo.findOneByUserIdAndType(userId, type);
            if (onBoardingVerificationStatus.isPresent()) {
                log.info("On boardingVerificationStatus details found in DB:{}", userId);
                try {
                    processorFactory.getProcessor(type).handleProcessorRequest(onBoardingVerificationStatus.get());
                } catch (JsonProcessingException e) {
                    log.error("Error occurred in processing");
                    throw new RuntimeException("Error occurred in processing");
                }
            }
        }
        else{
            log.error("Driver details not found in DB:{}", userId);
            throw new IllegalStateException("Driver details not found in DB!");
        }
    }

    @Override
    public void markReadyToTakeRide(boolean isReady, String userId) {
        Optional<DriverProfile> driver = driverProfileRepo.findOneByUserIdAndIsActiveTrue(userId);
        if (driver.isPresent()) {
            log.info("Driver details found in DB:{}", userId);
            driver.get().setIsReadyForRide(isReady);
            driverProfileRepo.save(driver.get());
            log.info("Driver is " + (isReady ? "" : "not ") + " ready to take the ride.");
        } else {
            log.error("Driver details not found in DB:{}", userId);
            throw new IllegalStateException("Driver details not found in DB!");
        }
    }

    private void triggerVerificationProcess(String userId) {
        Map< String, Object > event = new HashMap<>();
        event.put( ApplicationConfigurationConstants.USER_ID, userId);
        event.put( ApplicationConfigurationConstants.KEY_EVENT_NAME, "StartOnBoarding" );
        event.put( ApplicationConfigurationConstants.KEY_EVENT_TIMESTAMP, System.currentTimeMillis() );
        log.info( "Event Object Prepared Successfully." );
        kafkaService.sendMessage(  gson.toJson( event ), topicName );
    }

    private void uploadFileToBlob(MultipartFile multipartFile, String containerName, String fileName) {
        try {
            BlobServiceClient client =
                    new BlobServiceClientBuilder().connectionString( blobConnectionString ).buildClient();
            BlobClient blobClient = client.getBlobContainerClient( containerName).getBlobClient( fileName );
            long blockSize = 2 * 1024 * 1024; // 2MB
            ParallelTransferOptions parallelTransferOptions =
                    new ParallelTransferOptions().setBlockSizeLong( blockSize );
            BlobHttpHeaders headers = new BlobHttpHeaders().setContentLanguage( "en-US" ).setContentType( "binary" );
            blobClient.uploadWithResponse(
                    new BlobParallelUploadOptions( multipartFile.getInputStream() ).setParallelTransferOptions(
                            parallelTransferOptions )
                            .setHeaders( headers )
                            .setTier( AccessTier.HOT )
                            .setRequestConditions(
                                    new BlobRequestConditions() ),
                    Duration.ofMinutes( 30 ), Context.NONE );
        } catch (IOException e) {
            log.error("Error occurred while file upload!",e.getMessage());
            throw new RuntimeException("Error occurred while uploading the file.");
        }
    }
}
