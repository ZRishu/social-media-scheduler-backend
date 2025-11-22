package com.fierceadventurer.postservice.controller;

import com.fierceadventurer.postservice.dto.PostRequestDto;
import com.fierceadventurer.postservice.dto.PostResponseDto;
import com.fierceadventurer.postservice.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@Valid @RequestBody PostRequestDto postRequestDto,
                                                      JwtAuthenticationToken token) {
        String userIdString = token.getTokenAttributes().get("sub").toString();
        UUID userId = UUID.fromString(userIdString);
        PostResponseDto createdPost = postService.createPost(postRequestDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(postRequestDto , userId));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDto> getByPostId(@PathVariable UUID postId) {
        PostResponseDto post = postService.getPostById(postId);
        return ResponseEntity.ok(post);
    }

    @GetMapping
    public ResponseEntity<Page<PostResponseDto>> getAllPosts(Pageable pageable) {
        Page<PostResponseDto> post = postService.getAllPosts(pageable);
        return ResponseEntity.ok(post);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponseDto> updatePostById(@PathVariable UUID postId , @Valid @RequestBody PostRequestDto postRequestDto) {
        PostResponseDto updatedPost = postService.updatePostById(postId , postRequestDto);
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePostById(@PathVariable UUID postId) {
        postService.deletePostById(postId);
        return ResponseEntity.noContent().build();
    }


}
