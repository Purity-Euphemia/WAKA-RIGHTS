package com.WakaRights.service;

import com.WakaRights.model.CaseFile;
import com.WakaRights.repository.CaseRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EmergencyService {

    private final CaseRepository caseRepository;

    public EmergencyService(CaseRepository caseRepository) {
        this.caseRepository = caseRepository;
    }

    public void escalate(UUID userId, UUID evidenceId) {
        CaseFile file = new CaseFile();
        file.setUserId(userId);
        file.setEvidenceId(evidenceId);
        caseRepository.save(file);
    }
}