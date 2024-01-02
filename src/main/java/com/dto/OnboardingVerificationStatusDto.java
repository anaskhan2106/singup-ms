package com.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnboardingVerificationStatusDto {

    private int id;

    private String user_id;

    private String type;

    private Calendar conductedDate;

    private String conductedBy;

    private boolean isCompleted;

    private String comments;

}
