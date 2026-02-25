package com.WakaRights.service;

import com.WakaRights.dto.EvidenceRequestDTO;
import com.WakaRights.dto.EvidenceResponseDTO;

import java.util.List;
import java.util.UUID;

public interface EvidenceService {
    EvidenceResponseDTO save(EvidenceRequestDTO dto, UUID userId);
    List<EvidenceResponseDTO> getUserEvidence(UUID userId);
}