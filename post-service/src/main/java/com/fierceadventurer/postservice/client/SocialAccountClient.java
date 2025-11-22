package com.fierceadventurer.postservice.client;

import com.fierceadventurer.postservice.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "social-account-service" ,
        url = "${clients.social-account-service.url}"
        , configuration = FeignConfig.class )
public interface SocialAccountClient {

    @GetMapping("/api/v1/accounts/{accountId}/validate-owner")
    void validateAccountOwnerShip(@PathVariable("accountId") UUID accountId,
                                  @RequestParam("userId") UUID userId);

    // Method 2: Used for Quota Management (I added this because your Service logic calls it)
    @GetMapping("/api/v1/accounts/{accountId}/decrement-quota")
    void checkAndDecrementQuota(@PathVariable("accountId") UUID accountId);

    // Method 3: General Validation (From your duplicate file)
    @GetMapping("/api/v1/accounts/{accountId}")
    void validateSocialAccount(@PathVariable("accountId") UUID accountId);
}
