package com.runloyal.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AffiliatePerformance {
    private Long affiliateId;
    private String affiliateName;
    private Long clicks;
    private Long orders;
    private BigDecimal revenue;
    private BigDecimal commission;
}
