package com.processor;

import com.domain.OnboardingVerificationStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.repository.OnBoardingVerificationStatusRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DocumentsCollectionProcessor implements OnboardingProcessor{

    @Autowired
    private OnBoardingVerificationStatusRepo onBoardingVerificationStatusRepo;

    @Override
    public void handleProcessorRequest(OnboardingVerificationStatus onboardingVerificationStatus) throws JsonProcessingException {
        onboardingVerificationStatus.setIsCompleted(true);
        onBoardingVerificationStatusRepo.saveAndFlush(onboardingVerificationStatus);
    }
}
