package com.fierceadventurer.postservice.service;

import com.fierceadventurer.postservice.dto.CreatePostVariantRequestDto;
import com.fierceadventurer.postservice.dto.PostVariantResponseDto;
import com.fierceadventurer.postservice.dto.UpdatePostVariantRequestDto;

import java.util.List;
import java.util.UUID;

public interface PostVariantService {
    PostVariantResponseDto createNewVariant(UUID postId , CreatePostVariantRequestDto createDto);
    PostVariantResponseDto getPostVariantById(UUID postId , UUID variantId);
    PostVariantResponseDto updateExistingVariant(UUID postId, UUID variantId , UpdatePostVariantRequestDto updateDto);
    List<PostVariantResponseDto> getAllPostVariants(UUID postId);
    void deletePostVariantById(UUID variantId);

}
