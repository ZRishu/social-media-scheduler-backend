package com.fierceadventurer.socialaccountservice.controller;

import com.fierceadventurer.socialaccountservice.dto.RateLimitQuotaDto;
import com.fierceadventurer.socialaccountservice.service.AccountQueryService;
import com.fierceadventurer.socialaccountservice.service.RateLimitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accounts/{accountId}")
@RequiredArgsConstructor
public class RateLimitQuotaController {

    private final RateLimitService rateLimitService;
    private final AccountQueryService  accountQueryService;

    @GetMapping("/quota")
    public ResponseEntity<RateLimitQuotaDto> getQuotaForAccount(@PathVariable UUID accountId){
        RateLimitQuotaDto quota = accountQueryService.getAccountById(accountId).getRateLimitQuota();
        return ResponseEntity.ok(quota);
    }

    @PostMapping("/check")
    public ResponseEntity<Void> checkAndDecrementQuota(@PathVariable UUID accountId){
        rateLimitService.checkAndDecrementQuota(accountId);
        return ResponseEntity.ok().build();
    }
}
