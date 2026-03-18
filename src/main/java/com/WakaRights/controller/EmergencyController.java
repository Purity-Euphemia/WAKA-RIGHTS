package com.WakaRights.controller;

import com.WakaRights.dto.EmergencyRequestDTO;
import com.WakaRights.security.UserPrincipal;
import com.WakaRights.service.EmergencyService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/emergency")
public class EmergencyController {

    private final EmergencyService service;

    public EmergencyController(EmergencyService service) {
        this.service = service;
    }

    @PostMapping("/escalate")
    public String escalate(@AuthenticationPrincipal UserPrincipal user,
                           @RequestBody EmergencyRequestDTO dto) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
        }
        service.escalate(user.getId(), dto.evidenceId());
        return "Case sent to lawyers/NGOs";
    }
}