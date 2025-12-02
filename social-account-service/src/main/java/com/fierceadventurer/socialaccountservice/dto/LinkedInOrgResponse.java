package com.fierceadventurer.socialaccountservice.dto;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import java.util.List;


@Data
public class LinkedInOrgResponse {
    private List<Element> elements;

    @Data
    public static class Element {
        @JsonProperty("organizationalTarget~")
        private OrganizationDetails organizationDetails;
        private String organizationTarget;
    }

    @Data
    public static class OrganizationDetails {
        private String id;
        private String localizedName;
    }
    
}
