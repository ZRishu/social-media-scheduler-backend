package com.fierceadventurer.socialaccountservice.controller;

import com.fierceadventurer.socialaccountservice.dto.CreateSocialAccountRequestDto;
import com.fierceadventurer.socialaccountservice.dto.SocialAccountResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class SocialAccountController {

    @PostMapping
    public ResponseEntity<SocialAccountResponseDto> createSocialAccount(
            @Valid @RequestBody CreateSocialAccountRequestDto requestDto){
        return ResponseEntity.ok(new SocialAccountResponseDto());
    }

    @GetMapping
    public ResponseEntity<Page<SocialAccountResponseDto>> getAllSocialAccountsForUser(
            @RequestParam UUID userId , Pageable pageable){
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<SocialAccountResponseDto> getAccountById(
            @PathVariable UUID accountId){
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{accountId")
    public ResponseEntity<Void> deleteAccount(@PathVariable UUID accountId){
        return ResponseEntity.noContent().build();
    }
}
