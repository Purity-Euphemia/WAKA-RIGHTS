package com.WakaRights.service;

import com.WakaRights.dto.UserProfileDTO;
import com.WakaRights.dto.UserProfileUpdateDTO;
import com.WakaRights.model.UserProfile;
import com.WakaRights.repository.UserProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    @Test
    void testUpdateProfile_existingUser() {
        UUID userId = UUID.randomUUID();
        UserProfile user = new UserProfile();
        user.setUserId(userId);
        user.setFullName("John Doe");
        user.setPhone("1234567890");
        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(UserProfile.class))).thenAnswer(i -> i.getArgument(0));
        UserProfileUpdateDTO updateDTO = new UserProfileUpdateDTO("Jane Doe", "0987654321");
        UserProfileDTO updated = userService.updateProfile(userId, updateDTO);
        assertEquals("Jane Doe", updated.fullName());
        assertEquals("0987654321", updated.phone());
        verify(userRepository).save(any(UserProfile.class));
    }
    @Test
    void testUpdateProfile_newUser() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(userRepository.save(any(UserProfile.class))).thenAnswer(i -> i.getArgument(0));
        UserProfileUpdateDTO updateDTO = new UserProfileUpdateDTO("New User", "1112223333");
        UserProfileDTO updated = userService.updateProfile(userId, updateDTO);
        assertEquals("New User", updated.fullName());
        assertEquals("1112223333", updated.phone());
        verify(userRepository).save(any(UserProfile.class));
    }
    @Test
    void testGetProfile_userNotFound() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findByUserId(userId)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> userService.getProfile(userId));
    }
    @Test
    void testUpdateProfile_onlyFullName() {
        UUID userId = UUID.randomUUID();
        UserProfile user = new UserProfile();
        user.setUserId(userId);
        user.setFullName("John Doe");
        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(UserProfile.class))).thenAnswer(i -> i.getArgument(0));
        UserProfileUpdateDTO updateDTO = new UserProfileUpdateDTO("Jane Doe", null);
        UserProfileDTO updated = userService.updateProfile(userId, updateDTO);
        assertEquals("Jane Doe", updated.fullName());
        assertNull(updated.phone());
    }
    @Test
    void testUpdateProfile_onlyPhone() {
        UUID userId = UUID.randomUUID();
        UserProfile user = new UserProfile();
        user.setUserId(userId);
        user.setPhone("1234567890");
        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(UserProfile.class))).thenAnswer(i -> i.getArgument(0));
        UserProfileUpdateDTO updateDTO = new UserProfileUpdateDTO(null, "0987654321");
        UserProfileDTO updated = userService.updateProfile(userId, updateDTO);
        assertNull(updated.fullName());
        assertEquals("0987654321", updated.phone());
    }



}
