package com.fierceadventurer.socialaccountservice.controller;

import com.fierceadventurer.socialaccountservice.dto.CreateSocialAccountRequestDto;
import com.fierceadventurer.socialaccountservice.dto.PublishRequestDto;
import com.fierceadventurer.socialaccountservice.dto.SocialAccountResponseDto;
import com.fierceadventurer.socialaccountservice.repository.SocialAccountRepository;
import com.fierceadventurer.socialaccountservice.service.AccountQueryService;
import com.fierceadventurer.socialaccountservice.service.SocialAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class SocialAccountController {

    private final SocialAccountService socialAccountService;
    private final AccountQueryService  accountQueryService;

    @PostMapping
    public ResponseEntity<SocialAccountResponseDto> createSocialAccount(
            @Valid @RequestBody CreateSocialAccountRequestDto requestDto,
            @AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());
        SocialAccountResponseDto createAccount = socialAccountService.createSocialAccount(userId ,requestDto);
        return new ResponseEntity<>(createAccount ,HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<SocialAccountResponseDto>> getAllSocialAccountsForUser(
            @RequestParam UUID userId , Pageable pageable){
        Page<SocialAccountResponseDto> accounts = accountQueryService.getAccountsByUserId(userId, pageable);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<SocialAccountResponseDto> getAccountById(
            @PathVariable UUID accountId){
        SocialAccountResponseDto account = accountQueryService.getAccountById(accountId);
        return ResponseEntity.ok(account);
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable UUID accountId){
        socialAccountService.deleteSocialAccount(accountId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{accountId}/suspend")

    public ResponseEntity<Void> suspendAccount(@PathVariable UUID accountId , String reason){
        socialAccountService.suspendAccount(accountId , reason);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{accountId}/unsuspend")
    public ResponseEntity<Void> unsuspendAccount(@PathVariable UUID accountId){
        socialAccountService.unsuspendAccount(accountId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{accountId}/validate-owner")
    public ResponseEntity<SocialAccountResponseDto> validateOwner(
            @PathVariable UUID accountId,
            @AuthenticationPrincipal Jwt jwt
    ){
        UUID userId = UUID.fromString(jwt.getSubject());
        socialAccountService.validateAccountOwnership(accountId , userId);
        SocialAccountResponseDto account = accountQueryService.getAccountById(accountId);
        return ResponseEntity.ok(account);
    }

    @PostMapping("/{accountId}/publish")
    public ResponseEntity<String> publishPost(@PathVariable UUID accountId, @RequestBody PublishRequestDto requestDto) {
        String postId = socialAccountService.publishContent(accountId, requestDto);
        return ResponseEntity.ok(postId);
    }
}
