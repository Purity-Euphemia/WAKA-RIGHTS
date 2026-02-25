package com.WakaRights.service;

import com.WakaRights.dto.UserProfileDTO;
import com.WakaRights.model.UserProfile;
import com.WakaRights.repository.UserProfileRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserProfileRepository repo;

    public UserServiceImpl(UserProfileRepository repo) { this.repo = repo; }

    public UserProfileDTO getProfile(UUID userId) {
        UserProfile p = repo.findByUserId(userId).orElseThrow();
        return new UserProfileDTO(p.getFullName(), p.getPhone());
    }

    public UserProfileDTO updateProfile(UUID userId, UserProfileDTO dto) {
        UserProfile p = repo.findByUserId(userId).orElse(new UserProfile());
        p.setUserId(userId);
        p.setFullName(dto.fullName());
        p.setPhone(dto.phone());
        repo.save(p);
        return dto;
    }
}