package com.fierceadventurer.postservice.service.Impl;

import com.fierceadventurer.postservice.dto.PostVariantDto;
import com.fierceadventurer.postservice.entity.MediaAsset;
import com.fierceadventurer.postservice.entity.Post;
import com.fierceadventurer.postservice.entity.PostVariant;
import com.fierceadventurer.postservice.mapper.PostVariantMapper;
import com.fierceadventurer.postservice.repository.MediaAssetRepository;
import com.fierceadventurer.postservice.repository.PostRepository;
import com.fierceadventurer.postservice.repository.PostVariantRepository;
import com.fierceadventurer.postservice.service.PostVariantService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostVariantServiceImpl implements PostVariantService {
    private final PostVariantRepository postVariantRepository;
    private final PostRepository postRepository;
    private final PostVariantMapper variantMapper = PostVariantMapper.Instance;
    private final MediaAssetRepository mediaAssetRepository;

    @Override
    @Transactional
    public PostVariantDto createPostVariant(UUID postId, PostVariantDto postVariantDto) {
       Post post = postRepository.findById(postId).orElseThrow(
               ()-> new ResourceNotFoundException("Cannot create a variant for non existing post with id: " + postId));
        PostVariant variant = variantMapper.toEntity(postVariantDto);
        variant.setPost(post);

        if(postVariantDto.getMediaAssetIds() != null
                && !postVariantDto.getMediaAssetIds().isEmpty()) {
            List<MediaAsset> mediaAssetToLink = mediaAssetRepository.findAllById(postVariantDto
                    .getMediaAssetIds());
            variant.setMediaAssets(mediaAssetToLink);
        }

        PostVariant savedVariant = postVariantRepository.save(variant);
        return variantMapper.toDto(savedVariant);
    }

    @Override
    @Transactional(readOnly = true)
    public PostVariantDto getPostVariantById(UUID postId , UUID variantId) {
        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new ResourceNotFoundException("Cannot find post with id: " + postId)
        );
        PostVariant variant = postVariantRepository.findById(variantId).orElseThrow(
                ()-> new ResourceNotFoundException("Cannot find variant with id: " + variantId)
        );
        if (!variant.getPost().getId().equals(post.getId())) {
            throw new ResourceNotFoundException("Cannot find variant " + variantId + " does not belong to post " + postId);
        }
        return variantMapper.toDto(variant);
    }

    @Override
    @Transactional
    public PostVariantDto updatePostVariant(UUID postId, UUID variantId ,PostVariantDto postVariantDto) {
        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new ResourceNotFoundException("Cannot find post with id: " + postId)
        );
        PostVariant variant = postVariantRepository.findById(variantId).orElseThrow(
                ()-> new ResourceNotFoundException("Cannot find postVariant with id: " + variantId)
        );
        if(!variant.getPost().getId().equals(postId)) {
            throw new ResourceNotFoundException("Cannot update post variant " + variantId + " does not belong to post " + postId);
        }

        variant.setContent(postVariantDto.getContent());
        variant.setHashtags(postVariantDto.getHashtags());
        variant.setScheduledAt(postVariantDto.getScheduledAt());

        PostVariant updateVariant = postVariantRepository.save(variant);
        return variantMapper.toDto(updateVariant);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostVariantDto> getAllPostVariants(UUID postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new ResourceNotFoundException("Cannot find post with id: " + postId));
        return post.getVariants().stream().map(variantMapper::toDto).collect(Collectors.toList());

    }

    @Override
    public void deletePostVariantById(UUID variantId) {
        if(!postVariantRepository.existsById(variantId)) {
            throw new ResourceNotFoundException("Cannot find post with id: " + variantId);
        }
        postVariantRepository.deleteById(variantId);
    }
}
