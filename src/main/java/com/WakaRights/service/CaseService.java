package com.WakaRights.service;

import com.WakaRights.dto.CaseResponseDTO;
import com.WakaRights.repository.CaseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CaseService {

    private final CaseRepository repository;

    public CaseService(CaseRepository repository) {
        this.repository = repository;
    }

    public List<CaseResponseDTO> getUserCases(UUID userId) {
        return repository.findByUserId(userId)
                .stream()
                .map(c -> new CaseResponseDTO(
                        c.getId(), c.getStatus(), c.getCreatedAt()))
                .toList();
    }
    public void deleteCase(UUID caseId) {
        repository.deleteById(caseId);
    }
}