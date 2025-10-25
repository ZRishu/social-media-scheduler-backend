package com.fierceadventurer.schedulerservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateJobRequestDto {
    @NotNull(message = "Scheduled time cannot be null")
    @NotNull(message = "Scheduled time must be in future")
    private LocalDateTime scheduledAt;
}
