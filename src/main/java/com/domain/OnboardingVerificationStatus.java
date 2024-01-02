package com.domain;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name="onboarding_verification_status")
public class OnboardingVerificationStatus {

    @Id
    @Column(name = "id", length = 45)
    private int id;

    @Column(name = "type", length = 255)
    private String type;

    @Column(name = "conducted_date", length = 255)
    private Calendar conductedDate;

    @Column(name = "conducted_by", length = 255)
    private String conductedBy;

    @Column(name = "is_completed", length = 15)
    private boolean isCompleted;

    @Column(name = "comments", length = 255)
    private String comments;

    @ManyToOne
    @JoinColumn( name = "user_id" )
    private DriverProfile driverProfile;

    public OnboardingVerificationStatus() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Calendar getConductedDate() {
        return conductedDate;
    }

    public void setConductedDate(Calendar conductedDate) {
        this.conductedDate = conductedDate;
    }

    public String getConductedBy() {
        return conductedBy;
    }

    public void setConductedBy(String conductedBy) {
        this.conductedBy = conductedBy;
    }

    public boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public DriverProfile getDriverProfile() {
        return this.driverProfile;
    }

    public void setDriverProfile(DriverProfile driverProfile) {
        this.driverProfile = driverProfile;
    }
}
