package com.WakaRights.repository;

import com.WakaRights.model.Evidence;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class EvidenceRepositoryTest {
    @Autowired
    private EvidenceRepository evidenceRepository;

    @Test
    void saveEvidence() {
        Evidence evidence = new Evidence();
        evidence.setUserId(UUID.randomUUID());
        Evidence saved = evidenceRepository.save(evidence);
        assertNotNull(saved.getId());
    }
    @Test
    void findEvidenceByUserId() {
        UUID userId = UUID.randomUUID();
        Evidence evidence = new Evidence();
        evidence.setUserId(userId);
        evidenceRepository.save(evidence);
        assertEquals(1, evidenceRepository.findByUserId(userId).size());
    }
}
