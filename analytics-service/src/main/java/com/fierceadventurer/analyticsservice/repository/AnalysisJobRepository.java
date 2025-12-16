package com.fierceadventurer.analyticsservice.repository;

import com.fierceadventurer.analyticsservice.entity.AnalysisJob;
import com.fierceadventurer.analyticsservice.enums.AnalysisStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AnalysisJobRepository extends JpaRepository<AnalysisJob, UUID>{
    Optional<AnalysisJob> findBySocialAccountId(UUID socialAccountId);

    List<AnalysisJob> findTopByStatusOrderByCreatedAtAsc(AnalysisStatus status);
}
