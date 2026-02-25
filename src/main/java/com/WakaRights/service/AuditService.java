package com.WakaRights.service;

import com.WakaRights.model.AuditLog;
import com.WakaRights.repository.AuditRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuditService {

    private final AuditRepository repository;

    public AuditService(AuditRepository repository) {
        this.repository = repository;
    }

    public void log(UUID userId, String action) {
        AuditLog log = new AuditLog();
        log.setUserId(userId);
        log.setAction(action);
        repository.save(log);
    }
}