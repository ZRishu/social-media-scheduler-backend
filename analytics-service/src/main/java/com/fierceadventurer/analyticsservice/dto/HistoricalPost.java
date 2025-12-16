package com.fierceadventurer.analyticsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HistoricalPost {
    private LocalDateTime createdAt;
    private int engagementCount;
}
