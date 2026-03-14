package com.runloyal.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AffiliateLinkResponse {
    private String shortCode;
    private String trackingUrl;
    private String shortUrl;
}
