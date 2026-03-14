package com.runloyal.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AffiliateLinkRequest {
    @NotNull(message = "Tenant ID is required")
    private Long tenantId;
    @NotNull(message = "Affiliate ID is required")
    private Long affiliateId;
    @NotNull(message = "Item ID is required")
    private Long itemId;
    @NotNull(message = "Campaign ID is required")
    private Long campaignId;
}
