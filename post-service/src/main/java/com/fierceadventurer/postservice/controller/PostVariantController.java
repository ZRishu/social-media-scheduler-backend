package com.fierceadventurer.postservice.controller;

import com.fierceadventurer.postservice.dto.CreatePostVariantRequestDto;
import com.fierceadventurer.postservice.dto.PostVariantResponseDto;
import com.fierceadventurer.postservice.dto.UpdatePostVariantRequestDto;
import com.fierceadventurer.postservice.service.PostVariantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/posts/{postId}/variants")
@RequiredArgsConstructor
public class PostVariantController {

    private final PostVariantService postVariantService;


    @PostMapping
    public ResponseEntity<PostVariantResponseDto> createPostVariant(
            @PathVariable UUID postId ,
            @Valid @RequestBody CreatePostVariantRequestDto createDto,
            @AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());
        PostVariantResponseDto createdVariant = postVariantService.createNewVariant(postId,userId, createDto);
        return new ResponseEntity<>(createdVariant, HttpStatus.CREATED);
    }

    @GetMapping("/{variantId}")
    public ResponseEntity<PostVariantResponseDto> getPostVariantById(
            @PathVariable UUID postId, @PathVariable UUID variantId,
            @AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());
        PostVariantResponseDto variant = postVariantService.getPostVariantById(postId, userId, variantId);
        return ResponseEntity.ok(variant);
    }

    @PutMapping("/{variantId}")
    public ResponseEntity<PostVariantResponseDto> updatePostVariant(
            @PathVariable UUID postId, @PathVariable UUID variantId,
            @Valid @RequestBody UpdatePostVariantRequestDto updateDto,
            @AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());
        PostVariantResponseDto updatedPost = postVariantService.updateExistingVariant(
                postId ,userId, variantId,updateDto );
        return ResponseEntity.ok(updatedPost);
    }

    @GetMapping
    public ResponseEntity<List<PostVariantResponseDto>> getAllPostVariants(@PathVariable UUID postId,
    @AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());
        List<PostVariantResponseDto> allVariants = postVariantService.getAllPostVariants(postId , userId);
        return ResponseEntity.ok(allVariants);
    }

    @DeleteMapping("/{variantId}")
    public ResponseEntity<PostVariantResponseDto> deletePostVariantById(@PathVariable UUID postId, @PathVariable UUID variantId,
                                                                        @AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());
        postVariantService.deletePostVariantById(postId, userId, variantId);
        return ResponseEntity.noContent().build();
    }
}
