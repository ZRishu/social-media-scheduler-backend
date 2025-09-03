package com.fierceadventurer.postservice.service.Impl;

import com.fierceadventurer.postservice.dto.CreatePostVariantRequestDto;
import com.fierceadventurer.postservice.dto.PostVariantResponseDto;
import com.fierceadventurer.postservice.dto.UpdatePostVariantRequestDto;
import com.fierceadventurer.postservice.entity.MediaAsset;
import com.fierceadventurer.postservice.entity.Post;
import com.fierceadventurer.postservice.entity.PostVariant;
import com.fierceadventurer.postservice.events.VariantReadyForSchedulingEvent;
import com.fierceadventurer.postservice.mapper.PostVariantMapper;
import com.fierceadventurer.postservice.repository.MediaAssetRepository;
import com.fierceadventurer.postservice.repository.PostRepository;
import com.fierceadventurer.postservice.repository.PostVariantRepository;
import com.fierceadventurer.postservice.service.PostVariantService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.kafka.core.KafkaTemplate;
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
    private final PostVariantMapper postVariantMapper;
    private final MediaAssetRepository mediaAssetRepository;
    private final KafkaTemplate<String, VariantReadyForSchedulingEvent> kafkaTemplate;

    @Override
    @Transactional
    public PostVariantResponseDto createNewVariant(UUID postId, CreatePostVariantRequestDto createDto) {
       Post post = postRepository.findById(postId).orElseThrow(
               ()-> new ResourceNotFoundException("Cannot create a variant for non existing post with id: " + postId));
        PostVariant variant = postVariantMapper.toEntity(createDto);
        variant.setPost(post);

        if(createDto.getMediaAssetIds() != null
                && !createDto.getMediaAssetIds().isEmpty()) {
            List<MediaAsset> mediaAssets= mediaAssetRepository.findAllById(createDto
                    .getMediaAssetIds());
            variant.setMediaAssets(mediaAssets);
        }

        PostVariant savedVariant = postVariantRepository.save(variant);

        publishSchedulingEvent(savedVariant , createDto.getSocialAccountId());

        return postVariantMapper.toDto(savedVariant);
    }

    @Override
    @Transactional(readOnly = true)
    public PostVariantResponseDto getPostVariantById(UUID postId , UUID variantId) {
        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new ResourceNotFoundException("Cannot find post with id: " + postId)
        );
        PostVariant variant = postVariantRepository.findById(variantId).orElseThrow(
                ()-> new ResourceNotFoundException("Cannot find variant with id: " + variantId)
        );
        if (!variant.getPost().getId().equals(post.getId())) {
            throw new ResourceNotFoundException("Cannot find variant " + variantId + " does not belong to post " + postId);
        }
        return postVariantMapper.toDto(variant);
    }

    @Override
    @Transactional
    public PostVariantResponseDto updateExistingVariant(UUID postId, UUID variantId , UpdatePostVariantRequestDto updateDto) {
        postRepository.findById(postId).orElseThrow(
                ()-> new ResourceNotFoundException("Cannot find post with id: " + postId)
        );
        PostVariant existingVariant = postVariantRepository.findById(variantId).orElseThrow(
                ()-> new ResourceNotFoundException("Cannot find postVariant with id: " + variantId)
        );
        if(!existingVariant.getPost().getId().equals(postId)) {
            throw new ResourceNotFoundException("Cannot update post variant " + variantId + " does not belong to post " + postId);
        }
        postVariantMapper.updateFromDto(updateDto, existingVariant);

        if(updateDto.getMediaAssetIds() != null){
            List<MediaAsset> mediaAssets= mediaAssetRepository.findAllById( updateDto.getMediaAssetIds());
            existingVariant.setMediaAssets(mediaAssets);
        }


        PostVariant updateVariant = postVariantRepository.save(existingVariant);
        return postVariantMapper.toDto(updateVariant);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostVariantResponseDto> getAllPostVariants(UUID postId) {
        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new ResourceNotFoundException("Cannot find post with id: " + postId));
        return post.getVariants().stream().map(postVariantMapper::toDto).collect(Collectors.toList());

    }

    @Override
    public void deletePostVariantById(UUID variantId) {
        if(!postVariantRepository.existsById(variantId)) {
            throw new ResourceNotFoundException("Cannot find post with id: " + variantId);
        }
        postVariantRepository.deleteById(variantId);
    }

    private void publishSchedulingEvent(PostVariant variant, UUID socialAccountId) {
        boolean isScheduled = variant.getScheduledAt() != null;
        String topic = isScheduled ? "variant-scheduling-topic" : "variant-immediate-publish-topic";

        VariantReadyForSchedulingEvent event = new VariantReadyForSchedulingEvent();
        event.setPostId(variant.getPost().getId());
        event.setVariantId(variant.getVariantId());
        event.setSocialAccountId(socialAccountId);
        event.setPlatform(variant.getPlatform());
        event.setTitle(variant.getPost().getTitle());
        event.setContent(variant.getContent());
        event.setHashtags(variant.getHashtags());
        event.setScheduledAt(variant.getScheduledAt());

        List<String> urls = variant.getMediaAssets().stream().map(MediaAsset::getStorageUrl)
                .collect(Collectors.toList());
        event.setMediaUrls(urls);
        kafkaTemplate.send(topic, event);


    }
}
