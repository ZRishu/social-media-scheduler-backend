package com.fierceadventurer.schedulerservice.dto;

import com.fierceadventurer.schedulerservice.entities.PublishAttempt;
import com.fierceadventurer.schedulerservice.enums.JobStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class ScheduledJobDto {
    private UUID jobId;
    private UUID postVariantId;
    private JobStatus status;
    private LocalDateTime scheduledAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int retryCount;
    private String lastError;
    private List<PublishAttemptDto> attempts;

}
