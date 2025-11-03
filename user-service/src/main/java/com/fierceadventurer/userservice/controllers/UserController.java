package com.fierceadventurer.userservice.controllers;

import com.fierceadventurer.userservice.dto.UpdateUserRequestDto;
import com.fierceadventurer.userservice.dto.UserResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUserProfile(@PathVariable UUID userId) {
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseDto> updateUserProfile(
            @PathVariable UUID userId,
            @Valid @RequestBody UpdateUserRequestDto requestDto) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMyProfile() {
        return ResponseEntity.ok().build();
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponseDto> updateMyProfile(
            @Valid @RequestBody UpdateUserRequestDto requestDto){
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMyAccount() {
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUserByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/suspend")
    public ResponseEntity<Void> suspendUser(@PathVariable UUID userId) {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/unsuspend")
    public ResponseEntity<Void> unsuspendUser(@PathVariable UUID userId) {
        return ResponseEntity.ok().build();
    }



}
