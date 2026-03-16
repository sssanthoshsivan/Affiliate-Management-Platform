package com.runloyal.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
public class AffiliatePerformance implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long affiliateId;
    private String affiliateName;
    private Long clicks;
    private Long orders;
    private BigDecimal revenue;
    private BigDecimal commission;
}
