package com.processor;

import com.domain.OnboardingVerificationStatus;
import com.dto.OnboardingVerificationStatusDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OnboardingProcessor {
    void handleProcessorRequest(OnboardingVerificationStatus onboardingVerificationStatus) throws JsonProcessingException;
}

