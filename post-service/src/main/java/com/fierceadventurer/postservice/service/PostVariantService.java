package com.fierceadventurer.postservice.service;

import com.fierceadventurer.postservice.dto.CreatePostVariantRequestDto;
import com.fierceadventurer.postservice.dto.PostVariantResponseDto;
import com.fierceadventurer.postservice.dto.UpdatePostVariantRequestDto;

import java.util.List;
import java.util.UUID;

public interface PostVariantService {
    PostVariantResponseDto createNewVariant(UUID postId ,UUID userId, CreatePostVariantRequestDto createDto);
    PostVariantResponseDto getPostVariantById(UUID postId ,UUID userId, UUID variantId);
    PostVariantResponseDto updateExistingVariant(UUID postId, UUID userId ,UUID variantId , UpdatePostVariantRequestDto updateDto);
    List<PostVariantResponseDto> getAllPostVariants(UUID postId, UUID userId);
    void deletePostVariantById(UUID postId , UUID userId, UUID variantId);

}
