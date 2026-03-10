package com.WakaRights.controller;

import com.WakaRights.dto.CaseResponseDTO;
import com.WakaRights.model.CaseStatus;
import com.WakaRights.security.UserPrincipal;
import com.WakaRights.service.CaseService;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CaseControllerTest {

    @Test
    void myCases_success() throws Exception {

        UUID userId = UUID.randomUUID();

        CaseResponseDTO case1 =
                new CaseResponseDTO(UUID.randomUUID(), CaseStatus.OPEN, Instant.now());

        CaseResponseDTO case2 =
                new CaseResponseDTO(UUID.randomUUID(), CaseStatus.CLOSED, Instant.now());

        CaseService fakeService = new CaseService(null) {
            @Override
            public List<CaseResponseDTO> getUserCases(UUID id) {
                return List.of(case1, case2);
            }
        };

        CaseController controller = new CaseController(fakeService);

        MockMvc mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        UserPrincipal principal = new UserPrincipal(userId, "test@example.com");

        mockMvc.perform(get("/api/cases")
                        .with(user(principal)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].status").value("OPEN"))
                .andExpect(jsonPath("$[1].status").value("CLOSED"))
                .andExpect(jsonPath("$[0].createdAt").exists())
                .andExpect(jsonPath("$[1].createdAt").exists());
    }
}