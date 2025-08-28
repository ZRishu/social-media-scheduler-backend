package com.fierceadventurer.postservice.controller;

import com.fierceadventurer.postservice.dto.PostDto;
import com.fierceadventurer.postservice.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostDto> createPost(@Valid @RequestBody PostDto postDto) {
        PostDto createdPost = postService.createPost(postDto);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getByPostId(@PathVariable UUID postId) {
        PostDto post = postService.getPostById(postId);
        return ResponseEntity.ok(post);
    }

    @GetMapping
    public ResponseEntity<Page<PostDto>> getAllPosts(Pageable pageable) {
        Page<PostDto> post = postService.getAllPosts(pageable);
        return ResponseEntity.ok(post);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostDto> updatePostById(@PathVariable UUID postId ,@Valid @RequestBody PostDto postDto) {
        PostDto updatedPost = postService.updatePostById(postId , postDto);
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<PostDto> deletePostById(@PathVariable UUID postId) {
        postService.deletePostById(postId);
        return ResponseEntity.noContent().build();
    }

}
