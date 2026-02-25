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
@Table(name = "evidence")
public class Evidence {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Use just @GeneratedValue if Spring Boot 2
    private UUID id;

    private UUID userId;
    private UUID legalQueryId;

    @Enumerated(EnumType.STRING)
    private EvidenceType type;

    private String filePath;
    private String hash;
    private boolean synced;

    @Enumerated(EnumType.STRING)
    private EvidenceStatus status;

    private Instant createdAt;

    @PrePersist
    void onCreate() {
        createdAt = Instant.now();
        synced = false;
        status = EvidenceStatus.RECORDED;
    }
    public UUID getId() {
        return id;
    }
    public UUID getUserId() {
        return userId;
    }
    public UUID getLegalQueryId() {
        return legalQueryId;
    }
    public EvidenceType getType() {
        return type;
    }
    public String getFilePath() {
        return filePath;
    }
    public String getHash() {
        return hash;
    }
    public boolean isSynced() {
        return synced;
    }
    public EvidenceStatus getStatus() {
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
    public void setLegalQueryId(UUID legalQueryId) {
        this.legalQueryId = legalQueryId;
    }
    public void setType(EvidenceType type) {
        this.type = type;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    public void setHash(String hash) {
        this.hash = hash;
    }
    public void setSynced(boolean synced) {
        this.synced = synced;
    }
    public void setStatus(EvidenceStatus status) {
        this.status = status;
    }
    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}