package com.WakaRights.controller;

import com.WakaRights.dto.CaseResponseDTO;
import com.WakaRights.security.UserPrincipal;
import com.WakaRights.service.CaseService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cases")
public class CaseController {

    private final CaseService service;

    public CaseController(CaseService service) {
        this.service = service;
    }

    @GetMapping
    public List<CaseResponseDTO> myCases(
            @AuthenticationPrincipal UserPrincipal user) {
        return service.getUserCases(user.getId());
    }
}