package com.WakaRights.controller;

import com.WakaRights.dto.EvidenceRequestDTO;
import com.WakaRights.dto.EvidenceResponseDTO;
import com.WakaRights.security.UserPrincipal;
import com.WakaRights.service.EvidenceService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/evidence")
public class EvidenceController {

    private final EvidenceService service;

    public EvidenceController(EvidenceService service) {
        this.service = service;
    }

    @PostMapping
    public EvidenceResponseDTO upload(
            @AuthenticationPrincipal UserPrincipal user,
            @Valid @RequestBody EvidenceRequestDTO dto) {
        return service.save(dto, user.getId());
    }

    @GetMapping
    public List<EvidenceResponseDTO> myEvidence(
            @AuthenticationPrincipal UserPrincipal user) {
        return service.getUserEvidence(user.getId());
    }
    @DeleteMapping("/{id}")
    public void deleteEvidence(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal user) {

        service.delete(id);
    }
}