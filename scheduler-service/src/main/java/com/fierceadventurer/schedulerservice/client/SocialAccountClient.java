package com.fierceadventurer.schedulerservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@FeignClient(name = "social-account-service" , url = "${clients.social-account-service.url}")
public interface SocialAccountClient {

    @PostMapping("/api/v1/accounts/{accountId}/check")
    void checkAndDecrementQuota(@PathVariable("accountId") UUID accountId);
}
