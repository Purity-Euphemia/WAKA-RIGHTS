package com.WakaRights.service;

import com.WakaRights.dto.AuthResponseDTO;
import com.WakaRights.dto.LoginRequestDTO;
import com.WakaRights.dto.RegisterRequestDTO;

public interface AuthService {
    AuthResponseDTO register(RegisterRequestDTO registerRequestDTO);
    AuthResponseDTO login(LoginRequestDTO loginRequestDTO);
}
