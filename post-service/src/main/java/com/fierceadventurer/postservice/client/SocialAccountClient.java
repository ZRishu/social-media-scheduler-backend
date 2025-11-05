package com.fierceadventurer.postservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "social-account-service" , url = "${clients.social-account-service.url}")
public interface SocialAccountClient {

    @GetMapping("/api/v1/accounts/{accountId}")
    void validateSocialAccount(@PathVariable("accountId") UUID accountId);

    @GetMapping("/api/v1/accounts/{accountId}/validate-owner")
    void validateAccountOwnerShip(@PathVariable("accountId") UUID accountId,
                                  @RequestParam ("userId") UUID userId);
}
