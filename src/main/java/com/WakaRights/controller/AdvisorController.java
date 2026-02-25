package com.WakaRights.controller;

import com.WakaRights.dto.AdvisorRequestDTO;
import com.WakaRights.dto.AdvisorResponseDTO;
import com.WakaRights.security.UserPrincipal;
import com.WakaRights.service.AdvisorService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/advisor")
public class AdvisorController {

    private final AdvisorService service;

    public AdvisorController(AdvisorService service) { this.service = service; }

    @PostMapping("/ask")
    public AdvisorResponseDTO ask(@AuthenticationPrincipal UserPrincipal user,
                                  @RequestBody AdvisorRequestDTO dto) {
        return service.askQuestion(user.getId(), dto);
    }
}