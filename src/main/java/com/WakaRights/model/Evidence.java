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
    @GeneratedValue(strategy = GenerationType.UUID)
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
    private boolean locked = false;

    @PrePersist
    void onCreate() {
        createdAt = Instant.now();
        synced = false;
        status = EvidenceStatus.RECORDED;
    }

    public void lock() {
        this.locked = true;
    }

    public boolean isLocked() {
        return locked;
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

    public void setId(UUID id) { this.id = id; }

    public void setUserId(UUID userId) {
        checkLocked();
        this.userId = userId;
    }

    public void setLegalQueryId(UUID legalQueryId) {
        checkLocked();
        this.legalQueryId = legalQueryId;
    }

    public void setType(EvidenceType type) {
        checkLocked();
        this.type = type;
    }

    public void setFilePath(String filePath) {
        checkLocked();
        this.filePath = filePath;
    }

    public void setHash(String hash) {
        checkLocked();
        this.hash = hash;
    }

    public void setSynced(boolean synced) {
        checkLocked();
        this.synced = synced;
    }

    public void setStatus(EvidenceStatus status) {
        checkLocked();
        this.status = status;
    }

    public void setCreatedAt(Instant createdAt) {
        checkLocked();
        this.createdAt = createdAt;
    }

    private void checkLocked() {
        if (locked) {
            throw new IllegalStateException("Evidence is locked and cannot be modified");
        }
    }
}