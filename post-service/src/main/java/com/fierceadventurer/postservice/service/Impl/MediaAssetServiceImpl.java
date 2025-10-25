package com.fierceadventurer.postservice.service.Impl;

import com.fierceadventurer.postservice.dto.MediaAssetDto;
import com.fierceadventurer.postservice.entity.MediaAsset;
import com.fierceadventurer.postservice.entity.Post;
import com.fierceadventurer.postservice.mapper.MediaAssetMapper;
import com.fierceadventurer.postservice.repository.MediaAssetRepository;
import com.fierceadventurer.postservice.repository.PostRepository;
import com.fierceadventurer.postservice.service.MediaAssetService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MediaAssetServiceImpl implements MediaAssetService {

    private final PostRepository postRepository;
    private final MediaAssetRepository mediaAssetRepository;
    private final MediaAssetMapper mediaAssetMapper = MediaAssetMapper.Instance;

    @Override
    @Transactional
    public MediaAssetDto addMediaToPost(UUID postId, MediaAssetDto mediaAssetDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(
                        ()-> new ResourceNotFoundException("Cannot add media to non-existing post with id: " + postId)
                );
        MediaAsset mediaAsset = mediaAssetMapper.toEntity(mediaAssetDto);
        mediaAsset.setPost(post);

        MediaAsset savedMediaAsset = mediaAssetRepository.save(mediaAsset);
        return  mediaAssetMapper.toDto(savedMediaAsset);
    }

    @Override
    @Transactional(readOnly = true)
    public MediaAssetDto getMediaAssetById(UUID postId, UUID assetId) {
        MediaAsset mediaAsset = mediaAssetRepository.findById(assetId)
                .orElseThrow(()-> new ResourceNotFoundException("Cannot find media asset with id: " + assetId));
        if(!mediaAsset.getPost().getId().equals(postId)) {
            throw new ResourceNotFoundException("MediaAsset with id " + assetId + " does not belong to this post with id: " + postId);
        }
        return mediaAssetMapper.toDto(mediaAsset);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MediaAssetDto> getAllMediaForPost(UUID postId) {
        Post post  = postRepository.findById(postId)
                .orElseThrow(
                        ()-> new ResourceNotFoundException("Cannot find media asset with id: " + postId)
                );
        return post.getMediaAssets().stream()
                .map(mediaAssetMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteMediaAssetById(UUID postId, UUID assetId) {
        if(!mediaAssetRepository.existsById(assetId)){
            throw new ResourceNotFoundException("Cannot find media asset with id: " + assetId);
        }
        mediaAssetRepository.deleteById(assetId);
    }

    @Override
    @Transactional
    public void deleteAllMediaAssetsForPost(UUID postId) {
        Post post =  postRepository.findById(postId)
                .orElseThrow(
                        ()-> new ResourceNotFoundException("Cannot find media asset with id: " + postId)
                );
        mediaAssetRepository.deleteAll(post.getMediaAssets());
    }
}
