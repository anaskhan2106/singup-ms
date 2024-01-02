package com.domain;

import javax.persistence.*;
import java.util.Calendar;
import java.util.List;

@Entity
@Table(name="driver_profile")
public class DriverProfile {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "first_name", length = 255)
    private String firstName;

    @Column(name = "last_name", length = 255)
    private String lastName;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "phone", length = 15)
    private String phone;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "is_active", length = 255)
    private boolean isActive;

    @Column(name = "is_documents_verified", length = 255)
    private boolean isDocumentsVerified;

    @Column(name = "is_ready_for_ride", length = 255)
    private boolean isReadyForRide;

    @Column(name = "created_ts")
    private Calendar createdTs;

    @Column(name = "country", length = 255)
    private String country;

    @Column(name = "password", length = 255)
    private String password;

    @OneToMany( mappedBy = "driverProfile" )
    private List< DocumentStore > documentStores;

    public DriverProfile() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean getIsDocumentsVerified() {
        return isDocumentsVerified;
    }

    public void setIsDocumentsVerified(boolean isDocumentsVerified) {
        this.isDocumentsVerified = isDocumentsVerified;
    }

    public boolean getIsReadyForRide() {
        return isReadyForRide;
    }

    public void setIsReadyForRide(boolean isReadyForRide) {
        this.isReadyForRide = isReadyForRide;
    }

    public Calendar getCreatedTs() {
        return createdTs;
    }

    public void setCreatedTs(Calendar createdTs) {
        this.createdTs = createdTs;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List< DocumentStore > getDocumentStores() {
        return this.documentStores;
    }

    public void setDocumentStores(List< DocumentStore > documentStores) {
        this.documentStores = documentStores;
    }
}

