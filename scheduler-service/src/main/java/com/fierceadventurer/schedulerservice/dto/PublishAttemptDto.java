package com.fierceadventurer.schedulerservice.dto;

import com.fierceadventurer.schedulerservice.enums.AttemptStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PublishAttemptDto {
    private UUID attemptId;
    private AttemptStatus status;
    private String details;
    private String providerResponse;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
}
