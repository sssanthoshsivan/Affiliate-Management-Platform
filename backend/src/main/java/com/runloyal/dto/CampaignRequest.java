package com.runloyal.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CampaignRequest {

    @NotBlank(message = "Campaign name is required")
    private String name;

    private String description;
}
