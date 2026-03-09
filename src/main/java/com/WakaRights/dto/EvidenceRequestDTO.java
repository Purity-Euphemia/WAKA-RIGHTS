package com.WakaRights.dto;

import com.WakaRights.model.EvidenceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record EvidenceRequestDTO(

        @NotNull(message = "legalQueryId is required")
        UUID legalQueryId,

        @NotNull(message = "type is required")
        EvidenceType type,

        @NotBlank(message = "base64File is required")
        String base64File

) {}