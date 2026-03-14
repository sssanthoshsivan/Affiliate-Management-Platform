package com.runloyal.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AffiliateResponse {
    private Long id;
    private Long tenantId;
    private String name;
    private String email;
    private String status;
    private LocalDateTime createdAt;
}
