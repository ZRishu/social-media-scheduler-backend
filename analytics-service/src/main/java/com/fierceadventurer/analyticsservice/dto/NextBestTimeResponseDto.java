package com.fierceadventurer.analyticsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NextBestTimeResponseDto {
    private LocalDateTime nextBestTime;
}
