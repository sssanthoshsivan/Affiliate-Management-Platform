package com.runloyal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClickEvent implements Serializable {
    private Long tenantId;
    private Long affiliateId;
    private Long itemId;
    private Long campaignId;
    private String ipAddress;
    private String userAgent;
    private LocalDateTime timestamp;
}
