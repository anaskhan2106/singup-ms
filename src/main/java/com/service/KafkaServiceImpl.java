package com.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.Map;

@Slf4j
@Service
public class KafkaServiceImpl{

    @Autowired
    KafkaTemplate< String, String > kafkaTemplate;

    public void sendMessage(String message, String topic) {
        ListenableFuture<SendResult< String, String >> future = kafkaTemplate.send( topic, message );
        future.addCallback( new ListenableFutureCallback< SendResult< String, String > >() {
            @Override
            public void onSuccess(SendResult< String, String > result) {
                log.info( "Message [{}] delivered with offset {}", message, result.getRecordMetadata().offset() );
            }
            @Override
            public void onFailure(Throwable ex) {
                log.warn( "Unable to deliver message [{}]. {}", message, ex.getMessage() );
            }
        } );
    }
}
