package com.runloyal.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class AnalyticsResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long totalClicks;
    private Long totalOrders;
    private BigDecimal totalRevenue;
    private BigDecimal totalCommission;
    private List<AffiliatePerformance> topAffiliates;
}
