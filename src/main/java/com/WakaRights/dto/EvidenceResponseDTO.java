package com.WakaRights.dto;

import com.WakaRights.model.EvidenceStatus;
import com.WakaRights.model.EvidenceType;

import java.util.UUID;

public record EvidenceResponseDTO(
        UUID id,
        EvidenceType type,
        EvidenceStatus status,
        boolean synced
) {}