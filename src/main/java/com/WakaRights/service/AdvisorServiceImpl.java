package com.WakaRights.service;

import com.WakaRights.dto.AdvisorRequestDTO;
import com.WakaRights.dto.AdvisorResponseDTO;
import com.WakaRights.model.LegalQuery;
import com.WakaRights.repository.LegalQueryRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AdvisorServiceImpl implements AdvisorService {

    private final LegalQueryRepository repo;

    public AdvisorServiceImpl(LegalQueryRepository repo) { this.repo = repo; }

    @Override
    public AdvisorResponseDTO askQuestion(UUID userId, AdvisorRequestDTO dto) {
        String answer = "Step-by-step guidance for: " + dto.question();
        LegalQuery q = new LegalQuery();
        q.setUserId(userId);
        q.setQuestion(dto.question());
        q.setAnswer(answer);
        repo.save(q);
        return new AdvisorResponseDTO(answer);
    }
}