package com.WakaRights.service;

import com.WakaRights.dto.EvidenceRequestDTO;
import com.WakaRights.dto.EvidenceResponseDTO;
import com.WakaRights.exception.EvidenceException;
import com.WakaRights.model.Evidence;
import com.WakaRights.model.EvidenceType;
import com.WakaRights.repository.EvidenceRepository;
import com.WakaRights.utils.FileEncryptionUtil;
import com.WakaRights.utils.HashUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EvidencesServiceTest {

    @Mock
    private EvidenceRepository repository;

    @InjectMocks
    private EvidenceServiceImpl evidenceService;

    @Test
    void shouldSaveEvidence() {
        UUID userId = UUID.randomUUID();
        EvidenceRequestDTO dto = new EvidenceRequestDTO(
                UUID.randomUUID(),
                EvidenceType.PDF,
                "base64dummy"
        );
        try (MockedStatic<FileEncryptionUtil> mockedFileUtil = mockStatic(FileEncryptionUtil.class);
             MockedStatic<HashUtil> mockedHashUtil = mockStatic(HashUtil.class)) {
            mockedFileUtil.when(() -> FileEncryptionUtil.save(anyString()))
                    .thenReturn("fake/path/file.pdf");
            mockedHashUtil.when(() -> HashUtil.sha256(anyString()))
                    .thenReturn("fakehash");
            when(repository.save(any(Evidence.class))).thenAnswer(invocation -> {
                Evidence e = invocation.getArgument(0);
                e.setId(UUID.randomUUID());
                return e;
            });
            EvidenceResponseDTO response = evidenceService.save(dto, userId);
            assertNotNull(response, "Response should not be null");
            assertNotNull(response.id(), "ID should not be null");
            assertEquals(EvidenceType.PDF, response.type());
        }
    }
    @Test
    void shouldRejectNullEvidence() {
        UUID userId = UUID.randomUUID();
        assertThrows(EvidenceException.class, () -> evidenceService.save(null, userId));
    }
    @Test
    void shouldAssignTimestamp() {
        Evidence e = new Evidence();
        evidenceService.assignTimestamp(e);
        assertNotNull(e.getCreatedAt());
    }
}