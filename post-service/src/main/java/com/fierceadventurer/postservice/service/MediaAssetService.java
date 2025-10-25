package com.fierceadventurer.postservice.service;

import com.fierceadventurer.postservice.dto.MediaAssetDto;

import java.util.List;
import java.util.UUID;

public interface MediaAssetService {
    MediaAssetDto addMediaToPost(UUID postId, MediaAssetDto mediaAssetDto);
    MediaAssetDto getMediaAssetById(UUID postId, UUID assetId);
    List<MediaAssetDto> getAllMediaForPost(UUID postId);
    void deleteMediaAssetById(UUID postId, UUID assetId);
    void deleteAllMediaAssetsForPost(UUID postId);
}
