package com.WakaRights.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.PrePersist;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "case_files")
public class CaseFile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Use just @GeneratedValue if Spring Boot 2
    private UUID id;

    private UUID userId;
    private UUID evidenceId;

    @Enumerated(EnumType.STRING)
    private CaseStatus status;

    private Instant createdAt;

    @PrePersist
    void onCreate() {
        createdAt = Instant.now();
        status = CaseStatus.REPORTED;
    }
    public UUID getId() {
        return id;
    }
    public UUID getUserId() {
        return userId;
    }
    public UUID getEvidenceId() {
        return evidenceId;
    }

    public CaseStatus getStatus() {
        return status;
    }
    public Instant getCreatedAt() {
        return createdAt;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public void setUserId(UUID userId) {
        this.userId = userId;
    }
    public void setEvidenceId(UUID evidenceId) {
        this.evidenceId = evidenceId;
    }
    public void setStatus(CaseStatus status) {
        this.status = status;
    }
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}