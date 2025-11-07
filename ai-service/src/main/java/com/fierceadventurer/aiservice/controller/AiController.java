package com.fierceadventurer.aiservice.controller;

import com.fierceadventurer.aiservice.dto.GenerateRequestDto;
import com.fierceadventurer.aiservice.dto.GenerateResponseDto;
import com.fierceadventurer.aiservice.service.AiGenerateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class AiController {
    private final AiGenerateService aiGenerateService;

    @PostMapping("/generate")
    public ResponseEntity<GenerateResponseDto> generateContent(
            @Valid @RequestBody GenerateRequestDto requestDto){
        GenerateResponseDto response = aiGenerateService.generateContent(requestDto);
        return ResponseEntity.ok(response);
    }

}
