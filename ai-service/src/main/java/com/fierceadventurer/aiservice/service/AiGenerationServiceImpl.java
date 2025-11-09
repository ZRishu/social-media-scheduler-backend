package com.fierceadventurer.aiservice.service;

import com.fierceadventurer.aiservice.client.MediaServiceClient;
import com.fierceadventurer.aiservice.dto.GenerateRequestDto;
import com.fierceadventurer.aiservice.dto.GenerateResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.content.Media;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;

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

    @Override
    public GenerateResponseDto generateContent(GenerateRequestDto request) {
        log.info("Generating content for prompt: [{}...], with {} ",
                request.getPrompt().substring(0, Math.min(request.getPrompt().length(), 50)),
                request.getMediaIds() == null ? 0 : request.getMediaIds().size());

        List<Media> mediaList = new ArrayList<>();
        if (request.getMediaIds() != null && !request.getMediaIds().isEmpty()) {
            for (UUID mediaId : request.getMediaIds()) {
                mediaList.add(downloadMedia(mediaId));
            }
        }

        // Build the UserMessage with media using the builder
        UserMessage userMessage = UserMessage.builder()
                .text(request.getPrompt())
                .media(mediaList)
                .build();

        String systemPrompt = """
                You are an expert social media manager.
                Generate exactly 2-3 distinct, engaging options for a post based on user's prompt and attached media.

                RULES:
                - Separate each option with exactly three hashes: ###
                - Do NOT include my introductory or concluding text.
                - Start immediately with first option.
                - Use appropriate hashtags if relevant to the prompt.
                """;

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

        return new GenerateResponseDto(options);
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

            // wrap bytes in a Resource and construct Media with MimeType + Resource
            ByteArrayResource resource = new ByteArrayResource(data);
            MimeType mimeType = MimeType.valueOf(contentType);

            return new Media(mimeType, resource);
        } catch (Exception e) {
            log.error("failed to fetch media {}: {}", mediaId, e.getMessage(), e);
            throw new RuntimeException("failed to fetch media " + mediaId, e);
        }
    }
}
