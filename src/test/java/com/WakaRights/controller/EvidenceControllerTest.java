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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

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
    @Test
    void myEvidence_emptyList() throws Exception {
        UUID userId = UUID.randomUUID();
        UserPrincipal principal = new UserPrincipal(userId, "test@example.com");
        when(evidenceService.getUserEvidence(userId))
                .thenReturn(List.of());
        mockMvc.perform(get("/api/evidence")
                        .with(user(principal)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
    @Test
    void saveEvidence_largeQueryId() throws Exception {
        UUID userId = UUID.randomUUID();
        UserPrincipal principal = new UserPrincipal(userId, "test@example.com");
        UUID evidenceId = UUID.randomUUID();
        EvidenceResponseDTO response = new EvidenceResponseDTO(
                evidenceId,
                EvidenceType.TEXT,
                EvidenceStatus.PENDING,
                true
        );
        when(evidenceService.save(any(EvidenceRequestDTO.class), any(UUID.class)))
                .thenReturn(response);
        String requestJson = """
            {
                "legalQueryId":"%s",
                "type":"TEXT",
                "base64File":"largeFileData"
            }
            """.formatted(UUID.randomUUID());
        mockMvc.perform(post("/api/evidence")
                        .contentType("application/json")
                        .content(requestJson)
                        .with(user(principal))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("TEXT"));
    }
    @Test
    void saveEvidence_audioType() throws Exception {
        UUID userId = UUID.randomUUID();
        UserPrincipal principal = new UserPrincipal(userId, "test@example.com");
        EvidenceResponseDTO response = new EvidenceResponseDTO(
                UUID.randomUUID(),
                EvidenceType.AUDIO,
                EvidenceStatus.PENDING,
                true
        );
        when(evidenceService.save(any(EvidenceRequestDTO.class), any(UUID.class)))
                .thenReturn(response);
        String requestJson = """
            {
                "legalQueryId":"%s",
                "type":"AUDIO",
                "base64File":"audioFile"
            }
            """.formatted(UUID.randomUUID());
        mockMvc.perform(post("/api/evidence")
                        .contentType("application/json")
                        .content(requestJson)
                        .with(user(principal))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("AUDIO"));
    }
    @Test
    void saveEvidence_videoType() throws Exception {
        UUID userId = UUID.randomUUID();
        UserPrincipal principal = new UserPrincipal(userId, "test@example.com");

        EvidenceResponseDTO response = new EvidenceResponseDTO(
                UUID.randomUUID(),
                EvidenceType.VIDEO,
                EvidenceStatus.PENDING,
                true
        );
        when(evidenceService.save(any(EvidenceRequestDTO.class), any(UUID.class)))
                .thenReturn(response);
        String requestJson = """
            {
                "legalQueryId":"%s",
                "type":"VIDEO",
                "base64File":"videoFile"
            }
            """.formatted(UUID.randomUUID());
        mockMvc.perform(post("/api/evidence")
                        .contentType("application/json")
                        .content(requestJson)
                        .with(user(principal))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("VIDEO"));
    }
    @Test
    void saveEvidence_largeFile() throws Exception {
        UUID userId = UUID.randomUUID();
        UserPrincipal principal = new UserPrincipal(userId, "test@example.com");
        String largeFile = "A".repeat(10000);
        EvidenceResponseDTO response = new EvidenceResponseDTO(
                UUID.randomUUID(),
                EvidenceType.TEXT,
                EvidenceStatus.PENDING,
                true
        );
        when(evidenceService.save(any(EvidenceRequestDTO.class), any(UUID.class)))
                .thenReturn(response);
        String requestJson = """
            {
                "legalQueryId":"%s",
                "type":"TEXT",
                "base64File":"%s"
            }
            """.formatted(UUID.randomUUID(), largeFile);
        mockMvc.perform(post("/api/evidence")
                        .contentType("application/json")
                        .content(requestJson)
                        .with(user(principal))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("TEXT"));
    }
    @Test
    void deleteEvidence_success() throws Exception {
        UUID userId = UUID.randomUUID();
        UserPrincipal principal = new UserPrincipal(userId, "test@example.com");
        UUID evidenceId = UUID.randomUUID();
        doNothing().when(evidenceService).delete(evidenceId);
        mockMvc.perform(delete("/api/evidence/{id}", evidenceId)
                        .with(user(principal))
                        .with(csrf()))
                .andExpect(status().isOk());
        verify(evidenceService, times(1)).delete(evidenceId);
    }


}