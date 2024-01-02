package com.service;

import com.dto.DriverProfileDto;
import com.dto.LoginDto;
import com.dto.LoginMesage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public interface UserRegistrationService {
    void addDriver(DriverProfileDto driverProfileDto);
    LoginMesage loginDriver(LoginDto loginDto);
    String upload(MultipartFile multipartFile, String userId, Map<String, Object> metadata);
    void markVerified(String type, String userId);
    void markReadyToTakeRide(boolean isReady, String userId);
}
