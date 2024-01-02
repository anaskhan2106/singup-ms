package com.walmart.lng.service;

import com.azure.core.util.Context;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.options.BlobParallelUploadOptions;
import com.config.KafkaConfig;
import com.constant.ApplicationConfigurationConstants;
import com.domain.DriverProfile;
import com.domain.OnboardingVerificationStatus;
import com.dto.*;
import com.factory.ProcessorFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.mapper.MapperClass;
import com.processor.BackgroundVerificationProcessor;
import com.processor.DocumentsCollectionProcessor;
import com.processor.TrackingDeviceProcessor;
import com.repository.DocumentStoreRepo;
import com.repository.DriverProfileRepo;
import com.repository.OnBoardingVerificationStatusRepo;
import com.service.KafkaServiceImpl;
import com.service.UserRegistrationService;
import com.service.impl.UserRegistrationServiceImpl;
import com.util.UtilMethods;
import com.validator.ProfileInformationValidator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.concurrent.ListenableFuture;
import com.google.gson.JsonObject;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RunWith( SpringRunner.class )
@SpringBootTest
@ContextConfiguration(classes = UserRegistrationImplConfiguration.class, loader = AnnotationConfigContextLoader.class)
public class UserRegistrationServiceImplTest {

    @Autowired
    private UserRegistrationServiceImpl userRegistrationService;

    @Autowired
    ProfileInformationValidator profileInformationValidator;

    @MockBean
    private DriverProfileRepo driverProfileRepo;

    @MockBean
    private DocumentStoreRepo documentStoreRepo;

    @MockBean
    private OnBoardingVerificationStatusRepo onBoardingVerificationStatusRepo;

    @MockBean
    private KafkaServiceImpl kafkaService;

    @MockBean
    ProcessorFactory processorFactory;

    @MockBean
    KafkaTemplate< String, String > kafkaTemplate;

    @MockBean
    BackgroundVerificationProcessor backgroundVerificationProcessor;

    @MockBean
    DocumentsCollectionProcessor documentsCollectionProcessor;

    @MockBean
    TrackingDeviceProcessor trackingDeviceProcessor;

    @MockBean
    KafkaConfig kafkaConfig;

    @MockBean
    PasswordEncoder passwordEncoder;

    @MockBean
    MapperClass mapperClass;

    @MockBean
    UtilMethods utilMethods;

    private BlobServiceClientBuilder blobServiceClientBuilder;

    private BlobServiceClient blobServiceClient;

    private BlobContainerClient blobContainerClient;

    private BlobClient blobClient;

    private ObjectMapper objectMapper;
    private MockMultipartFile mockMultipartFile;
    private DriverProfileDto driverProfileDto;
    private DocumentStoreDto documentStoreDto;
    private LoginDto loginDto;
    private OnboardingVerificationStatusDto onboardingVerificationStatusDto;
    private DriverProfile driverProfile;
    private OnboardingVerificationStatus onboardingVerificationStatus;
    private MultipartFile multipartFile;
    private SendResult<String, Object> sendResult;
    private JSONObject json;
    private final JsonObject obj = new JsonObject();
    private final Gson gson = new Gson();

    Map< String, String > testAttributesForEmptyCacheFlag = new HashMap<>();
    Map< String, String > testAttributesForWrongCacheFlag = new HashMap<>();
    Map<String, Object> processMap=new HashMap<>();
    Map<String, Object> meteDataMap= new HashMap<>();

    String topic = "topic";
    long offset = 100;
    int partition = 2;

    @Before
    public void setUp() throws Exception {
        Map< String, String > testAttributes = new HashMap<>();
        json = new JSONObject();
        objectMapper = new ObjectMapper();
        sendResult = mock(SendResult.class);
        blobServiceClientBuilder = mock(BlobServiceClientBuilder.class);
        blobServiceClient = mock(BlobServiceClient.class);
        blobContainerClient = mock(BlobContainerClient.class);
        blobClient = mock(BlobClient.class);
        meteDataMap.put("IL_OBJECT_ID","123abc");
        testAttributes.put( "testKey", "testVal" );
        testAttributesForEmptyCacheFlag.put( "testKey", "testVal" );
        testAttributesForWrongCacheFlag.put("isCacheReloadRequired","TEST");
        testAttributes.put("isCacheReloadRequired","N");
        mockMultipartFile =  new MockMultipartFile("file", "test.txt",
                "text/plain", "Spring Framework".getBytes());
        multipartFile = new MockMultipartFile("file", "test.txt",
                "text/plain", "Spring Framework".getBytes());
        driverProfileDto = DriverProfileDto.builder().userId("username").password("password").email("abc@gmail.com")
                .firstName("Peter").lastName("Parker").phone("9591222190").address("HSR").country("India").build();
        loginDto = LoginDto.builder().email("abc@gmail.com").username("username").password("password").build();
        driverProfile = new DriverProfile();
        onboardingVerificationStatus = new OnboardingVerificationStatus();
        driverProfile.setAddress("HSR");
        driverProfile.setCountry("India");
        driverProfile.setEmail("abc@gmail.com");
        driverProfile.setFirstName("Peter");
        driverProfile.setLastName("Parker");
        driverProfile.setPassword("password");
        driverProfile.setPhone("9591222190");
        driverProfile.setUserId("username");
        onboardingVerificationStatus.setId(27181);
        onboardingVerificationStatus.setIsCompleted(false);
        onboardingVerificationStatus.setType(ApplicationConfigurationConstants.BACKGROUND);

    }

    @Test
    public void testAddDriverWithCorrectInput() {
        when(driverProfileRepo.findOneByEmail(any())).thenReturn(java.util.Optional.ofNullable(null));
        RecordMetadata recordMetadata = new RecordMetadata(new TopicPartition(topic, partition), offset, 0L, 0L, 0L, 0, 0);
        when(kafkaTemplate.send(any(),any())).thenReturn(null);
        given(sendResult.getRecordMetadata()).willReturn(recordMetadata);
        ListenableFuture<SendResult<String, String>> responseFuture = mock(ListenableFuture.class);
        when(kafkaTemplate.send(any(),any())).thenReturn(responseFuture);
        doAnswer(invocationOnMock -> {
            ListenableFutureCallback listenableFutureCallback = invocationOnMock.getArgument(0);
            listenableFutureCallback.onSuccess(sendResult);
            return null;
        }).when(responseFuture).addCallback(any(ListenableFutureCallback.class));
        userRegistrationService.addDriver(driverProfileDto);
        verify(driverProfileRepo, times(1)).findOneByEmail(driverProfileDto.getEmail());
        verify(driverProfileRepo, times(1)).saveAndFlush(any());

    }

    @Test(expected = DuplicateKeyException.class)
    public void testAddDriverWithDuplicateDriver() {
        when(driverProfileRepo.findOneByEmail(any())).thenReturn(java.util.Optional.ofNullable(driverProfile));
        userRegistrationService.addDriver(driverProfileDto);
        verify(driverProfileRepo, times(0)).saveAndFlush(any());
        verify(driverProfileRepo, times(1)).findOneByEmail(driverProfileDto.getEmail());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDriverWithWrongFirstName() {
        when(driverProfileRepo.findOneByEmail(any())).thenReturn(java.util.Optional.ofNullable(driverProfile));
        driverProfileDto.setFirstName("***");
        userRegistrationService.addDriver(driverProfileDto);
        verify(profileInformationValidator, times(1)).validateInfo(driverProfileDto);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDriverWithWrongLastName() {
        when(driverProfileRepo.findOneByEmail(any())).thenReturn(java.util.Optional.ofNullable(driverProfile));
        driverProfileDto.setLastName("***");
        userRegistrationService.addDriver(driverProfileDto);
        verify(profileInformationValidator, times(1)).validateInfo(driverProfileDto);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDriverWithWrongPhoneNo() {
        when(driverProfileRepo.findOneByEmail(any())).thenReturn(java.util.Optional.ofNullable(driverProfile));
        driverProfileDto.setPhone("phone");
        userRegistrationService.addDriver(driverProfileDto);
        verify(profileInformationValidator, times(1)).validateInfo(driverProfileDto);
    }

    @Test
    public void testLoginDriverWithCorrectInput() {
        when(driverProfileRepo.findByEmail(any())).thenReturn(driverProfile);
        when(passwordEncoder.matches(any(),any())).thenReturn(true);
        userRegistrationService.loginDriver(loginDto);
        verify(driverProfileRepo, times(1)).findByEmail(driverProfileDto.getEmail());
        verify(driverProfileRepo, times(1)).findOneByEmailAndPassword(any(), any());
    }

    @Test
    public void testLoginDriverWithWrongEmail() {
        when(driverProfileRepo.findByEmail(any())).thenReturn(null);
        when(passwordEncoder.matches(any(),any())).thenReturn(true);
        LoginMesage loginMesage = userRegistrationService.loginDriver(loginDto);
        verify(driverProfileRepo, times(1)).findByEmail(driverProfileDto.getEmail());
        assertEquals("Email Does Not Exits", loginMesage.getMessage());
        assertEquals(false, loginMesage.getStatus());
    }

    @Test
    public void testLoginDriverWithWrongPassword() {
        when(driverProfileRepo.findByEmail(any())).thenReturn(driverProfile);
        when(passwordEncoder.matches(any(),any())).thenReturn(false);
        LoginMesage loginMesage = userRegistrationService.loginDriver(loginDto);
        verify(driverProfileRepo, times(1)).findByEmail(driverProfileDto.getEmail());
        assertEquals("Password Does Not Match", loginMesage.getMessage());
        assertEquals(false, loginMesage.getStatus());
    }

    @Test
    public void testUploadWithCorrectInput() {
        when(driverProfileRepo.findOneByUserIdAndIsActiveTrue(any())).thenReturn(java.util.Optional.ofNullable(driverProfile));
        given(blobServiceClientBuilder.connectionString("gyy").buildClient()).willReturn(blobServiceClient);
        given(blobServiceClient.getBlobContainerClient(anyString())).willReturn(blobContainerClient);
        given(blobContainerClient.getBlobClient(anyString())).willReturn(blobClient);
        // Creating a dummy MultipartFile
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "Hello, World!".getBytes()
        );

        // Stubbing the behavior of the blobClient.uploadWithResponse method
        when(blobClient.uploadWithResponse(
                Mockito.any(BlobParallelUploadOptions.class),
                Mockito.any(Duration.class),
                Mockito.any(Context.class))
        ).thenReturn(null);
        String fileId = userRegistrationService.upload(multipartFile,"userId123",meteDataMap);
        verify(driverProfileRepo, times(1)).findOneByUserIdAndIsActiveTrue(driverProfileDto.getEmail());
        assertNotNull(fileId);
    }


    @Test
    public void testMarkVerifiedWithCorrectInput() {
        when(driverProfileRepo.findOneByUserIdAndIsActiveTrue(any())).thenReturn(java.util.Optional.ofNullable(driverProfile));
        when(onBoardingVerificationStatusRepo.findOneByUserIdAndType(any(),any())).thenReturn(java.util.Optional.ofNullable(onboardingVerificationStatus));
        when(processorFactory.getProcessor( ApplicationConfigurationConstants.DOCUMENT ) ).thenReturn( documentsCollectionProcessor );
        userRegistrationService.markVerified(ApplicationConfigurationConstants.DOCUMENT, "username");
        verify(driverProfileRepo, times(1)).findOneByUserIdAndIsActiveTrue("username");
        verify(onBoardingVerificationStatusRepo, times(1)).findOneByUserIdAndType("username", ApplicationConfigurationConstants.DOCUMENT);
    }

    @Test(expected = IllegalStateException.class)
    public void testMarkVerifiedWithNoDriver() {
        when(driverProfileRepo.findOneByUserIdAndIsActiveTrue(any())).thenReturn(java.util.Optional.ofNullable(null));
        when(onBoardingVerificationStatusRepo.findOneByUserIdAndType(any(),any())).thenReturn(java.util.Optional.ofNullable(onboardingVerificationStatus));
        when(processorFactory.getProcessor( ApplicationConfigurationConstants.DOCUMENT ) ).thenReturn( documentsCollectionProcessor );
        userRegistrationService.markVerified(ApplicationConfigurationConstants.DOCUMENT, "username");
        verify(driverProfileRepo, times(1)).findOneByUserIdAndIsActiveTrue("username");
        verify(onBoardingVerificationStatusRepo, times(0)).findOneByUserIdAndType("username", ApplicationConfigurationConstants.DOCUMENT);
    }

    @Test
    public void testMarkReadyToTakeRide() {
        when(driverProfileRepo.findOneByUserIdAndIsActiveTrue(any())).thenReturn(java.util.Optional.ofNullable(driverProfile));
        userRegistrationService.markReadyToTakeRide(true, "username");
        verify(driverProfileRepo, times(1)).findOneByUserIdAndIsActiveTrue("username");
        verify(driverProfileRepo, times(1)).save(driverProfile);
    }

    @Test(expected = IllegalStateException.class)
    public void testMarkReadyToTakeRideDriverNotPresent() {
        when(driverProfileRepo.findOneByUserIdAndIsActiveTrue(any())).thenReturn(java.util.Optional.ofNullable(null));
        userRegistrationService.markReadyToTakeRide(true, "username");
        verify(driverProfileRepo, times(1)).findOneByUserIdAndIsActiveTrue("username");
        verify(driverProfileRepo, times(0)).save(driverProfile);
    }
}
