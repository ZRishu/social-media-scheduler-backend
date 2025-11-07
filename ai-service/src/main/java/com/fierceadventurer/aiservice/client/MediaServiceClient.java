package com.fierceadventurer.aiservice.client;

import org.apache.coyote.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "media-storge-service" , url = "${clients.media-service.url}")
public interface MediaServiceClient {

    @GetMapping("/api/v1/media/{id}")
    ResponseEntity<byte[]> downloadFile(@PathVariable("id") UUID id);
}
