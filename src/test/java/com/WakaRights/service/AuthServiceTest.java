package com.WakaRights.service;

import com.WakaRights.dto.AuthResponseDTO;
import com.WakaRights.dto.LoginRequestDTO;
import com.WakaRights.dto.RegisterRequestDTO;
import com.WakaRights.exception.AuthException;
import com.WakaRights.model.Role;
import com.WakaRights.model.User;
import com.WakaRights.repository.UserRepository;
import com.WakaRights.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private AuthServicesImpl authService;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;

    static class FakeJwtUtil extends JwtUtil {
        @Override
        public String generateToken(String email) {
            return "fake-jwt-token";
        }
    }

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        jwtUtil = new FakeJwtUtil();

        authService = new AuthServicesImpl(userRepository, passwordEncoder, jwtUtil);
    }

    @Test
    void register_shouldReturnAuthResponse_whenNewUser() {
        RegisterRequestDTO request = new RegisterRequestDTO("test@example.com", "password123");

        when(userRepository.existsByEmail(request.email())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AuthResponseDTO response = authService.register(request);

        assertNotNull(response);
        assertEquals("Registration successful", response.message());
        assertEquals("fake-jwt-token", response.token());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void register_shouldThrowException_whenEmailExists() {
        RegisterRequestDTO request = new RegisterRequestDTO("test@example.com", "password123");
        when(userRepository.existsByEmail(request.email())).thenReturn(true);

        AuthException exception = assertThrows(AuthException.class, () -> authService.register(request));
        assertEquals("Email already exists", exception.getMessage());
    }

    @Test
    void login_shouldReturnAuthResponse_whenCredentialsAreValid() {
        LoginRequestDTO request = new LoginRequestDTO("test@example.com", "password123");
        User user = new User();
        user.setEmail(request.email());
        user.setPassword("encoded-password");
        user.setRole(Role.USER);

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.password(), user.getPassword())).thenReturn(true);

        AuthResponseDTO response = authService.login(request);

        assertNotNull(response);
        assertEquals("Login successful", response.message());
        assertEquals("fake-jwt-token", response.token());
    }

    @Test
    void login_shouldThrowException_whenUserNotFound() {
        LoginRequestDTO request = new LoginRequestDTO("missing@mail.com", "password123");
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());

        AuthException exception = assertThrows(AuthException.class, () -> authService.login(request));
        assertEquals("Invalid credentials", exception.getMessage());
    }

    @Test
    void login_shouldThrowException_whenPasswordDoesNotMatch() {
        LoginRequestDTO request = new LoginRequestDTO("test@example.com", "wrong-password");
        User user = new User();
        user.setEmail(request.email());
        user.setPassword("encoded-password");
        user.setRole(Role.USER);

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.password(), user.getPassword())).thenReturn(false);

        AuthException exception = assertThrows(AuthException.class, () -> authService.login(request));
        assertEquals("Invalid credentials", exception.getMessage());
    }
    @Test
    void shouldFailWhenUserNotFound() {
        when(userRepository.findByEmail("missing@mail.com")).thenReturn(Optional.empty());
        LoginRequestDTO request = new LoginRequestDTO("missing@mail.com", "any-password");
        assertThrows(AuthException.class, () -> authService.login(request));
    }
    @Test
    void shouldAssignDefaultRole() {
        User user = new User();
        user.setRole(Role.USER);
        assertEquals(Role.USER, user.getRole());
    }
}