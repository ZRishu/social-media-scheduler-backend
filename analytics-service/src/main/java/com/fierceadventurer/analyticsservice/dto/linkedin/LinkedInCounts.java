package com.fierceadventurer.analyticsservice.dto.linkedin;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LinkedInCounts {

    @JsonAlias("numLikes")
    private int numLikes = 0;

    @JsonAlias("numComments")
    private int numComments = 0;

    @JsonAlias("numShares")
    private int numShares = 0;
}
