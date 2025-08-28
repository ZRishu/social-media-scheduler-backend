package com.fierceadventurer.postservice.service;

import com.fierceadventurer.postservice.dto.PostVariantDto;

import java.util.List;
import java.util.UUID;

public interface PostVariantService {
    PostVariantDto createPostVariant(UUID postId , PostVariantDto postVariantDto);
    PostVariantDto getPostVariantById(UUID postId , UUID variantId);
    PostVariantDto updatePostVariant(UUID postId, UUID variantId , PostVariantDto postVariantDto);
    List<PostVariantDto> getAllPostVariants(UUID postId);
    void deletePostVariantById(UUID variantId);

}
