package com.fierceadventurer.userservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdateUserRequestDto {
    private String displayName;
    private String status;
}
