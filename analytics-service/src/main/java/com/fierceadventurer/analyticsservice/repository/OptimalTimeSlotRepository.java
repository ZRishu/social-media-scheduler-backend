package com.fierceadventurer.analyticsservice.repository;

import com.fierceadventurer.analyticsservice.entity.OptimalTimeSlot;
import com.fierceadventurer.analyticsservice.entity.PostMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OptimalTimeSlotRepository extends JpaRepository<OptimalTimeSlot, UUID> {
    List<OptimalTimeSlot> findBySocialAccountIdOrderByEngagementScoreDesc(UUID socialAccountId);

    void deleteAllBySocialAccountId(UUID socialAccountId);

}
