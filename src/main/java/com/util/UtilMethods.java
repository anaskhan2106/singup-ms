package com.util;

import com.azure.core.util.Context;
import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.AccessTier;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.azure.storage.blob.models.BlobRequestConditions;
import com.azure.storage.blob.models.ParallelTransferOptions;
import com.azure.storage.blob.options.BlobParallelUploadOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

@Component
@Slf4j
public class UtilMethods {

    @Value( "${blobConnectionString}")
    static String blobConnectionString;

    public static boolean isValidUUID(String uuid) {
        try {
            UUID.fromString(uuid);
            return true;
        } catch (Exception var2) {
            log.error("Invalid UUID provided: {}", uuid);
            return false;
        }
    }

    public static void validateRequired(String value, String field) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException(field+" cannot be null or empty.");
        }
    }


}
