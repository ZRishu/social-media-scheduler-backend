package com.fierceadventurer.postservice.events;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AiGenerationCompletedEvent {
    private String prompt;
    private String generatedContent;
    private String platform;
    private String timestamp;
}
