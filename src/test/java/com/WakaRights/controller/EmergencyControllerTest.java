package com.WakaRights.controller;

import com.WakaRights.dto.EmergencyRequestDTO;
import com.WakaRights.security.SecurityConfig;
import com.WakaRights.security.UserPrincipal;
import com.WakaRights.service.EmergencyService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(EmergencyController.class)
@AutoConfigureMockMvc(addFilters = true) // ensures Spring Security is applied
@Import(SecurityConfig.class)
public class EmergencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmergencyService emergencyService;

    static class FakeEmergencyService extends EmergencyService {
        boolean escalateCalled = false;
        UUID receivedUserId;
        UUID receivedEvidenceId;

        FakeEmergencyService() { super(null); }

        @Override
        public void escalate(UUID userId, UUID evidenceId) {
            escalateCalled = true;
            receivedUserId = userId;
            receivedEvidenceId = evidenceId;
        }
    }

    private FakeEmergencyService fakeService;

    @BeforeEach
    void setup() {
        fakeService = new FakeEmergencyService();
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
                        .with(anonymous())
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isUnauthorized()); // ✅ this will now pass
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
}
}