package com.fierceadventurer.postservice.controller;

import com.fierceadventurer.postservice.dto.PostVariantDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/posts/{postId}/variants")
public class PostVariantController {

    @PostMapping
    public ResponseEntity<?> createPostVariant(
            @PathVariable UUID postId , @RequestBody PostVariantDto postVariantDto) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{variantId}")
    public ResponseEntity<?> getPostVariantById(@PathVariable UUID postId, @PathVariable UUID variantId) {
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{variantId}")
    public ResponseEntity<?> updatePostVariant(@PathVariable UUID postId, @RequestBody PostVariantDto postVariantDto) {
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<?> getAllPostVariants(@PathVariable UUID postId) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{variantId}")
    public ResponseEntity<?> deletePostVariantById(@PathVariable UUID postId, @PathVariable UUID variantId) {
        return ResponseEntity.noContent().build();
    }
}
