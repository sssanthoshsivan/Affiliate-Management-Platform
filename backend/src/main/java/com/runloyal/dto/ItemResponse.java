package com.runloyal.dto;

import com.runloyal.entity.ItemType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ItemResponse {
    private Long id;
    private Long tenantId;
    private String name;
    private ItemType type;
    private String category;
    private BigDecimal price;
    private LocalDateTime createdAt;
}
