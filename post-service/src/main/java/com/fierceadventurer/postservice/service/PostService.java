package com.fierceadventurer.postservice.service;

import com.fierceadventurer.postservice.dto.PostDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


public interface PostService {
    PostDto createPost(PostDto postDto);
    PostDto updatePostById(UUID postId , PostDto postDto);
    PostDto getPostById(UUID postId);
    Page<PostDto> getAllPosts(Pageable pageable);
    void deletePostById(UUID postId);



}
