package com.fierceadventurer.userservice.controllers;

import com.fierceadventurer.userservice.dto.UpdateUserRequestDto;
import com.fierceadventurer.userservice.dto.UserResponseDto;
import com.fierceadventurer.userservice.entity.User;
import com.fierceadventurer.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

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
        UserResponseDto user = userService.getUserById(UUID.randomUUID());
        return ResponseEntity.ok(user);

    }

    @PutMapping("/me")
    public ResponseEntity<UserResponseDto> updateMyProfile(
            @Valid @RequestBody UpdateUserRequestDto requestDto){
        UserResponseDto updatedUser = userService.updateUser(UUID.randomUUID(), requestDto);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMyAccount() {
        userService.deleteUser(UUID.randomUUID());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(Pageable pageable) {
        Page<UserResponseDto> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUserByUserId(@PathVariable UUID userId) {
        UserResponseDto user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/{userId}/suspend")
    public ResponseEntity<Void> suspendUser(@PathVariable UUID userId) {
        userService.suspendUser(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/unsuspend")
    public ResponseEntity<Void> unsuspendUser(@PathVariable UUID userId) {
        userService.activateUser(userId);
        return ResponseEntity.ok().build();
    }



}
