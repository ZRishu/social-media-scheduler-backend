package com.fierceadventurer.socialaccountservice.controller;

import com.fierceadventurer.socialaccountservice.dto.TokenResponseDto;
import com.fierceadventurer.socialaccountservice.entities.AuthToken;
import com.fierceadventurer.socialaccountservice.entities.SocialAccount;
import com.fierceadventurer.socialaccountservice.repository.SocialAccountRepository;
import com.fierceadventurer.socialaccountservice.service.SocialAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneOffset;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/interal/accounts")
@RequiredArgsConstructor
@Slf4j
public class InternalController {

    private final SocialAccountRepository socialAccountRepository;
    @GetMapping("/{accountId}/token")
    public ResponseEntity<TokenResponseDto> getAccessToken(@PathVariable UUID accountId) {
        log.info("Internal request: Fetching token for account {}", accountId);
        SocialAccount account = socialAccountRepository.findById(accountId)
                .orElseThrow(()-> new RuntimeException("Account not found: " + accountId));

        if(account.getAuthTokens() == null || account.getAuthTokens().isEmpty()){
            throw new RuntimeException("No auth tokens found for account: " + accountId);
        }

        AuthToken tokenEntity = account.getAuthTokens().get(0);

        long expiresAtEpoch = 0;
        if(tokenEntity.getExpiry() != null){
            expiresAtEpoch = tokenEntity.getExpiry().toEpochSecond(ZoneOffset.UTC);
        }

        TokenResponseDto response = TokenResponseDto.builder()
                .accessToken(tokenEntity.getAccessToken())
                .refreshToken(tokenEntity.getRefreshToken())
                .expiresAt(expiresAtEpoch)
                .build();

        return ResponseEntity.ok(response);
    }
}
