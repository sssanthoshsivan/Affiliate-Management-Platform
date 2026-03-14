package com.runloyal.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class OrderResponse {
    private Long id;
    private Long tenantId;
    private Long affiliateId;
    private Long itemId;
    private Long campaignId;
    private BigDecimal orderValue;
    private BigDecimal commission;
    private LocalDateTime createdAt;
}
