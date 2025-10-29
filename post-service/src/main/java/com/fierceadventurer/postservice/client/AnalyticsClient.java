package com.fierceadventurer.postservice.client;

import com.fierceadventurer.postservice.dto.NextBestTimeResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "analytics-service", url = "${clients.analytics-service.url}")
public interface AnalyticsClient {
    @GetMapping("/api/v1/analytics.{socialAccountId}/next-best-time")
    NextBestTimeResponseDto getNextBestTime(@PathVariable("socialAccountId") UUID socialAccountId);
}
