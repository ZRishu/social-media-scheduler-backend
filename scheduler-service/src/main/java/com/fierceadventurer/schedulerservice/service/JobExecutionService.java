package com.fierceadventurer.schedulerservice.service;

import com.fierceadventurer.schedulerservice.dto.ScheduledJobDto;
import com.fierceadventurer.schedulerservice.dto.UpdateJobRequestDto;
import com.fierceadventurer.schedulerservice.events.VariantReadyForSchedulingEvent;

import java.util.UUID;

public interface JobExecutionService {
    void findAndExecutionDueJobs();
    void publishNow(VariantReadyForSchedulingEvent event);
    void cancelJob(UUID jobId);
    ScheduledJobDto repostJob(UUID jobId);
    ScheduledJobDto updateJobSchedule(UUID jobId, UpdateJobRequestDto requestDto);
}
