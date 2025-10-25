package com.fierceadventurer.postservice.controller;

import com.fierceadventurer.postservice.dto.CreatePostVariantRequestDto;
import com.fierceadventurer.postservice.dto.PostVariantResponseDto;
import com.fierceadventurer.postservice.dto.UpdatePostVariantRequestDto;
import com.fierceadventurer.postservice.service.PostVariantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            @Valid @RequestBody CreatePostVariantRequestDto createDto) {
        PostVariantResponseDto createdVariant = postVariantService.createNewVariant(postId, createDto);
        return new ResponseEntity<>(createdVariant, HttpStatus.CREATED);
    }

    @GetMapping("/{variantId}")
    public ResponseEntity<PostVariantResponseDto> getPostVariantById(
            @PathVariable UUID postId, @PathVariable UUID variantId) {
        PostVariantResponseDto variant = postVariantService.getPostVariantById(postId, variantId);
        return ResponseEntity.ok(variant);
    }

    @PutMapping("/{variantId}")
    public ResponseEntity<PostVariantResponseDto> updatePostVariant(
            @PathVariable UUID postId, @PathVariable UUID variantId, @Valid @RequestBody UpdatePostVariantRequestDto updateDto) {
        PostVariantResponseDto updatedPost = postVariantService.updateExistingVariant(postId , variantId,updateDto );
        return ResponseEntity.ok(updatedPost);
    }

    @GetMapping
    public ResponseEntity<List<PostVariantResponseDto>> getAllPostVariants(@PathVariable UUID postId) {
        List<PostVariantResponseDto> allVariants = postVariantService.getAllPostVariants(postId);
        return ResponseEntity.ok(allVariants);
    }

    @DeleteMapping("/{variantId}")
    public ResponseEntity<PostVariantResponseDto> deletePostVariantById(@PathVariable UUID postId, @PathVariable UUID variantId) {
        postVariantService.deletePostVariantById(variantId);
        return ResponseEntity.noContent().build();
    }
}
