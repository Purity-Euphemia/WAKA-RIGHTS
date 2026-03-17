package com.WakaRights.controller;

import com.WakaRights.dto.EmergencyRequestDTO;
import com.WakaRights.security.UserPrincipal;
import com.WakaRights.service.EmergencyService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class EmergencyControllerTest {

    private MockMvc mockMvc;
    private FakeEmergencyService fakeService;

    // ✅ Fake service (same style as your Case test)
    static class FakeEmergencyService extends EmergencyService {

        boolean escalateCalled = false;
        UUID receivedUserId;
        UUID receivedEvidenceId;

        FakeEmergencyService() {
            super(null); // because parent needs CaseRepository
        }

        @Override
        public void escalate(UUID userId, UUID evidenceId) {
            escalateCalled = true;
            receivedUserId = userId;
            receivedEvidenceId = evidenceId;
        }
    }

    @BeforeEach
    void setup() {
        fakeService = new FakeEmergencyService();
        EmergencyController controller = new EmergencyController(fakeService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
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
                        .with(user(principal)))
                .andExpect(status().isOk())
                .andExpect(content().string("Case sent to lawyers/NGOs"));
        assertEquals(true, fakeService.escalateCalled);
        assertEquals(userId, fakeService.receivedUserId);
        assertEquals(evidenceId, fakeService.receivedEvidenceId);
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
                            .contentType("application/json")
                            .content(json)
                            .with(user(principal)))
                    .andExpect(status().isOk());
        }
        assertEquals(true, fakeService.escalateCalled);
    }

    @Test
    void escalate_nullUser_shouldFail() throws Exception {
        UUID evidenceId = UUID.randomUUID();
        String json = """
        {
            "evidenceId": "%s"
        }
        """.formatted(evidenceId);
        mockMvc.perform(post("/api/emergency/escalate")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().is4xxClientError());
    }
}