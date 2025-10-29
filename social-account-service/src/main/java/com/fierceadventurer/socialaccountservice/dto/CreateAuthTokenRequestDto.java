package com.fierceadventurer.socialaccountservice.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CreateAuthTokenRequestDto {

    @NotBlank(message = "Access token cannot be blank")
    private String accessToken;

    private String refreshToken;

    @NotNull(message = "Expiry time must be provided")
    @Future(message = "Token expiry must be in the future")
    private LocalDateTime expiry;

    private List<String> scopes;
}
