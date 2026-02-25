package com.WakaRights.service;

import com.WakaRights.dto.AdvisorRequestDTO;
import com.WakaRights.dto.AdvisorResponseDTO;

import java.util.UUID;

public interface AdvisorService {
    AdvisorResponseDTO askQuestion(UUID userId, AdvisorRequestDTO dto);
}