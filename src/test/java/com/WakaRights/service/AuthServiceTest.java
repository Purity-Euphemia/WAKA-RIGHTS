package com.WakaRights.service;

import com.WakaRights.dto.AuthResponseDTO;
import com.WakaRights.dto.RegisterRequestDTO;
import com.WakaRights.model.User;
import com.WakaRights.repository.UserRepository;
import com.WakaRights.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private AuthServicesImpl authServicesImpl;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = new BCryptPasswordEncoder();

        // Default service (used for second test)
        authServicesImpl = new AuthServicesImpl(
                userRepository,
                passwordEncoder,
                new JwtUtil()
        );
    }
    
    static class FakeJwtUtil extends JwtUtil {
        @Override
        public String generateToken(String email) {
            return "fake-jwt-token";
        }
    }

    @Test
    void register_shouldReturnAuthResponse_whenNewUser() {
        RegisterRequestDTO request =
                new RegisterRequestDTO("test@example.com", "password123");
        when(userRepository.existsByEmail(request.email()))
                .thenReturn(false);
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        JwtUtil fakeJwtUtil = new FakeJwtUtil();
        AuthServicesImpl testService =
                new AuthServicesImpl(userRepository, passwordEncoder, fakeJwtUtil);
        AuthResponseDTO response = testService.register(request);
        assertNotNull(response);
        assertEquals("Registration successful", response.message());
        assertEquals("fake-jwt-token", response.token());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void register_shouldThrowException_whenEmailExists() {
        RegisterRequestDTO request =
                new RegisterRequestDTO("test@example.com", "password123");
        when(userRepository.existsByEmail(request.email()))
                .thenReturn(true);
        Exception exception = assertThrows(RuntimeException.class, () -> {
            authServicesImpl.register(request);
        });
        assertEquals("Email already exists", exception.getMessage());
    }
}