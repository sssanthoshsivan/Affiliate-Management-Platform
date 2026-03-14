package com.runloyal.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderRequest {
    @NotNull(message = "Tenant ID is required")
    private Long tenantId;
    
    @NotNull(message = "Affiliate ID is required")
    private Long affiliateId;
    
    @NotNull(message = "Item ID is required")
    private Long itemId;
    
    private Long campaignId;
    
    @NotNull(message = "Order value is required")
    private BigDecimal orderValue;
}
