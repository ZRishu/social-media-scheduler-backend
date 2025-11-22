package com.fierceadventurer.schedulerservice.client;
import com.fierceadventurer.schedulerservice.config.SchedulerFeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;

import com.fierceadventurer.schedulerservice.dto.PublishRequestDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "social-account-service" , url = "${clients.social-account-service.url}",
    configuration = SchedulerFeignConfiguration.class)
public interface SocialAccountClient {

    @PostMapping("/api/v1/accounts/{accountId}/check")
    void checkAndDecrementQuota(@PathVariable("accountId") UUID accountId);

    @PostMapping(value = "/api/v1/accounts/{accountId}/publish",  consumes = MediaType.APPLICATION_JSON_VALUE)
    String publishPost(@PathVariable("accountId") UUID accountId, @RequestBody PublishRequestDto publishRequestDto);

}

