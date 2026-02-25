package com.WakaRights.service;

import com.WakaRights.dto.LawSummaryDTO;
import org.springframework.stereotype.Service;

@Service
public class LawLibraryService {

    public LawSummaryDTO getSummary(String topic) {
        return new LawSummaryDTO(
                topic,
                "Simplified explanation of " + topic
        );
    }
}