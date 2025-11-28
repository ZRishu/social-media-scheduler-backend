package com.fierceadventurer.socialaccountservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Element {
    @JsonProperty("organizationalTarget~")
    private OrganizationDetails organizationDetails;
    private String organizationTarget;
}
