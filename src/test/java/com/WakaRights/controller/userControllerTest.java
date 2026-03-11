package com.WakaRights.controller;

import com.WakaRights.dto.UserProfileDTO;
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
}
