package com.fierceadventurer.socialaccountservice.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class RateLimitQuotaDto {
    private UUID quotaId;
    private LocalDateTime windowStart;
    private LocalDateTime windowEnd;
    private int usedRequests;
    private int requestLimit;
}
