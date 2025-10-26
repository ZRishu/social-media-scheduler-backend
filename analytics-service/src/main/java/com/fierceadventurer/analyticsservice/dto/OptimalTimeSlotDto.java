package com.fierceadventurer.analyticsservice.dto;

import lombok.Data;

import java.time.DayOfWeek;

@Data
public class OptimalTimeSlotDto {
    private DayOfWeek dayOfWeek;
    private int hourOfDay;
    private Double engagementScore;
}
