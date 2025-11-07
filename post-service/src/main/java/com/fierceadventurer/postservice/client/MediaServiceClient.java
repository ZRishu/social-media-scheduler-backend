package com.fierceadventurer.postservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "media-storage-service", url = "${clients.media-service.url}")
public interface MediaServiceClient {

    @GetMapping("/api/v1/media/{id}")
    void checkMediaExists(@PathVariable("id") UUID id);
}
