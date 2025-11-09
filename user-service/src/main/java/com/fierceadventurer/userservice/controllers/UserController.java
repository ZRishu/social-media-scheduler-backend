package com.fierceadventurer.userservice.controllers;

import com.fierceadventurer.userservice.dto.UpdateUserRequestDto;
import com.fierceadventurer.userservice.dto.UserResponseDto;
import com.fierceadventurer.userservice.enums.UserStatus;
import com.fierceadventurer.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;



    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMyProfile(
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());
        UserResponseDto user = userService.getUserById(userId);
        return ResponseEntity.ok(user);

    }

    @PutMapping("/me")
    public ResponseEntity<UserResponseDto> updateMyProfile(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody UpdateUserRequestDto requestDto){

        UUID userId = UUID.fromString(jwt.getSubject());
        UserResponseDto updatedUser = userService.updateUser(userId, requestDto);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteMyAccount(
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(Pageable pageable,
                                                             @RequestParam(required = false)UserStatus status) {
        Page<UserResponseDto> users = userService.getAllUsers(pageable , status);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable UUID userId) {
        UserResponseDto user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/{userId}/suspend")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> suspendUser(@PathVariable UUID userId) {
        userService.suspendUser(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/unsuspend")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> unsuspendUser(@PathVariable UUID userId) {
        userService.activateUser(userId);
        return ResponseEntity.ok().build();
    }



}
