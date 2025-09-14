package com.fierceadventurer.socialaccountservice.controller;

import com.fierceadventurer.socialaccountservice.dto.RateLimitQuotaDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/vq/accounts/{accountId}/quota")
@RequiredArgsConstructor
public class RateLimitQuotaController {

    @GetMapping
    public ResponseEntity<RateLimitQuotaDto> getQuotaForAccount(@PathVariable UUID accountId){
        return ResponseEntity.ok().build();
    }

    @PostMapping("/check")
    public ResponseEntity<Void> checkAndDecrementQuota(@PathVariable UUID accountId){
        return ResponseEntity.ok().build();
    }
}
