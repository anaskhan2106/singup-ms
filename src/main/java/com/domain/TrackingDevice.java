package com.domain;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name="tracking_device")
public class TrackingDevice {

    @Id
    @Column(name = "id", length = 45)
    private int id;

    @Column(name = "type", length = 255)
    private String type;

    @Column(name = "approved_ts", length = 255)
    private Calendar approvedTs;

    @Column(name = "serial_number", length = 255)
    private String serialNumber;

    @Column(name = "uploaded_ts", length = 15)
    private Calendar uploadedTs;

    @Column(name = "comments", length = 255)
    private String comments;

    @ManyToOne
    @JoinColumn( name = "user_id" )
    private DriverProfile driverProfile;

    public TrackingDevice() {
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

    public Calendar getApprovedTs() {
        return approvedTs;
    }

    public void setApprovedTs(Calendar approvedTs) {
        this.approvedTs = approvedTs;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Calendar getUploadedTs() {
        return uploadedTs;
    }

    public void setUploadedTs(Calendar uploadedTs) {
        this.uploadedTs = uploadedTs;
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
