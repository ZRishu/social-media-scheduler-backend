package com.fierceadventurer.postservice.service.Impl;

import com.fierceadventurer.postservice.client.AnalyticsClient;
import com.fierceadventurer.postservice.client.SocialAccountClient;
import com.fierceadventurer.postservice.dto.CreatePostVariantRequestDto;
import com.fierceadventurer.postservice.dto.PostVariantResponseDto;
import com.fierceadventurer.postservice.dto.UpdatePostVariantRequestDto;
import com.fierceadventurer.postservice.entity.MediaAsset;
import com.fierceadventurer.postservice.entity.Post;
import com.fierceadventurer.postservice.entity.PostVariant;
import com.fierceadventurer.postservice.enums.Platform;
import com.fierceadventurer.postservice.enums.PostType;
import com.fierceadventurer.postservice.events.VariantReadyForSchedulingEvent;
import com.fierceadventurer.postservice.exception.MediaAssetNotFoundException;
import com.fierceadventurer.postservice.exception.ResourceNotFoundException;
import com.fierceadventurer.postservice.mapper.PostVariantMapper;
import com.fierceadventurer.postservice.repository.MediaAssetRepository;
import com.fierceadventurer.postservice.repository.PostRepository;
import com.fierceadventurer.postservice.repository.PostVariantRepository;
import com.fierceadventurer.postservice.service.PostVariantService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.apache.kafka.common.requests.FetchMetadata.log;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostVariantServiceImpl implements PostVariantService {
    private final PostVariantRepository postVariantRepository;
    private final PostRepository postRepository;
    private final PostVariantMapper postVariantMapper;
    private final MediaAssetRepository mediaAssetRepository;
    private final KafkaTemplate<String, VariantReadyForSchedulingEvent> kafkaTemplate;
    private final SocialAccountClient socialAccountClient;
    private final AnalyticsClient analyticsClient;

    @Override
    @Transactional
    public PostVariantResponseDto createNewVariant(UUID postId, UUID userId, CreatePostVariantRequestDto createDto) {

        try{
            socialAccountClient.validateAccountOwnerShip(createDto.getSocialAccountId(), userId);
        }
        catch(Exception e){
            log.error("Failed to validate account ownership. Reason: ", e);
            throw new AccessDeniedException("User does not own social account: " + createDto.getSocialAccountId());
        }

        Post post = findPostAndVerifyOwnership(postId , userId , "create variant for");

        PostVariant variant = postVariantMapper.toEntity(createDto);
        variant.setPost(post);

        if(variant.getScheduledAt() == null) {
            log.info("No schedule time provided for variant. Fetching best time...");
            LocalDateTime bestTime = analyticsClient.getNextBestTime(createDto.getSocialAccountId())
                    .getNextBestTime();
            variant.setScheduledAt(bestTime);
        }

        if(createDto.getMediaUrls() != null
                && !createDto.getMediaUrls().isEmpty()) {
            List<MediaAsset> mediaAssets= createDto.getMediaUrls().stream()
                .map(url -> {
                    MediaAsset asset = new MediaAsset();
                    asset.setPost(post);
                    asset.setUserId(userId);
                    asset.setStorageUrl(url);


                    asset.setMediaType(mapPlatformToPostType(variant.getPlatform()));
                    asset.setSize(0L); // Placeholder.

                    return mediaAssetRepository.save(asset);
                }).collect(Collectors.toList());


            for(MediaAsset asset : mediaAssets) {
                if(!asset.getPost().getUserId().equals(userId)) {
                    log.warn("User {} attempted to use media asset{} which they do not own (Owner: {})", userId , asset.getAssetId() , asset.getUserId());
                    throw new AccessDeniedException("User does not have permission to use social media asset: " + asset.getAssetId());
                }
            }
            variant.setMediaAssets(mediaAssets);
        }

        PostVariant savedVariant = postVariantRepository.save(variant);

        publishSchedulingEvent(savedVariant , createDto.getSocialAccountId());

        return postVariantMapper.toDto(savedVariant);
    }

    private PostType mapPlatformToPostType(@NotNull(message = "Platform cannot be blank") Platform platform) {
        if (platform == Platform.INSTAGRAM || platform == Platform.THREADS || platform == Platform.LINKEDIN) {
            return PostType.IMAGE; // Or a more complex logic to check content type
        }
        return PostType.TEXT;
    }

    @Override
    @Transactional(readOnly = true)
    public PostVariantResponseDto getPostVariantById(UUID postId ,UUID userId,  UUID variantId) {
        Post post = findPostAndVerifyOwnership(postId , userId , "view");

        PostVariant variant = postVariantRepository.findById(variantId).orElseThrow(
                ()-> new ResourceNotFoundException("Cannot find variant with id: " + variantId)
        );

        if (!variant.getPost().getId().equals(post.getId())) {
            throw new ResourceNotFoundException("Variant " + variantId + " does not belong to post " + postId);
        }

        return postVariantMapper.toDto(variant);
    }

    @Override
    @Transactional
    public PostVariantResponseDto updateExistingVariant(
            UUID postId, UUID userId,
            UUID variantId , UpdatePostVariantRequestDto updateDto) {


        Post post = findPostAndVerifyOwnership(postId , userId , "update");


        PostVariant existingVariant = postVariantRepository.findById(variantId).orElseThrow(
                ()-> new ResourceNotFoundException("Cannot find postVariant with id: " + variantId)
        );

        if(!existingVariant.getPost().getId().equals(post.getId())) {
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
    public List<PostVariantResponseDto> getAllPostVariants(UUID postId, UUID userId) {
        Post post = findPostAndVerifyOwnership(postId, userId, "view");

        return post.getVariants().stream().
                map(postVariantMapper::toDto)
                .collect(Collectors.toList());

    }

    @Override
    @Transactional
    public void deletePostVariantById(UUID postId , UUID userId , UUID variantId) {
        Post post = findPostAndVerifyOwnership(postId, userId, "delete");

        PostVariant variant = postVariantRepository.findById(variantId).orElseThrow(()->
                new ResourceNotFoundException("Cannot find variant with id: " + variantId));

        if(!variant.getPost().getId().equals(postId)) {
            throw new ResourceNotFoundException("Variant " + variantId + " does not belong to post " + postId);
        }
        postVariantRepository.deleteById(variantId);
    }

    private Post findPostAndVerifyOwnership(UUID postId, UUID userId, String action) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        if (!post.getUserId().equals(userId)) {
            log.warn("User {} attempted to {} variant for post {} which they do not own (Owner: {})",
                    userId, action, postId, post.getUserId());
            throw new AccessDeniedException("User does not have permission to modify this post.");
        }
        return post;
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
        log.info("Published {} event to topic '{}' for variant ID: {}",
                isScheduled ? "SCHEDULED" : "IMMEDIATE", topic, variant.getVariantId());



    }
}
