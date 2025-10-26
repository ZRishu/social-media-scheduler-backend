package com.fierceadventurer.analyticsservice.controllers;

import com.fierceadventurer.analyticsservice.dto.AnalysisJobDto;
import com.fierceadventurer.analyticsservice.dto.NextBestTimeResponseDto;
import com.fierceadventurer.analyticsservice.dto.OptimalTimeSlotDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    @GetMapping("/{socialAccountId}/next-best-time")
    public ResponseEntity<NextBestTimeResponseDto> getNextBestTime(
            @PathVariable UUID socialAccountId) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{socialAccountId}/job-status")
    public ResponseEntity<AnalysisJobDto> getAnalysisJobStatus(
            @PathVariable UUID socialAccountId) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{socialAccountId}/time-slots")
    public ResponseEntity<List<OptimalTimeSlotDto>> getAllTimeSlotsForAccount(
            @PathVariable UUID socialAccountId) {
        return ResponseEntity.ok().build();
    }
}
