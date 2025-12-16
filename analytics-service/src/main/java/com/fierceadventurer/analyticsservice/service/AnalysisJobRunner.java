package com.fierceadventurer.analyticsservice.service;

import com.fierceadventurer.analyticsservice.client.ExternalPlatformClient;
import com.fierceadventurer.analyticsservice.client.SocialAccountClient;
import com.fierceadventurer.analyticsservice.client.SocialMediaClientFactory;
import com.fierceadventurer.analyticsservice.dto.HistoricalPost;
import com.fierceadventurer.analyticsservice.entity.AnalysisJob;
import com.fierceadventurer.analyticsservice.entity.OptimalTimeSlot;
import com.fierceadventurer.analyticsservice.enums.AnalysisStatus;
import com.fierceadventurer.analyticsservice.enums.Provider;
import com.fierceadventurer.analyticsservice.repository.AnalysisJobRepository;
import com.fierceadventurer.analyticsservice.repository.OptimalTimeSlotRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnalysisJobRunner {
    private final AnalysisJobRepository analysisJobRepository;
    private final OptimalTimeSlotRepository  optimalTimeSlotRepository;
    private final SocialAccountClient socialAccountClient;
    private final SocialMediaClientFactory clientFactory;

    @Scheduled(fixedDelay = 5 , timeUnit = TimeUnit.MINUTES)
    public void findAndProcessPendingJobs() {
        log.info("Running scheduled job runner....");
        List<AnalysisJob> pendingJobs = analysisJobRepository.findTopByStatusOrderByCreatedAtAsc(AnalysisStatus.PENDING);
        if(pendingJobs.isEmpty()) {
            log.info("No pending analysis jobs found.");
            return;
        }

        AnalysisJob job = pendingJobs.get(0);
        job.setStatus(AnalysisStatus.FETCHING_DATA);
        analysisJobRepository.save(job);

        try{
            performAnalysis(job);

            job.setStatus(AnalysisStatus.COMPLETED);
            analysisJobRepository.save(job);
            log.info("Successfully completed analysis for job: {}" , job.getJobId());
        }
        catch (Exception e){
            log.error("Failed to process analysis job {}: {}" , job.getJobId(), e.getMessage());
            job.setStatus(AnalysisStatus.FAILED);
            String errorMessage = e.getMessage() != null ? e.getMessage() : "Unknown Error";
            job.setLastError(errorMessage.substring(0 , Math.min(errorMessage.length() , 255)));
            analysisJobRepository.save(job);
        }
    }

    public void performAnalysis(AnalysisJob job) throws Exception{
        log.info("Starting analysis for job: {}" , job.getJobId());

        String accessToken = socialAccountClient.getAccessToken(job.getSocialAccountId()).getAccessToken();

        Provider provider = job.getProvider();
        ExternalPlatformClient apiClient = clientFactory.getClient(provider);

        List<HistoricalPost> posts = apiClient.getHistoricalData(accessToken);

        if(posts.isEmpty()) {
            log.warn("No historical posts found for account {}. Skipping analysis." , job.getSocialAccountId());
            return;
        }

        job.setStatus(AnalysisStatus.ANALYZING);
        analysisJobRepository.save(job);

        List<OptimalTimeSlot> newTimeSlots = calulateScores(job.getSocialAccountId() , posts);

        optimalTimeSlotRepository.saveAll(newTimeSlots);
    }

    private List<OptimalTimeSlot> calulateScores(UUID socialAccountId, List<HistoricalPost> posts){

        Map<DayOfWeek , Map<Integer, List<Integer>>> engagementBySlot = new EnumMap<>(DayOfWeek.class);

        for(HistoricalPost post : posts){
            DayOfWeek day = post.getCreatedAt().getDayOfWeek();
            int hour = post.getCreatedAt().getHour();

            engagementBySlot
                    .computeIfAbsent(day, k -> new HashMap<>())
                    .computeIfAbsent(hour, k -> new ArrayList<>())
                    .add(post.getEngagementCount());

        }

        Map<OptimalTimeSlot, Double> slotAverages = new HashMap<>();
        double maxAverage = 0.0;

        for(Map.Entry<DayOfWeek , Map<Integer, List<Integer>>> dayEntry : engagementBySlot.entrySet()){
            for(Map.Entry<Integer, List<Integer>> hourEntry : dayEntry.getValue().entrySet()){

                double average = hourEntry.getValue().stream()
                        .mapToInt(Integer::intValue)
                        .average().orElse(0.0);

                if(average > maxAverage){
                    maxAverage = average;
                }

                OptimalTimeSlot slot = new OptimalTimeSlot();
                slot.setSocialAccountId(socialAccountId);
                slot.setDayOfWeek(dayEntry.getKey());
                slot.setHourOfDay(hourEntry.getKey());
                slotAverages.put(slot, average);
            }
        }

        final double finalMaxAverage = maxAverage;
        return slotAverages.entrySet().stream()
                .map(entry -> {
                    OptimalTimeSlot slot = entry.getKey();
                    double normalizedScore = (finalMaxAverage > 0 ) ? (entry.getValue() /  finalMaxAverage) : 0.0 ;
                    slot.setEngagementScore(normalizedScore);
                    return slot;
                })
                .collect(Collectors.toList());

    }

    @Transactional
    protected void saveOptimalSlots(UUID socialAccountId, List<OptimalTimeSlot> newTimeSlots) {
        optimalTimeSlotRepository.deleteAllBySocialAccountId(socialAccountId);
        optimalTimeSlotRepository.saveAll(newTimeSlots);
    }
}
