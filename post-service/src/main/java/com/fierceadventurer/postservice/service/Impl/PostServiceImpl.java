package com.fierceadventurer.postservice.service.Impl;

import com.fierceadventurer.postservice.dto.PostRequestDto;
import com.fierceadventurer.postservice.dto.PostResponseDto;
import com.fierceadventurer.postservice.entity.Post;
import com.fierceadventurer.postservice.enums.PostStatus;
import com.fierceadventurer.postservice.exception.ResourceNotFoundException;
import com.fierceadventurer.postservice.mapper.PostRequestMapper;
import com.fierceadventurer.postservice.mapper.PostResponseMapper;
import com.fierceadventurer.postservice.repository.PostRepository;
import com.fierceadventurer.postservice.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
@Slf4j
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostRequestMapper postRequestMapper;
    private final PostResponseMapper postResponseMapper;

    @Override
    @Transactional
    public PostResponseDto createPost(PostRequestDto postRequestDto) {
        Post post = postRequestMapper.toEntity(postRequestDto);
        post.setDatePosted(LocalDateTime.now());
        post.setLastEdited(LocalDateTime.now());
        try {
            post.setStatus(PostStatus.PUBLISHED);
            Post savedPost = postRepository.save(post);
            log.debug("Created post {}", post);
            return postResponseMapper.toDto(savedPost);
        } catch (DataAccessException e) {
            log.error("Failed to create post {}" , e.getMessage());
            post.setStatus(PostStatus.FAILED);
            return postResponseMapper.toDto(post);
        }
    }

    @Override
    @Transactional
    public PostResponseDto updatePostById(UUID postId, PostRequestDto postRequestDto) {
        Post existingPost = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post not found with id: " + postId ));
        postRequestMapper.updatePost(postRequestDto, existingPost);
        existingPost.setLastEdited(LocalDateTime.now());
        try {
            existingPost.setStatus(PostStatus.UPDATED);
            Post updatedPost = postRepository.save(existingPost);
            log.debug("Updated post {}", existingPost);
            return postResponseMapper.toDto(updatedPost);
        } catch (DataAccessException e) {
            log.error("Failed to update post  with id: {}" , e.getMessage());
            existingPost.setStatus(PostStatus.FAILED);
            return postResponseMapper.toDto(existingPost);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PostResponseDto getPostById(UUID postId) {
        Post post = postRepository.findByIdAndStatusNot(postId , PostStatus.DELETED)
                .orElseThrow(()-> new ResourceNotFoundException(
                        "Post not found with id " + postId
                ));
        log.debug("Retrieved post by id {}", post);
        return postResponseMapper.toDto(post);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostResponseDto> getAllPosts(Pageable pageable) {

        return postRepository.findAllByStatusNot(PostStatus.DELETED ,pageable)
                .map(postResponseMapper::toDto);

    }

    @Override
    @Transactional
    public PostResponseDto deletePostById(UUID postId) {
        Post postTodelete = postRepository.findByIdAndStatusNot(postId , PostStatus.DELETED).orElseThrow(
                ()-> new ResourceNotFoundException("Post not found with id: " + postId)
        );
        postTodelete.setStatus(PostStatus.DELETED);
        Post deletedPost = postRepository.save(postTodelete);
        log.debug("Marked post as deleted with id {}", postId);
        return postResponseMapper.toDto(deletedPost);


    }
}
