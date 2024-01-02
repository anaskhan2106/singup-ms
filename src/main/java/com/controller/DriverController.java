package com.controller;

import com.dto.DriverProfileDto;
import com.dto.LoginDto;
import com.service.UserRegistrationService;
import com.dto.LoginMesage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("api/driver")
public class DriverController {

    @Autowired
    private UserRegistrationService userRegistrationService;

    /**
     * This API is responsible for driver on boarding which takes the driver's information as input and create the profile in system.
     *
     * @param driverProfileDto - This DTO contains all the basic information about the driver for singing up.
     * @return
     */
    @PostMapping(path = "/signup")
    public ResponseEntity signupDriver(@RequestBody DriverProfileDto driverProfileDto)
    {
        userRegistrationService.addDriver(driverProfileDto);
        return ResponseEntity.noContent().build();
    }

    /**
     * This API will be used by driver to login into system using email as it's username and the password.
     *
     * @param loginDTO - This DTO contains the credentials of the user.
     * @return
     */
    @PostMapping(path = "/login")
    public ResponseEntity<?> loginDriver(@RequestBody LoginDto loginDTO)
    {
        LoginMesage loginResponse = userRegistrationService.loginDriver(loginDTO);
        return ResponseEntity.ok(loginResponse);
    }

    /**
     * This API will be used by driver to upload any documents required for on boarding.
     *
     * @param multipartFile - This is the file needs to be uploaded.
     * @param userId - This is user id for which file needs to be uploaded.
     * @return
     */
    @PostMapping(path = "/upload")
    public ResponseEntity<String> uploadDocument(@RequestParam( name = "file" ) MultipartFile multipartFile,
                                                 @RequestParam( name = "userId" ) String userId,
                                                 @RequestBody Map< String, Object > metadata)
    {
        String fileId = userRegistrationService.upload(multipartFile, userId, metadata);
        return ResponseEntity.ok(fileId);
    }

    /**
     * This API will be used by driver to upload any documents required for on boarding.
     *
     * @param type - This is the type of on boarding process which needs to be marked verified.
     * @param userId - This is user id for the process needs to be verified.
     * @return
     */
    @PatchMapping(path = "/verify")
    public ResponseEntity<?> markVerified(@RequestParam( name = "type" ) String type,@RequestParam( name = "userId" ) String userId)
    {
        userRegistrationService.markVerified(type, userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * This API will be used by driver to upload any documents required for on boarding.
     *
     * @param isReady - This is the type of on boarding process which needs to be marked verified.
     * @param userId - This is user id for the process needs to be verified.
     * @return
     */
    @PostMapping(path = "/readyToRide")
    public ResponseEntity<?> toggleDriverRideAvailability(@RequestParam( name = "isReady" ) boolean isReady,@RequestParam( name = "userId" ) String userId)
    {
        userRegistrationService.markReadyToTakeRide(isReady, userId);
        return ResponseEntity.noContent().build();
    }
}
