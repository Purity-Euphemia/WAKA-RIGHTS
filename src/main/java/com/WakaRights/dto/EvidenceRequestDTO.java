package com.WakaRights.dto;

import com.WakaRights.model.EvidenceType;

import java.util.UUID;

public record EvidenceRequestDTO(
        UUID legalQueryId,
        EvidenceType type,
        String base64File
) {}