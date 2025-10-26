package com.fierceadventurer.analyticsservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnalysisJobDto {

    private String status;
    private String lastError;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
