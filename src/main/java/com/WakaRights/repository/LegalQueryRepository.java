package com.WakaRights.repository;

import com.WakaRights.model.LegalQuery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LegalQueryRepository extends JpaRepository<LegalQuery, UUID> {
    List<LegalQuery> findByUserId(UUID userId);
}