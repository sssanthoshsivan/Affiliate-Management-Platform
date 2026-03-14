package com.runloyal.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class TenantResponse {
    private Long id;
    private String name;
    private String domain;
    private String status;
    private BigDecimal commissionRate;
    private LocalDateTime createdAt;
}
