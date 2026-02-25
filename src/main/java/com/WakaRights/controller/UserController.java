package com.WakaRights.controller;

import com.WakaRights.dto.UserProfileDTO;
import com.WakaRights.security.UserPrincipal;
import com.WakaRights.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService service;

    public UserController(UserService service) { this.service = service; }

    @GetMapping("/profile")
    public UserProfileDTO getProfile(@AuthenticationPrincipal UserPrincipal user) {
        return service.getProfile(user.getId());
    }

    @PutMapping("/profile")
    public UserProfileDTO updateProfile(@AuthenticationPrincipal UserPrincipal user,
                                        @RequestBody UserProfileDTO dto) {
        return service.updateProfile(user.getId(), dto);
    }
}