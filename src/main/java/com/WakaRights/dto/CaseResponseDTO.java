package com.WakaRights.dto;

import com.WakaRights.model.CaseStatus;

import java.time.Instant;
import java.util.UUID;

public record CaseResponseDTO(
        UUID id,
        CaseStatus status,
        Instant createdAt
) {}