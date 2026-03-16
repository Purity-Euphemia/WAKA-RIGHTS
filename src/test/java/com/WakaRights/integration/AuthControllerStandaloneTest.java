package com.WakaRights.integration;

import com.WakaRights.controller.AuthController;
import com.WakaRights.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerStandaloneTest {

    private MockMvc mockMvc;
    private AuthService authService;

    @BeforeEach
    void setup() {
        authService = Mockito.mock(AuthService.class);
        AuthController authController = new AuthController(authService);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void missingContentType() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .content("{\"email\":\"test@mail.com\",\"password\":\"123456\"}"))
                .andExpect(status().isUnsupportedMediaType());
    }
}