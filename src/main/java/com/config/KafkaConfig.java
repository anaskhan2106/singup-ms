package com.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KafkaConfig {
    private String bootstrapServers;
    private String indexApplicationTopicName;
    private Integer retryCount;
}