package com.WakaRights.controller;

import com.WakaRights.dto.EvidenceRequestDTO;
import com.WakaRights.dto.EvidenceResponseDTO;
import com.WakaRights.model.Evidence;
import com.WakaRights.model.EvidenceStatus;
import com.WakaRights.model.EvidenceType;
import com.WakaRights.security.UserPrincipal;
import com.WakaRights.service.EvidenceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(EvidenceController.class)
class EvidenceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EvidenceService evidenceService;

    @Test
    void uploadEvidence_authenticated() throws Exception {
        UUID userId = UUID.randomUUID();
        UserPrincipal principal = new UserPrincipal(userId, "test@example.com");
        UUID evidenceId = UUID.randomUUID();
        EvidenceResponseDTO response = new EvidenceResponseDTO(
                evidenceId,
                EvidenceType.PDF,
                EvidenceStatus.PENDING,
                true
        );
        when(evidenceService.save(any(EvidenceRequestDTO.class), any(UUID.class)))
                .thenReturn(response);
        String requestJson = """
                {
                    "legalQueryId":"%s",
                    "type":"PDF",
                    "base64File":"base64dummy"
                }
                """.formatted(UUID.randomUUID());
        mockMvc.perform(post("/api/evidence")
                        .contentType("application/json")
                        .content(requestJson)
                        .with(user(principal))
                        .with(csrf())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(evidenceId.toString()))
                .andExpect(jsonPath("$.type").value("PDF"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.synced").value(true));
    }
    @Test
    void uploadEvidence_unauthenticated() throws Exception {
        String requestJson = """
                {
                    "legalQueryId":"%s",
                    "type":"PDF",
                    "base64File":"base64dummy"
                }
                """.formatted(UUID.randomUUID());
        mockMvc.perform(post("/api/evidence")
                        .contentType("application/json")
                        .content(requestJson)
                        .with(csrf())
                )
                .andExpect(status().isUnauthorized());
    }
    @Test
    void uploadEvidence_invalidRequest() throws Exception {

        UUID userId = UUID.randomUUID();
        UserPrincipal principal = new UserPrincipal(userId, "test@example.com");

        String invalidJson = """
        { "type":"PDF" }
        """;

        mockMvc.perform(post("/api/evidence")
                        .contentType("application/json")
                        .content(invalidJson)
                        .with(user(principal))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.legalQueryId").value("legalQueryId is required"))
                .andExpect(jsonPath("$.base64File").value("base64File is required"));
    }
    @Test
    void myEvidence_serviceCalled() throws Exception {

        UUID userId = UUID.randomUUID();
        UserPrincipal principal = new UserPrincipal(userId, "test@example.com");

        when(evidenceService.getUserEvidence(userId))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/evidence")
                        .with(user(principal)))
                .andExpect(status().isOk());

        verify(evidenceService, times(1)).getUserEvidence(userId);
    }

}