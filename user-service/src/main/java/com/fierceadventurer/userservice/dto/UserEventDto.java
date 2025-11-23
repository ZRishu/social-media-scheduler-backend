package com.fierceadventurer.userservice.dto;

import lombok.Data;

@Data
public class UserEventDto {
    private String userId;
    private String email;
    private String username;
    private String firstName;
    private String lastName;
}
