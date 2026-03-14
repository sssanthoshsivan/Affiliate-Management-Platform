package com.runloyal.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class AnalyticsResponse {
    private Long totalClicks;
    private Long totalOrders;
    private BigDecimal totalRevenue;
    private BigDecimal totalCommission;
    private List<AffiliatePerformance> topAffiliates;
}
