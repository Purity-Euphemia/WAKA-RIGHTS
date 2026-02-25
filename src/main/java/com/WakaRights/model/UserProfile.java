package com.WakaRights.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.util.UUID;

@Entity
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID userId;
    private String fullName;
    private String phone;

    public UUID getId() {
        return id;
    }
    public UUID getUserId() {
        return userId;
    }
    public String getFullName() {
        return fullName;
    }
    public String getPhone() {
        return phone;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public void setUserId(UUID userId) {
        this.userId = userId;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
}