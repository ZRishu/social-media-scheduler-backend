package com.fierceadventurer.analyticsservice;

import com.fierceadventurer.analyticsservice.dto.NextBestTimeResponseDto;
import com.fierceadventurer.analyticsservice.entity.AnalysisJob;
import com.fierceadventurer.analyticsservice.entity.OptimalTimeSlot;
import com.fierceadventurer.analyticsservice.enums.AnalysisStatus;
import com.fierceadventurer.analyticsservice.enums.Provider;
import com.fierceadventurer.analyticsservice.repository.AnalysisJobRepository;
import com.fierceadventurer.analyticsservice.repository.OptimalTimeSlotRepository;
import com.fierceadventurer.analyticsservice.service.AnalysisJobRunner;
import com.fierceadventurer.analyticsservice.service.AnalyticsServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceImplTest {

    @Mock
    private OptimalTimeSlotRepository optimalTimeSlotRepository;

    @Mock
    private AnalysisJobRepository analysisJobRepository;

    @Mock
    private AnalysisJobRunner analysisJobRunner;

    @InjectMocks
    private AnalyticsServiceImpl analyticsService;

    @Test
    void findNextBestTime_ShouldTriggerImmediateAnalysis_IfSlotsEmpty() throws Exception {
        UUID accountId = UUID.randomUUID();

        AnalysisJob mockJob = new AnalysisJob();
        mockJob.setJobId(UUID.randomUUID());
        mockJob.setSocialAccountId(accountId);
        mockJob.setProvider(Provider.LINKEDIN);
        mockJob.setStatus(AnalysisStatus.PENDING);

        when(analysisJobRepository.findBySocialAccountId(accountId)).thenReturn(Optional.of(mockJob));

        when(optimalTimeSlotRepository.findBySocialAccountIdOrderByEngagementScoreDesc(accountId))
                .thenReturn(Collections.emptyList())
                .thenReturn(List.of(createSlot(accountId, DayOfWeek.MONDAY, 10, 1.0)));

        NextBestTimeResponseDto result = analyticsService.findNextBestTime(accountId);

        verify(analysisJobRunner, times(1)).performAnalysis(mockJob);

        Assertions.assertEquals(DayOfWeek.MONDAY, result.getNextBestTime().getDayOfWeek());
        Assertions.assertEquals(10, result.getNextBestTime().getHour());
    }

    @Test
    void findNextBestTime_LinkedIn_ShouldFilterOutWeekends() {
        UUID accountId = UUID.randomUUID();
        AnalysisJob mockJob = new AnalysisJob();
        mockJob.setProvider(Provider.LINKEDIN);
        mockJob.setStatus(AnalysisStatus.COMPLETED);

        when(analysisJobRepository.findBySocialAccountId(accountId)).thenReturn(Optional.of(mockJob));

        OptimalTimeSlot saturdaySlot = createSlot(accountId, DayOfWeek.SATURDAY, 14, 1.0);
        OptimalTimeSlot tuesdaySlot = createSlot(accountId, DayOfWeek.TUESDAY, 10, 0.8);

        when(optimalTimeSlotRepository.findBySocialAccountIdOrderByEngagementScoreDesc(accountId))
                .thenReturn(Arrays.asList(saturdaySlot, tuesdaySlot));

        NextBestTimeResponseDto result = analyticsService.findNextBestTime(accountId);

        Assertions.assertEquals(DayOfWeek.TUESDAY, result.getNextBestTime().getDayOfWeek());
    }

    @Test
    void findNextBestTime_ShouldSelfRepair_IfJobMissing() {

        UUID accountId = UUID.randomUUID();

        when(analysisJobRepository.findBySocialAccountId(accountId)).thenReturn(Optional.empty());

        AnalysisJob newJob = new AnalysisJob();
        newJob.setProvider(Provider.LINKEDIN);
        newJob.setStatus(AnalysisStatus.PENDING);
        when(analysisJobRepository.save(any(AnalysisJob.class))).thenReturn(newJob);

        analyticsService.findNextBestTime(accountId);

        verify(analysisJobRepository, times(1)).save(any(AnalysisJob.class));
    }

    private OptimalTimeSlot createSlot(UUID accountId, DayOfWeek day, int hour, double score) {
        OptimalTimeSlot slot = new OptimalTimeSlot();
        slot.setSocialAccountId(accountId);
        slot.setDayOfWeek(day);
        slot.setHourOfDay(hour);
        slot.setEngagementScore(score);
        return slot;
    }
}