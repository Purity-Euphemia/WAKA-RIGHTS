package com.WakaRights.repository;

import com.WakaRights.model.CaseFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CaseRepository extends JpaRepository<CaseFile, UUID> {
    List<CaseFile> findByUserId(UUID userId);
}