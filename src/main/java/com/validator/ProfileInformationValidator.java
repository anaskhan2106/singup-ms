package com.validator;

import com.dto.DriverProfileDto;
import com.util.UtilMethods;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
@Slf4j
public class ProfileInformationValidator {

    public void validateInfo(DriverProfileDto driverProfileDto){
        validateName(driverProfileDto.getFirstName(),"FirstName");
        validateName(driverProfileDto.getLastName(),"LastName");
        validateEmail(driverProfileDto.getEmail());
        validatePhone(driverProfileDto.getPhone());
    }

    private void validateName(String value, String fieldName) {
        UtilMethods.validateRequired(value, fieldName);
        if (!Pattern.matches("^[a-zA-Z]+$", value)) {
            throw new IllegalArgumentException(fieldName + " should only contain alphabetic characters.");
        }
    }

    private void validateEmail(String email) {
        UtilMethods.validateRequired(email, "Email");
        if (!Pattern.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,6}$", email)) {
            throw new IllegalArgumentException("Invalid email address.");
        }
    }

    private void validatePhone(String phone) {
        UtilMethods.validateRequired(phone, "Phone");
        if (!Pattern.matches("^\\+?[0-9.-]+$", phone)) {
            throw new IllegalArgumentException("Invalid phone number.");
        }
    }
}
