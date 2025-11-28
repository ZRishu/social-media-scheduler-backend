package com.fierceadventurer.socialaccountservice.dto;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

import java.util.List;


@Data
public class LinkedInOrgResponse {
    private List<Element> elements;
    
}
