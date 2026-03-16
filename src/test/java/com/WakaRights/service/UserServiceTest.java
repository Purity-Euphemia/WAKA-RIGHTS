package com.WakaRights.service;

import com.WakaRights.dto.UserProfileDTO;
import com.WakaRights.model.UserProfile;
import com.WakaRights.repository.UserProfileRepository;
import com.WakaRights.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    private UserServiceImpl userService;
    private UserProfileRepository userRepository;


    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserProfileRepository.class);
        userService = new UserServiceImpl(userRepository);
    }
    @Test
    void testFindUserByEmail() {
        UUID userId = UUID.randomUUID();
        UserProfile user = new UserProfile();
        user.setUserId(userId);
        user.setFullName("John Doe");
        user.setPhone("1234567890");
        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));
        UserProfileDTO dto = userService.getProfile(userId);
        assertEquals("John Doe", dto.fullName());
        assertEquals("1234567890", dto.phone());
    }

}
