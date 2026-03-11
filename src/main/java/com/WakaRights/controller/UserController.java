package com.WakaRights.controller;

import com.WakaRights.dto.UserProfileDTO;
import com.WakaRights.security.UserPrincipal;
import com.WakaRights.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService service;

    public UserController(UserService service) { this.service = service; }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDTO> getProfile(@AuthenticationPrincipal UserPrincipal user) {
        UserProfileDTO profile = service.getProfile(user.getId());
        if (profile == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/profile")
    public UserProfileDTO updateProfile(@AuthenticationPrincipal UserPrincipal user,
                                        @RequestBody UserProfileDTO dto) {
        return service.updateProfile(user.getId(), dto);
    }
}