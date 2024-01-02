package com.processor;

import com.domain.OnboardingVerificationStatus;
import com.dto.OnboardingVerificationStatusDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrackingDeviceProcessor implements OnboardingProcessor{

    @Override
    public void handleProcessorRequest(OnboardingVerificationStatus onboardingVerificationStatus) throws JsonProcessingException {

    }
}
