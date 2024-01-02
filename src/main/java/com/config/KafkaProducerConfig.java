package com.config;

import com.constant.ApplicationConfigurationConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class KafkaProducerConfig {
    
    @Value( "${bootstrapServers}" )
    String bootstrapServers;

    @Value( "${retryCount}" )
    String retryCount;
    
    @Bean
    public Map< String, Object > producerConfigs() {
        Map< String, Object > props = new HashMap<>();
        props.put( ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers );
        props.put( ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class );
        props.put( ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class );
        props.put( ProducerConfig.RETRIES_CONFIG, retryCount );
        props.put( JsonSerializer.ADD_TYPE_INFO_HEADERS, false );
        props.put( ApplicationConfigurationConstants.KEY_KAFKA_SECURITY_PROTOCOL, "SSL" );
        props.put( ApplicationConfigurationConstants.KEY_KAFKA_SSL_TRUST_STORE_LOCATION, getTrustStoreFileLocation() );
        props.put( ApplicationConfigurationConstants.KEY_KAFKA_SSL_KEY_STORE_LOCATION, getKeyStoreFileLocation() );
        
        String trustStoreFileCredentials = getTrustStoreFilePassword();
        props.put( ApplicationConfigurationConstants.KEY_KAFKA_SSL_TRUST_STORE_CREDENTIALS, trustStoreFileCredentials );
        props.put( ApplicationConfigurationConstants.KEY_KAFKA_SSL_KEY_STORE_CREDENTIALS, trustStoreFileCredentials );
        props.put( ApplicationConfigurationConstants.KEY_KAFKA_SSL_KEY_CREDENTIALS, trustStoreFileCredentials );
        props.put( ApplicationConfigurationConstants.KEY_KAFKA_ENDPOINT_IDENTIFICATION_ALGORITHM, "" );
        return props;
    }
    
    @Bean
    public ProducerFactory< String, String > producerFactory() {
        return new DefaultKafkaProducerFactory<>( producerConfigs() );
    }
    
    @Bean
    public KafkaTemplate< String, String > kafkaTemplate() {
        return new KafkaTemplate<>( producerFactory() );
    }
    
    private String getTrustStoreFileLocation() {
        File file = new File( ApplicationConfigurationConstants.TRUSTSTORE_FILEPATH );
        log.info( "Truststore File Location: {}", ApplicationConfigurationConstants.TRUSTSTORE_FILEPATH );
        try( FileOutputStream fos = new FileOutputStream( file ) ) {
            String truststore = new String( Files.readAllBytes( Paths.get( "/etc/secrets/".concat(
                    ApplicationConfigurationConstants.KV_KAFKA_BROKER_TRUST_STORE_CERT_FILE_NAME ) ) ) );
            fos.write( Base64.getDecoder().decode( truststore ) );
        } catch( Exception e ) {
            log.error( "error writing file: {} | {} | {}", e.getClass().getCanonicalName(), e.getMessage(),
                       e.getCause() );
            return "";
        }
        return ApplicationConfigurationConstants.TRUSTSTORE_FILEPATH;
    }
    
    private String getKeyStoreFileLocation() {
        File file = new File( ApplicationConfigurationConstants.KEYSTORE_FILEPATH );
        log.info( "Truststore File Location: {}", ApplicationConfigurationConstants.KEYSTORE_FILEPATH );
        try( FileOutputStream fos = new FileOutputStream( file ) ) {
            String keystore = new String( Files.readAllBytes( Paths.get( "/etc/secrets/".concat(
                    ApplicationConfigurationConstants.KV_KAFKA_BROKER_KEY_STORE_CERT_FILE_NAME ) ) ) );
            fos.write( Base64.getDecoder().decode( keystore ) );
        } catch( Exception e ) {
            log.error( "error writing file: {} | {} | {}", e.getClass().getCanonicalName(), e.getMessage(),
                       e.getCause() );
            return "";
        }
        return ApplicationConfigurationConstants.KEYSTORE_FILEPATH;
    }
    
    private String getTrustStoreFilePassword() {
        String password = null;
        try {
            password = new String( Files.readAllBytes( Paths.get( "/etc/secrets/".concat(
                    ApplicationConfigurationConstants.KV_TRUST_STORE_CREDENTIAL_FILE_NAME ) ) ) );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return password;
    }
}
