package com.WakaRights.controller;

import com.WakaRights.dto.CaseResponseDTO;
import com.WakaRights.model.User;
import com.WakaRights.security.UserPrincipal;
import com.WakaRights.service.CaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/cases")
public class CaseController {

    private final CaseService service;

    public CaseController(CaseService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CaseResponseDTO>> myCases(@AuthenticationPrincipal User user) {
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<CaseResponseDTO> cases = service.getUserCases(user.getId());
        return ResponseEntity.ok(cases);
    }
}