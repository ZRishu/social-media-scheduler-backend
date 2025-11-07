package com.fierceadventurer.aiservice.service;

import com.fierceadventurer.aiservice.dto.GenerateRequestDto;
import com.fierceadventurer.aiservice.dto.GenerateResponseDto;

public interface AiGenerateService {
    GenerateResponseDto generateContent(GenerateRequestDto request);
}
