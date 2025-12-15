package com.fierceadventurer.analyticsservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostInsightsDto {
    private String externalPostId;
    private LocalDateTime postedAt;
    private int likes;
    private int comments;
    private int shares;
    private int impressions;
}
