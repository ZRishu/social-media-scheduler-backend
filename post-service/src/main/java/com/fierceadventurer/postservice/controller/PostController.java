package com.fierceadventurer.postservice.controller;

import com.fierceadventurer.postservice.dto.PostDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    @PostMapping("/create")
    public ResponseEntity<?> createPost(@RequestBody PostDto postDto) {
        return ResponseEntity.ok().body(postDto);
    }

    @GetMapping("/get/{postId}")
    public ResponseEntity<?> getByPostId(@PathVariable UUID postId) {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllPosts(){
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{postId}")
    public ResponseEntity<?> updatePostById(@PathVariable UUID postId) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePostById(@PathVariable UUID postId) {
        return ResponseEntity.noContent().build();
    }

}
