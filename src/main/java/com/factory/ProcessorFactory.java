package com.factory;

import com.constant.ApplicationConfigurationConstants;
import com.processor.BackgroundVerificationProcessor;
import com.processor.DocumentsCollectionProcessor;
import com.processor.OnboardingProcessor;
import com.processor.TrackingDeviceProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;


@Slf4j
@Component
public class ProcessorFactory {
    
    @Autowired
    DocumentsCollectionProcessor documentsCollectionProcessor;
    @Autowired
    BackgroundVerificationProcessor backgroundVerificationProcessor;
    @Autowired
    TrackingDeviceProcessor trackingDeviceProcessor;
    
    public OnboardingProcessor getProcessor(String type) {
        log.debug( "Processor type: {}", type );
        OnboardingProcessor onboardingProcessor = null;
        if(!ObjectUtils.isEmpty(type) ) {
            switch( type.toUpperCase() ) {
                case ApplicationConfigurationConstants.TRACKING_DEVICE:
                    onboardingProcessor = trackingDeviceProcessor;
                    break;
                case ApplicationConfigurationConstants.BACKGROUND:
                    onboardingProcessor = backgroundVerificationProcessor;
                    break;
                default:
                    onboardingProcessor = documentsCollectionProcessor;
                    break;
            }
        }
        
        return onboardingProcessor;
    }
}
