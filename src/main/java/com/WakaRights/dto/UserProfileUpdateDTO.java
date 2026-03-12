package com.WakaRights.dto;

import jakarta.validation.constraints.NotBlank;

public record UserProfileUpdateDTO(
        @NotBlank(message = "Full name is required")
        String fullName,
        String phone
) {}
