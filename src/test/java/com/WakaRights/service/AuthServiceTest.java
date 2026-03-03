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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private AuthServicesImpl authServicesImpl;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);

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

    @Test
    void shouldEncodePassword() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashed = encoder.encode("password");
        assertNotNull(hashed);
        assertTrue(encoder.matches("password", hashed));
    }

    @Test
    void shouldLoginSuccessfully() {
        User user = new User();
        user.setEmail("test@mail.com");
        user.setPassword("hashed");
        user.setRole(Role.USER);
        when(userRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.of(user));
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        when(passwordEncoder.matches("password123", "hashed")).thenReturn(true);
        JwtUtil fakeJwtUtil = new JwtUtil() {
            @Override
            public String generateToken(String email) {
                return "fake-jwt-token";
            }
        };
        AuthServicesImpl authService =
                new AuthServicesImpl(userRepository, passwordEncoder, fakeJwtUtil);
        LoginRequestDTO request =
                new LoginRequestDTO("test@mail.com", "password123");
        AuthResponseDTO response = authService.login(request);
        assertNotNull(response);
        assertEquals("fake-jwt-token", response.token());
        assertEquals("Login successful", response.message());
    }

    @Test
    void shouldFailLoginWithWrongPassword() {
        User user = new User();
        user.setEmail("test@mail.com");
        user.setPassword("hashed-password");
        when(userRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong-password", "hashed-password"))
                .thenReturn(false);
        JwtUtil fakeJwtUtil = new JwtUtil() {
            @Override
            public String generateToken(String email) {
                return "fake-token";
            }
        };
        AuthServicesImpl authService =
                new AuthServicesImpl(userRepository, passwordEncoder, fakeJwtUtil);
        LoginRequestDTO request =
                new LoginRequestDTO("test@mail.com", "wrong-password");
        assertThrows(AuthException.class, () -> authService.login(request));
    }

}