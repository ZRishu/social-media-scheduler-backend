package com.fierceadventurer.analyticsservice.client;

import com.fierceadventurer.analyticsservice.dto.TokenResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "social-account-service", url = "${clients.social-account-service.url}")
public interface SocialAccountClient {
    @GetMapping("/api/v1/internal/accounts/{accountId}/token")
    TokenResponseDto getAccessToken(@PathVariable("accountId") UUID accountId);
}
