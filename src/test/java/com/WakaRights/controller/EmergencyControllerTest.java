package com.WakaRights.controller;

import com.WakaRights.dto.EmergencyRequestDTO;
import com.WakaRights.security.UserPrincipal;
import com.WakaRights.service.EmergencyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class EmergencyControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private FakeEmergencyService fakeService;

    @BeforeEach
    void setUp() {
        fakeService = new FakeEmergencyService();
        EmergencyController controller = new EmergencyController(fakeService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
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
                        .contentType("application/json")
                        .content(json)
                        .with(authentication(
                                new UsernamePasswordAuthenticationToken(
                                        principal,
                                        null,
                                        principal.getAuthorities()
                                )
                        )))
                .andExpect(status().isOk())
                .andExpect(content().string("Case sent to lawyers/NGOs"));

        assertEquals(true, fakeService.escalateCalled);
        assertEquals(userId, fakeService.receivedUserId);   // ✅ WILL PASS
        assertEquals(evidenceId, fakeService.receivedEvidenceId);
    }
}