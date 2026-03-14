package com.runloyal.dto;

import com.runloyal.entity.ItemType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemRequest {

    @NotBlank(message = "Item name is required")
    private String name;

    @NotNull(message = "Item type is required (PRODUCT or SERVICE)")
    private ItemType type;

    private String category;

    private BigDecimal price;
}
