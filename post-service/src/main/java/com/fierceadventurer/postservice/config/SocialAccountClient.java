package com.fierceadventurer.postservice.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(
        name = "social-account-service",
        url = "${clients.social-account-service.url}",
        configuration = FeignConfig.class
)
public interface SocialAccountClient {

    @GetMapping("/api/v1/accounts/{accountId}/validate-owner")
    void validateAccountOwnerShip(@PathVariable("accountId") UUID accountId, @RequestParam("userId") UUID userId);

    @GetMapping("/api/v1/accounts/{accountId}/decrement-quota")
    void checkAndDecrementQuota(@PathVariable("accountId") UUID accountId);
}
