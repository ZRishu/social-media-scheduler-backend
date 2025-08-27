package com.fierceadventurer.postservice.service;

import com.fierceadventurer.postservice.dto.PostDto;
import com.fierceadventurer.postservice.entity.Post;
import com.fierceadventurer.postservice.mapper.PostMapper;
import com.fierceadventurer.postservice.repository.PostRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper = PostMapper.Instance;

    @Override
    @Transactional
    public PostDto createPost(PostDto postDto) {
        Post post = postMapper.toEntity(postDto);
        Post savedPost = postRepository.save(post);
        log.debug("Created post {}", post);
        return postMapper.toDto(savedPost);

    }

    @Override
    @Transactional
    public PostDto updatePostById(UUID postId, PostDto postDto) {
        Post existingPost = postRepository.findById(postId).orElseThrow(
                () -> new ResourceNotFoundException("Post not found with id: " + postId ));
        existingPost.setTitle(postDto.getTitle());
        existingPost.setContent(postDto.getContent());
        existingPost.setStatus(postDto.getStatus());
        Post updatedPost = postRepository.save(existingPost);
        return postMapper.toDto(updatedPost);
    }

    @Override
    @Transactional(readOnly = true)
    public PostDto getPostById(UUID postId) {
        Post post = postRepository.findById(postId).
                orElseThrow(()-> new ResourceNotFoundException(
                        "Post not found with id " + postId
                ));
        return postMapper.toDto(post);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostDto> getAllPosts() {

        return postRepository.findAll()
                .stream().map(postMapper::toDto)
                .collect(Collectors.toList());

    }

    @Override
    @Transactional
    public boolean deletePostById(UUID postId) {
        if (!postRepository.existsById(postId)) {
            throw new ResourceNotFoundException("Post not found with id: " + postId);
        }
        postRepository.deleteById(postId);
        return true;

    }
}
