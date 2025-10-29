package com.fierceadventurer.analyticsservice.service;

import com.fierceadventurer.analyticsservice.dto.AnalysisJobDto;
import com.fierceadventurer.analyticsservice.dto.NextBestTimeResponseDto;
import com.fierceadventurer.analyticsservice.dto.OptimalTimeSlotDto;
import com.fierceadventurer.analyticsservice.entity.AnalysisJob;
import com.fierceadventurer.analyticsservice.entity.OptimalTimeSlot;

import java.util.List;
import java.util.UUID;

public interface AnalyticsService {

    NextBestTimeResponseDto findNextBestTime(UUID socialAccountId);

    AnalysisJobDto getAnalysisJobStatus(UUID socialAccountId);

    List<OptimalTimeSlotDto> getAllTimeSlots(UUID socialAccountId);
}
