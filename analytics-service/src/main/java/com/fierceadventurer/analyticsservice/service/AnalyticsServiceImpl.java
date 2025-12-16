package com.fierceadventurer.analyticsservice.service;

import com.fierceadventurer.analyticsservice.dto.AnalysisJobDto;
import com.fierceadventurer.analyticsservice.dto.NextBestTimeResponseDto;
import com.fierceadventurer.analyticsservice.dto.OptimalTimeSlotDto;
import com.fierceadventurer.analyticsservice.entity.AnalysisJob;
import com.fierceadventurer.analyticsservice.entity.OptimalTimeSlot;
import com.fierceadventurer.analyticsservice.enums.Provider;
import com.fierceadventurer.analyticsservice.mapper.AnalyticsMapper;
import com.fierceadventurer.analyticsservice.repository.AnalysisJobRepository;
import com.fierceadventurer.analyticsservice.repository.OptimalTimeSlotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalyticsServiceImpl implements AnalyticsService {
    private final OptimalTimeSlotRepository optimalTimeSlotRepository;
    private final AnalysisJobRepository analysisJobRepository;
    private final AnalyticsMapper analyticsMapper;
    private static final int MINIMUM_SCHEDULING_DELAY_MINUTES = 30;


    @Override
    public NextBestTimeResponseDto findNextBestTime(UUID socialAccountId) {
        log.info("Calculating next best time for social account {}", socialAccountId);

        AnalysisJob job = analysisJobRepository.findBySocialAccountId(socialAccountId)
                .orElseThrow(()-> new RuntimeException("Analysis Job not found. Cannot determine provider."));
        List<OptimalTimeSlot> slots = optimalTimeSlotRepository.
                findBySocialAccountIdOrderByEngagementScoreDesc(socialAccountId);

        if(job.getProvider() == Provider.LINKEDIN){
            slots = filterForBusinessHours(slots);
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime earlistAllowedTime = now.plusMinutes(MINIMUM_SCHEDULING_DELAY_MINUTES);

        if(slots.isEmpty()) {
            log.warn("No optimal slot found for social account {}. Falling back ...", socialAccountId);
            return generateFallbackTime(job.getProvider(), now);
        }

        for (OptimalTimeSlot slot : slots) {
            LocalDateTime nextSlotDateTime = calculateNextOccurrence(now, slot.getDayOfWeek(), slot.getHourOfDay());

            if(nextSlotDateTime.isAfter(earlistAllowedTime)){
                log.info("Found best slot for account {}", socialAccountId, nextSlotDateTime);
                return new NextBestTimeResponseDto(nextSlotDateTime);
            }
        }

        OptimalTimeSlot bestSlot = slots.get(0);
        LocalDateTime nextWeekBestSlot = calculateNextOccurrence(now.plusWeeks(1)
                ,bestSlot.getDayOfWeek(), bestSlot.getHourOfDay());

        if(!nextWeekBestSlot.isAfter(earlistAllowedTime)){
            nextWeekBestSlot = nextWeekBestSlot.plusWeeks(1);
        }
        log.info("All optimal slots are within the buffer. Scheduling for next week at best slot: {}", nextWeekBestSlot);
        return new NextBestTimeResponseDto(nextWeekBestSlot);
    }

    private NextBestTimeResponseDto generateFallbackTime(Provider provider, LocalDateTime now) {
        if(provider == Provider.LINKEDIN){
            LocalDateTime candidate = now.withHour(10).withMinute(0).withSecond(0).withNano(0);

            if(now.getHour() >= 10) candidate = candidate.plusDays(1);

            while (candidate.getDayOfWeek() == DayOfWeek.SATURDAY || candidate.getDayOfWeek() == DayOfWeek.SUNDAY){
                candidate = candidate.plusDays(1);
            }
            return new NextBestTimeResponseDto(candidate);
        }

        return new NextBestTimeResponseDto(now.plusHours(1));
    }

    private List<OptimalTimeSlot> filterForBusinessHours(List<OptimalTimeSlot> slots) {
        return slots.stream()
                .filter(slot -> {
                    boolean isWeekday = slot.getDayOfWeek().getValue() <= 5;
                    boolean isBusinessHours = slot.getHourOfDay() >= 8 && slot.getHourOfDay() <= 18;
                    return isWeekday && isBusinessHours;
                })
                .collect(Collectors.toList());
    }

    private LocalDateTime calculateNextOccurrence(LocalDateTime now, DayOfWeek slotDay, int slotHour) {
        LocalDateTime slotTimeToday = now.toLocalDate().atTime(slotHour,0);

        if(now.getDayOfWeek().getValue() < slotDay.getValue()){
            return now.with(TemporalAdjusters.next(slotDay)).withHour(slotHour).withMinute(0).withSecond(0);
        }

        if(now.getDayOfWeek() == slotDay){
            if (slotTimeToday.isAfter(now)) {
                return slotTimeToday;
            }
        }

        return now.with(TemporalAdjusters.next(slotDay)).withHour(slotHour).withMinute(0).withSecond(0);
    }

    @Override
    public AnalysisJobDto getAnalysisJobStatus(UUID socialAccountId) {
        return analysisJobRepository.findBySocialAccountId(socialAccountId)
                .map(analyticsMapper::toDto)
                .orElseThrow(()-> new ResourceNotFoundException("Analysis Job Not Found For AccountId: " + socialAccountId));
    }

    @Override
    public List<OptimalTimeSlotDto> getAllTimeSlots(UUID socialAccountId) {
        List<OptimalTimeSlot> slots = optimalTimeSlotRepository.findBySocialAccountIdOrderByEngagementScoreDesc(socialAccountId);
        return analyticsMapper.toDtoList(slots);
    }
}
