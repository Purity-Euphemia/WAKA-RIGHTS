package com.WakaRights.controller;

import com.WakaRights.dto.EmergencyRequestDTO;
import com.WakaRights.security.SecurityConfig;
import com.WakaRights.security.UserPrincipal;
import com.WakaRights.service.EmergencyService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmergencyController.class)
@AutoConfigureMockMvc(addFilters = true)
@Import(SecurityConfig.class)
class EmergencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmergencyService emergencyService;

    @Test
    void escalate_nullUser_shouldFail() throws Exception {
        UUID evidenceId = UUID.randomUUID();

        String json = """
        {
            "evidenceId": "%s"
        }
        """.formatted(evidenceId);

        mockMvc.perform(post("/api/emergency/escalate")
                        .with(anonymous())
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void escalate_callsServiceSuccessfully() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID evidenceId = UUID.randomUUID();
        UserPrincipal principal = new UserPrincipal(userId, "test@example.com");

        String json = """
        {
            "evidenceId": "%s"
        }
        """.formatted(evidenceId);

        mockMvc.perform(post("/api/emergency/escalate")
                        .with(user(principal))
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("Case sent to lawyers/NGOs"));

        // ✅ VERIFY service was called
        verify(emergencyService, times(1))
                .escalate(userId, evidenceId);
    }

    @Test
    void escalate_multipleRequestsWork() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID evidenceId = UUID.randomUUID();
        UserPrincipal principal = new UserPrincipal(userId, "multi@test.com");

        String json = """
        {
            "evidenceId": "%s"
        }
        """.formatted(evidenceId);

        for (int i = 0; i < 3; i++) {
            mockMvc.perform(post("/api/emergency/escalate")
                            .with(user(principal))
                            .contentType("application/json")
                            .content(json))
                    .andExpect(status().isOk());
        }

        verify(emergencyService, times(3))
                .escalate(userId, evidenceId);
    }
}