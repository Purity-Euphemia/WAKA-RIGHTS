package com.WakaRights.controller;

import com.WakaRights.dto.LawSummaryDTO;
import com.WakaRights.service.LawLibraryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/library")
public class LawLibraryController {

    private final LawLibraryService service;

    public LawLibraryController(LawLibraryService service) {
        this.service = service;
    }

    @GetMapping("/{topic}")
    public LawSummaryDTO read(@PathVariable String topic) {
        return service.getSummary(topic);
    }
}