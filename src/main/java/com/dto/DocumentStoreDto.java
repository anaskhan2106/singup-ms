package com.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentStoreDto {

    private String id;

    private String user_id;

    private String documentName;

    private String documentType;

    private String uploadedBy;

    private Date uploadedTs;

    private String comments;
}