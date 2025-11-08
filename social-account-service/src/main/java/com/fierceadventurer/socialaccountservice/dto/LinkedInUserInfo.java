package com.fierceadventurer.socialaccountservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LinkedInUserInfo {
    @JsonProperty("sub")
    private String externalId;

    @JsonProperty("name")
    private String fullName;

    @JsonProperty("given_name")
    private String firstName;

    @JsonProperty("family_name")
    private String lastName;

    @JsonProperty("picture")
    private String pictureUrl;

    @JsonProperty("email")
    private String email;
}
