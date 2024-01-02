package com.walmart.lng.service;

import com.service.UserRegistrationService;
import com.service.impl.UserRegistrationServiceImpl;
import com.validator.ProfileInformationValidator;
import org.springframework.context.annotation.Bean;

public class UserRegistrationImplConfiguration {
    @Bean
    public UserRegistrationService userRegistrationService() {
        return new UserRegistrationServiceImpl();
    }

    @Bean
    public ProfileInformationValidator profileInformationValidator(){return new ProfileInformationValidator();}

}
