package com.fierceadventurer.analyticsservice.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AnalysisJobDto {
    private String status;
    private String lastError;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
