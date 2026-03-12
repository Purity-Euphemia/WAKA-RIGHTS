package com.WakaRights.controller;

import com.WakaRights.dto.UserProfileDTO;
import com.WakaRights.exception.GlobalExceptionHandler;
import com.WakaRights.security.UserPrincipal;
import com.WakaRights.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class userControllerTest {

    private MockMvc mockMvc;
    private UserService userService;

    @BeforeEach
    void setup() {
        userService = mock(UserService.class);
        UserController controller = new UserController(userService);
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getProfile_returnUserProfile() throws Exception {
        UserProfileDTO profile = new UserProfileDTO("Ada Lovelace", "0700000000");
        when(userService.getProfile(any())).thenReturn(profile);
        mockMvc.perform(get("/api/user/profile")
                        .principal((Principal) () -> "user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Ada Lovelace"))
                .andExpect(jsonPath("$.phone").value("0700000000"));
    }
//    @Test
//    void getProfile_noPrincipal_returnsUnauthorized() throws Exception {
//        mockMvc.perform(get("/api/user/profile"))
//                .andExpect(status().isUnauthorized());
//    }
    @Test
    void updateProfile_success() throws Exception {
        UserProfileDTO updatedProfile = new UserProfileDTO("Grace Hopper", "0800000000");
        when(userService.updateProfile(any(), any())).thenReturn(updatedProfile);
        String jsonBody = "{\"fullName\":\"Grace Hopper\",\"phone\":\"0800000000\"}";
        mockMvc.perform(put("/api/user/profile")
                        .principal((Principal) () -> "user")
                        .contentType("application/json")
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Grace Hopper"))
                .andExpect(jsonPath("$.phone").value("0800000000"));
    }
    @Test
    void getProfile_serviceThrowsException_returnsInternalServerError() throws Exception {
        when(userService.getProfile(any())).thenThrow(new RuntimeException("Service error"));
        mockMvc.perform(get("/api/user/profile")
                        .principal((Principal) () -> "user"))
                .andExpect(status().isInternalServerError());
    }
    @Test
    void updateProfile_serviceThrowsException_returnsInternalServerError() throws Exception {
        when(userService.updateProfile(any(), any())).thenThrow(new RuntimeException("Service error"));
        String jsonBody = "{\"fullName\":\"Ada\",\"phone\":\"0700000000\"}";
        mockMvc.perform(put("/api/user/profile")
                        .principal((Principal) () -> "user")
                        .contentType("application/json")
                        .content(jsonBody))
                .andExpect(status().isInternalServerError());
    }
    @Test
    void getProfile_emptyProfile_returnsNotFound() throws Exception {
        when(userService.getProfile(any())).thenReturn(null);
        mockMvc.perform(get("/api/user/profile")
                        .principal((Principal) () -> "user"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getProfile_multipleCalls_returnCorrectData() throws Exception {
        UserProfileDTO profile1 = new UserProfileDTO("User One", "0701111111");
        UserProfileDTO profile2 = new UserProfileDTO("User Two", "0702222222");
        when(userService.getProfile(any()))
                .thenReturn(profile1)
                .thenReturn(profile2);
        mockMvc.perform(get("/api/user/profile")
                        .principal((Principal) () -> "user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("User One"));
        mockMvc.perform(get("/api/user/profile")
                        .principal((Principal) () -> "user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("User Two"));
    }
    @Test
    void updateProfile_partialUpdate_success() throws Exception {
        UserProfileDTO updatedProfile = new UserProfileDTO("Ada Lovelace", "0709999999");
        when(userService.updateProfile(any(), any())).thenReturn(updatedProfile);
        String jsonBody = "{\"phone\":\"0709999999\"}";
        mockMvc.perform(put("/api/user/profile")
                        .principal((Principal) () -> "user")
                        .contentType("application/json")
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Ada Lovelace"))
                .andExpect(jsonPath("$.phone").value("0709999999"));
    }




}
