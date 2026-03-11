package com.WakaRights.service;

import com.WakaRights.dto.CaseResponseDTO;
import com.WakaRights.model.CaseFile;
import com.WakaRights.model.CaseStatus;
import com.WakaRights.repository.CaseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class caseServiceTest {

    private CaseService caseService;
    private CaseRepository caseRepository;

    @BeforeEach
    void setUp() {
        caseRepository = mock(CaseRepository.class);
        caseService = new CaseService(caseRepository);
    }
    @Test
    void getUserCases_returnsMappedDTOs() {
        UUID userId = UUID.randomUUID();
        CaseFile case1 = new CaseFile();
        case1.setId(UUID.randomUUID());
        case1.setStatus(CaseStatus.OPEN);
        case1.setCreatedAt(Instant.now());
        case1.setUserId(userId);
        CaseFile case2 = new CaseFile();
        case2.setId(UUID.randomUUID());
        case2.setStatus(CaseStatus.CLOSED);
        case2.setCreatedAt(Instant.now());
        case2.setUserId(userId);

        when(caseRepository.findByUserId(userId))
                .thenReturn(List.of(case1, case2));
        List<CaseResponseDTO> result = caseService.getUserCases(userId);
        assertEquals(2, result.size());
        assertEquals(CaseStatus.OPEN, result.get(0).status());
        assertEquals(CaseStatus.CLOSED, result.get(1).status());
        verify(caseRepository, times(1)).findByUserId(userId);
    }
    @Test
    void getUserCases_returnsEmptyListWhenNoCases() {
        UUID userId = UUID.randomUUID();
        when(caseRepository.findByUserId(userId))
                .thenReturn(List.of());
        List<CaseResponseDTO> result = caseService.getUserCases(userId);
        assertTrue(result.isEmpty());
        verify(caseRepository, times(1)).findByUserId(userId);
    }
    @Test
    void deleteCase_callsRepositoryDelete() {
        UUID caseId = UUID.randomUUID();
        caseService.deleteCase(caseId);
        verify(caseRepository, times(1)).deleteById(caseId);
    }
    @Test
    void getUserCases_returnsCorrectId() {
        UUID userId = UUID.randomUUID();
        CaseFile caseFile = new CaseFile();
        UUID caseId = UUID.randomUUID();
        caseFile.setId(caseId);
        caseFile.setStatus(CaseStatus.OPEN);
        caseFile.setCreatedAt(Instant.now());
        caseFile.setUserId(userId);
        when(caseRepository.findByUserId(userId)).thenReturn(List.of(caseFile));
        List<CaseResponseDTO> result = caseService.getUserCases(userId);
        assertEquals(caseId, result.get(0).id());
    }
    @Test
    void getUserCases_returnsCorrectCreatedAt() {
        UUID userId = UUID.randomUUID();
        Instant now = Instant.now();
        CaseFile caseFile = new CaseFile();
        caseFile.setId(UUID.randomUUID());
        caseFile.setStatus(CaseStatus.OPEN);
        caseFile.setCreatedAt(now);
        caseFile.setUserId(userId);
        when(caseRepository.findByUserId(userId)).thenReturn(List.of(caseFile));
        List<CaseResponseDTO> result = caseService.getUserCases(userId);
        assertEquals(now, result.get(0).createdAt());
    }



}
