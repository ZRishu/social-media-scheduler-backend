package com.fierceadventurer.postservice.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fierceadventurer.postservice.entity.Post;
import com.fierceadventurer.postservice.enums.PostStatus;
import com.fierceadventurer.postservice.events.AiGenerationCompletedEvent;
import com.fierceadventurer.postservice.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AiGenerationListener {

    private final PostRepository postRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "ai-service-completed-topic" , groupId = "post-service-ai-group")
    @Transactional
    public void handleAiGeneration(String message){
        log.info("Received AI Content Event: {}", message);

        try {
            AiGenerationCompletedEvent event = objectMapper.readValue(message , AiGenerationCompletedEvent.class);

            Post post = new Post();
            String title = "AI Draft" + (event.getPrompt().length() > 100 ? event.getPrompt().substring(0,97) + "..." : event.getPrompt());

            post.setTitle(title);
            post.setContent(event.getGeneratedContent());
            post.setStatus(PostStatus.DRAFT);
            post.setUserId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
            postRepository.save(post);
            log.info("Auto-saved AI Draft with ID: {}", post.getId());
        }
        catch (Exception e){
            log.error("Failed to process AI generation event", e);
        }
    }
}
