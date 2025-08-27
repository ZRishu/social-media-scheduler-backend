package com.fierceadventurer.postservice.service;

import com.fierceadventurer.postservice.dto.PostDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface PostService {
    PostDto createPost(PostDto postDto);
    PostDto updatePostById(UUID postId , PostDto postDto);
    PostDto getPostById(UUID postId);
    List<PostDto> getAllPosts();
    boolean deletePostById(UUID postId);



}
