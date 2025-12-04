package com.fierceadventurer.aiservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AiGenerationCompletedEvent {
    private String prompt;
    private String generatedContent;
    private String platform;
    private String timestamp;
}
