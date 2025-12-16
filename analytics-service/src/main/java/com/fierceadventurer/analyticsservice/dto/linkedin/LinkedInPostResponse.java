package com.fierceadventurer.analyticsservice.dto.linkedin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LinkedInPostResponse {
    private List<LinkedInElement> elements;
}
