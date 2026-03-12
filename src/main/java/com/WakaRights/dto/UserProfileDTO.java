package com.WakaRights.dto;

import jakarta.validation.constraints.NotBlank;

public record UserProfileDTO(
        @NotBlank(message = "Full name must not be blank") String fullName,
        @NotBlank(message = "Phone must not be blank") String phone
) {}