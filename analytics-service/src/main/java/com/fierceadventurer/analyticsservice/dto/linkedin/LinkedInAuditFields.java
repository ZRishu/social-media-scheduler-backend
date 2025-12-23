package com.fierceadventurer.analyticsservice.dto.linkedin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LinkedInAuditFields {
    private long time;
}
