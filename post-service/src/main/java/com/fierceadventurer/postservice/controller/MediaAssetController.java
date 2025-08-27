package com.fierceadventurer.postservice.controller;

import com.fierceadventurer.postservice.dto.MediaAssetDto;
import com.fierceadventurer.postservice.entity.MediaAsset;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/posts/{postId}/media")
public class MediaAssetController {

    @PostMapping
    public ResponseEntity<?> addMediaAssetToPost(
            @PathVariable UUID postId, @RequestBody MediaAssetDto mediaAssetDto){
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{assetId}")
    public ResponseEntity<?> getMediaAssetById(@PathVariable UUID postId, @PathVariable UUID assetId) {
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<?> getAllMediaAssetsForPost(@PathVariable UUID postId) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{assetId}")
    public ResponseEntity<?> deleteMediaAssetFromPostById(@PathVariable UUID postId, @PathVariable UUID assetId) {
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("media")
    public ResponseEntity<?> deleteAllMediaAssets(@PathVariable UUID postId) {
        return ResponseEntity.noContent().build();
    }
}
