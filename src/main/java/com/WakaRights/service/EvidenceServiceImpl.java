package com.WakaRights.service;

import com.WakaRights.dto.EvidenceRequestDTO;
import com.WakaRights.dto.EvidenceResponseDTO;
import com.WakaRights.exception.EvidenceException;
import com.WakaRights.model.Evidence;
import com.WakaRights.repository.EvidenceRepository;
import com.WakaRights.utils.FileEncryptionUtil;
import com.WakaRights.utils.HashUtil;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class EvidenceServiceImpl implements EvidenceService {

    private final EvidenceRepository repository;

    public EvidenceServiceImpl(EvidenceRepository repository) {
        this.repository = repository;
    }

    @Override
    public EvidenceResponseDTO save(EvidenceRequestDTO dto, UUID userId) {
        if (dto == null) {
            throw new EvidenceException("Evidence cannot be null");
        }
        if (userId == null) {
            throw new EvidenceException("User ID cannot be null");
        }
        Evidence e = new Evidence();
        e.setUserId(userId);
        e.setLegalQueryId(dto.legalQueryId());
        e.setType(dto.type());
        e.setFilePath(FileEncryptionUtil.save(dto.base64File()));
        e.setHash(HashUtil.sha256(dto.base64File()));

        assignTimestamp(e);

        repository.save(e);

        return new EvidenceResponseDTO(
                e.getId(),
                e.getType(),
                e.getStatus(),
                e.isSynced()
        );
    }

    @Override
    public List<EvidenceResponseDTO> getUserEvidence(UUID userId) {
        if (userId == null) {
            throw new EvidenceException("User ID cannot be null");
        }
        return repository.findByUserId(userId)
                .stream()
                .map(e -> new EvidenceResponseDTO(
                        e.getId(),
                        e.getType(),
                        e.getStatus(),
                        e.isSynced()))
                .toList();
    }

    public EvidenceResponseDTO getById(UUID id) {
        if (id == null) {
            throw new EvidenceException("Evidence ID cannot be null");
        }
        Evidence e = repository.findById(id)
                .orElseThrow(() -> new EvidenceException("Evidence not found"));
        return new EvidenceResponseDTO(
                e.getId(),
                e.getType(),
                e.getStatus(),
                e.isSynced()
        );
    }

    public void assignTimestamp(Evidence evidence) {
        if (evidence != null) {
            evidence.setCreatedAt(Instant.now());
        }
    }
}