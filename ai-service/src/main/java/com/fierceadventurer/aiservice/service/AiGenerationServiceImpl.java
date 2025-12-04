package com.fierceadventurer.aiservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fierceadventurer.aiservice.client.MediaServiceClient;
import com.fierceadventurer.aiservice.dto.GenerateRequestDto;
import com.fierceadventurer.aiservice.dto.GenerateResponseDto;
import com.fierceadventurer.aiservice.event.AiGenerationCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.content.Media;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AiGenerationServiceImpl implements AiGenerateService {

    private final ChatClient chatClient;
    private final MediaServiceClient mediaServiceClient;

    private final KafkaTemplate<String , String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public GenerateResponseDto generateContent(GenerateRequestDto request) {
        log.info("Generating content for prompt: [{}...], with {} ",
                request.getPrompt().substring(0, Math.min(request.getPrompt().length(), 50)),
                request.getMediaIds() == null ? 0 : request.getMediaIds().size());

        List<Media> mediaList = new ArrayList<>();
        if (request.getMediaIds() != null && !request.getMediaIds().isEmpty()) {
            for (UUID mediaId : request.getMediaIds()) {
                try{
                    mediaList.add(downloadMedia(mediaId));
                }
                catch (Exception e){
                    log.warn("Skipping failed media ID {}: {}", mediaId, e.getMessage());
                }
            }
        }

        UserMessage userMessage = UserMessage.builder()
                .text(request.getPrompt())
                .media(mediaList)
                .build();

        String systemPrompt = """
                You are an expert social media manager.
                Generate exactly 2-3 distinct, engaging options for a post based on user's prompt and attached media.

                RULES:
                - Do NOT use conversational filler (e.g. "Here is your post").
                - Use short paragraphs and emojis.
                - Separate each option with exactly three hashes: ###
                - Do NOT include my introductory or concluding text.
                - Start immediately with first option.
                - If an image is provided, describe it briefly in the context of the post.
                - Use appropriate hashtags if relevant to the prompt.
                """;

        try{
            ChatResponse response = chatClient.prompt()
                    .system(systemPrompt)
                    .messages(userMessage)
                    .call()
                    .chatResponse();

            String rawContent = response.getResult().getOutput().getText();

            List<String> options = Arrays.stream(rawContent.split("###"))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());

            String combinedContent = String.join("\n\n---\n\n", options);
            publishToKafka(request , combinedContent);
            return new GenerateResponseDto(rawContent);
        }
        catch (Exception e){
            log.error("AI Provider Error", e);
            return new GenerateResponseDto("Error generating content. Please try again without media or check backend logs.");

        }

    }

    private void publishToKafka(GenerateRequestDto request , String generateContent){
        try {
            AiGenerationCompletedEvent event = new AiGenerationCompletedEvent(
                    request.getPrompt(),
                    generateContent,
                    request.getPlatform() != null ? request.getPlatform() : "LINKEDIN",
                    LocalDateTime.now().toString()
            );

            String jsonEvent = objectMapper.writeValueAsString(event);

            kafkaTemplate.send("ai-service-completed-topic", jsonEvent);
            log.info("Successfully published AI generation event to Kafka");
        }
        catch (JsonProcessingException e){
            log.error("Failed to serialize AI event", e);
        }
        catch (Exception e) {
            log.error("Failed to send AI event to Kafka", e);
        }
    }

    private Media downloadMedia(UUID mediaId) {
        log.debug("Downloading media file from media-service: {}", mediaId);
        try {
            ResponseEntity<byte[]> response = mediaServiceClient.downloadFile(mediaId);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new RuntimeException("Media service returned error for ID: " + mediaId);
            }

            byte[] data = response.getBody();
            String contentType = response.getHeaders().getContentType() != null
                    ? response.getHeaders().getContentType().toString()
                    : "application/octet-stream";

            ByteArrayResource resource = new ByteArrayResource(data);
            MimeType mimeType = MimeType.valueOf(contentType);

            return new Media(mimeType, resource);
        } catch (Exception e) {
            log.error("failed to fetch media {}: {}", mediaId, e.getMessage(), e);
            throw new RuntimeException("failed to fetch media " + mediaId, e);
        }
    }


}
