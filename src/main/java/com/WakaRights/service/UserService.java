package com.WakaRights.service;

import com.WakaRights.dto.UserProfileDTO;
import com.WakaRights.dto.UserProfileUpdateDTO;

import java.util.UUID;

public interface UserService {
    UserProfileDTO getProfile(UUID userId);
    UserProfileDTO updateProfile(UUID userId, UserProfileUpdateDTO dto);
}