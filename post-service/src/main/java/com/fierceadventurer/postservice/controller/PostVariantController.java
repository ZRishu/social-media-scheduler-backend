package com.fierceadventurer.postservice.controller;

import com.fierceadventurer.postservice.dto.PostVariantDto;
import com.fierceadventurer.postservice.service.PostService;
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
    public ResponseEntity<PostVariantDto> createPostVariant(
            @PathVariable UUID postId ,@Valid @RequestBody PostVariantDto postVariantDto) {
        PostVariantDto createdVariant = postVariantService.createPostVariant(postId, postVariantDto);
        return new ResponseEntity<>(createdVariant, HttpStatus.CREATED);
    }

    @GetMapping("/{variantId}")
    public ResponseEntity<PostVariantDto> getPostVariantById(@PathVariable UUID postId, @PathVariable UUID variantId) {
        PostVariantDto variant = postVariantService.getPostVariantById(postId, variantId);
        return ResponseEntity.ok(variant);
    }

    @PutMapping("/{variantId}")
    public ResponseEntity<PostVariantDto> updatePostVariant(@PathVariable UUID postId,@PathVariable UUID variantId,@Valid @RequestBody PostVariantDto postVariantDto) {
        PostVariantDto updatedPost = postVariantService.updatePostVariant(postId , variantId, postVariantDto );
        return ResponseEntity.ok(updatedPost);
    }

    @GetMapping
    public ResponseEntity<List<PostVariantDto>> getAllPostVariants(@PathVariable UUID postId) {
        List<PostVariantDto> allVariants = postVariantService.getAllPostVariants(postId);
        return ResponseEntity.ok(allVariants);
    }

    @DeleteMapping("/{variantId}")
    public ResponseEntity<PostVariantDto> deletePostVariantById(@PathVariable UUID postId, @PathVariable UUID variantId) {
        postVariantService.deletePostVariantById(variantId);
        return ResponseEntity.noContent().build();
    }
}
