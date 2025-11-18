package com.fierceadventurer.postservice.service;

import com.fierceadventurer.postservice.dto.PostRequestDto;

import com.fierceadventurer.postservice.dto.PostResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;


public interface PostService {
    PostResponseDto createPost(PostRequestDto postRequestDto, UUID userId);
    PostResponseDto updatePostById(UUID postId , PostRequestDto postRequestDto);
    PostResponseDto getPostById(UUID postId);
    Page<PostResponseDto> getAllPosts(Pageable pageable);
    void deletePostById(UUID postId);
}
