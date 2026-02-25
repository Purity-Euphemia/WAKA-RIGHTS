package com.WakaRights.service;

import com.WakaRights.dto.UserProfileDTO;

import java.util.UUID;

public interface UserService {
    UserProfileDTO getProfile(UUID userId);
    UserProfileDTO updateProfile(UUID userId, UserProfileDTO dto);
}