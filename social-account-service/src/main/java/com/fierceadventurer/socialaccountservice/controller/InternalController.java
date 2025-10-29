package com.fierceadventurer.socialaccountservice.controller;

import com.fierceadventurer.socialaccountservice.dto.TokenResponseDto;
import com.fierceadventurer.socialaccountservice.service.SocialAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/interal/accounts")
@RequiredArgsConstructor
public class InternalController {
    private final SocialAccountService socialAccountService;

    @GetMapping("/{accountId}/token")
    public ResponseEntity<TokenResponseDto> getAccessToken(@PathVariable UUID accountId) {
        String token = socialAccountService.getActiveAccessToken(accountId);
        return ResponseEntity.ok(new TokenResponseDto(token));
    }
}
