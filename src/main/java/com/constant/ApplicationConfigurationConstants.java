package com.constant;

import java.time.Duration;

public class ApplicationConfigurationConstants {
    
    public static final String KEY_EVENT_NAME = "ename";
    public static final String KEY_EVENT_TIMESTAMP = "ets";
    public static final String KEY_EVENT_DATA = "edata";
    public static final String TRUSTSTORE_FILEPATH = "/tmp/kafkaBrokerTrustStore.jks";
    public static final String KEYSTORE_FILEPATH = "/tmp/kafkaBrokerKeyStore.jks";
    public static final String KV_KAFKA_BROKER_TRUST_STORE_CERT_FILE_NAME = "base64EncodedKafkaBrokerTrustStoreCertificate.txt";
    public static final String KV_KAFKA_BROKER_KEY_STORE_CERT_FILE_NAME = "base64EncodedKafkaBrokerKeyStoreCertificate.txt";
    public static final String KV_TRUST_STORE_CREDENTIAL_FILE_NAME = "kafkaBrokerTrustStoreCredentials.txt";
    public static final String KEY_KAFKA_SECURITY_PROTOCOL = "security.protocol";
    public static final String KEY_KAFKA_SSL_TRUST_STORE_LOCATION = "ssl.truststore.location";
    public static final String KEY_KAFKA_SSL_KEY_STORE_LOCATION = "ssl.keystore.location";
    public static final String KEY_KAFKA_SSL_TRUST_STORE_CREDENTIALS = "ssl.truststore.password";
    public static final String KEY_KAFKA_SSL_KEY_STORE_CREDENTIALS = "ssl.keystore.password";
    public static final String KEY_KAFKA_SSL_KEY_CREDENTIALS = "ssl.key.password";
    public static final String KEY_KAFKA_ENDPOINT_IDENTIFICATION_ALGORITHM = "";
    public static final String DOCUMENT = "DOCUMENT";
    public static final String BACKGROUND = "BACKGROUND";
    public static final String TRACKING_DEVICE = "TRACKING_DEVICE";
    public static final String CONTAINER_NAME = "test-container";
    public static final String FILE_NAME = "fileName";
    public static final String USER_ID = "userID";

    private ApplicationConfigurationConstants() { //
    }
}
