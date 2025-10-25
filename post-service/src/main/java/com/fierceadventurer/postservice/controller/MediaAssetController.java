package com.fierceadventurer.postservice.controller;

import com.fierceadventurer.postservice.dto.MediaAssetDto;
import com.fierceadventurer.postservice.entity.MediaAsset;
import com.fierceadventurer.postservice.service.MediaAssetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/posts/{postId}/media")
@RequiredArgsConstructor
public class MediaAssetController {

    private final MediaAssetService mediaAssetService;

    @PostMapping
    public ResponseEntity<MediaAssetDto> addMediaAssetToPost(
            @PathVariable UUID postId,@Valid @RequestBody MediaAssetDto mediaAssetDto){
        MediaAssetDto addMediaAsset = mediaAssetService.addMediaToPost(postId, mediaAssetDto);
        return new ResponseEntity<>(addMediaAsset, HttpStatus.CREATED);
    }

    @GetMapping("/{assetId}")
    public ResponseEntity<MediaAssetDto> getMediaAssetById(@PathVariable UUID postId, @PathVariable UUID assetId) {
        MediaAssetDto mediaAsset = mediaAssetService.getMediaAssetById(postId, assetId);
        return ResponseEntity.ok(mediaAsset);
    }

    @GetMapping
    public ResponseEntity<List<MediaAssetDto>> getAllMediaAssetsForPost(@PathVariable UUID postId) {
        List<MediaAssetDto> mediaAssets = mediaAssetService.getAllMediaForPost(postId);
        return ResponseEntity.ok(mediaAssets);
    }

    @DeleteMapping("/{assetId}")
    public ResponseEntity<Void> deleteMediaAssetFromPostById(@PathVariable UUID postId, @PathVariable UUID assetId) {
        mediaAssetService.deleteMediaAssetById(postId, assetId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllMediaAssets(@PathVariable UUID postId) {
        mediaAssetService.deleteAllMediaAssetsForPost(postId);
        return ResponseEntity.noContent().build();
    }
}
