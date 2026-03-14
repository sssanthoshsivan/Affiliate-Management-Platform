package com.runloyal.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TenantRequest {

    @NotBlank(message = "Tenant name is required")
    private String name;

    private String domain;

    private String status;

    private BigDecimal commissionRate;
}
