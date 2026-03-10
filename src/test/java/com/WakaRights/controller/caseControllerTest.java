package com.WakaRights.controller;
import com.WakaRights.dto.CaseResponseDTO;
import com.WakaRights.model.CaseStatus;
import com.WakaRights.security.UserPrincipal;
import com.WakaRights.service.CaseService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class caseControllerTest {

    private MockMvc mockMvc;
    private TestCaseService fakeService;

    static class TestCaseService extends CaseService {
        List<CaseResponseDTO> data = new ArrayList<>();
        UUID receivedUserId;
        TestCaseService() {
            super(null);
        }

        @Override
        public List<CaseResponseDTO> getUserCases(UUID userId) {
            receivedUserId = userId;
            return data;
        }
    }

    @BeforeEach
    void setup() {
        fakeService = new TestCaseService();
        CaseController controller = new CaseController(fakeService);
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    void myCases_returnsCasesSuccessfully() throws Exception {
        UUID userId = UUID.randomUUID();
        fakeService.data = List.of(
                new CaseResponseDTO(UUID.randomUUID(), CaseStatus.OPEN, Instant.now()),
                new CaseResponseDTO(UUID.randomUUID(), CaseStatus.CLOSED, Instant.now())
        );
        UserPrincipal principal = new UserPrincipal(userId, "test@example.com");
        mockMvc.perform(get("/api/cases").with(user(principal)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].status").value("OPEN"))
                .andExpect(jsonPath("$[1].status").value("CLOSED"))
                .andExpect(jsonPath("$[0].createdAt").exists())
                .andExpect(jsonPath("$[1].createdAt").exists());
    }
    @Test
    void myCases_returnsEmptyList() throws Exception {
        UUID userId = UUID.randomUUID();
        fakeService.data = List.of();
        UserPrincipal principal = new UserPrincipal(userId, "test@example.com");
        mockMvc.perform(get("/api/cases").with(user(principal)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
    @Test
    void myCases_multipleCasesReturned() throws Exception {
        UUID userId = UUID.randomUUID();
        fakeService.data = List.of(
                new CaseResponseDTO(UUID.randomUUID(), CaseStatus.OPEN, Instant.now()),
                new CaseResponseDTO(UUID.randomUUID(), CaseStatus.OPEN, Instant.now()),
                new CaseResponseDTO(UUID.randomUUID(), CaseStatus.CLOSED, Instant.now())
        );
        UserPrincipal principal = new UserPrincipal(userId, "test@example.com");
        mockMvc.perform(get("/api/cases").with(user(principal)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }
    @Test
    void myCases_responseContainsCreatedAt() throws Exception {
        UUID userId = UUID.randomUUID();
        fakeService.data = List.of(
                new CaseResponseDTO(UUID.randomUUID(), CaseStatus.OPEN, Instant.now())
        );
        UserPrincipal principal = new UserPrincipal(userId, "test@example.com");
        mockMvc.perform(get("/api/cases").with(user(principal)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].createdAt").exists());
    }
    @Test
    void myCases_returnsOnlyOpenCases() throws Exception {
        UUID userId = UUID.randomUUID();
        fakeService.data = List.of(
                new CaseResponseDTO(UUID.randomUUID(), CaseStatus.OPEN, Instant.now()),
                new CaseResponseDTO(UUID.randomUUID(), CaseStatus.CLOSED, Instant.now())
        );
        UserPrincipal principal = new UserPrincipal(userId, "open@test.com");
        mockMvc.perform(get("/api/cases").with(user(principal)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("OPEN"))
                .andExpect(jsonPath("$[1].status").value("CLOSED"));
    }

}
