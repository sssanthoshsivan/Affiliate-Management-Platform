package com.runloyal.service;

import com.runloyal.dto.ItemRequest;
import com.runloyal.dto.ItemResponse;
import com.runloyal.entity.Item;
import com.runloyal.exception.ResourceNotFoundException;
import com.runloyal.repository.ItemRepository;
import com.runloyal.util.TenantValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final TenantValidator tenantValidator;

    public ItemResponse create(Long tenantId, ItemRequest request) {
        tenantValidator.validate(tenantId);
        Item item = Item.builder()
                .tenantId(tenantId)
                .name(request.getName())
                .type(request.getType())
                .category(request.getCategory())
                .price(request.getPrice() != null ? request.getPrice() : BigDecimal.ZERO)
                .build();
        return toResponse(itemRepository.save(item));
    }

    public List<ItemResponse> findByTenantId(Long tenantId) {
        tenantValidator.validate(tenantId);
        return itemRepository.findByTenantId(tenantId).stream()
                .map(this::toResponse)
                .toList();
    }

    public ItemResponse findById(Long tenantId, Long id) {
        tenantValidator.validate(tenantId);
        Item item = itemRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Item", id));
        return toResponse(item);
    }

    private ItemResponse toResponse(Item item) {
        return ItemResponse.builder()
                .id(item.getId())
                .tenantId(item.getTenantId())
                .name(item.getName())
                .type(item.getType())
                .category(item.getCategory())
                .price(item.getPrice())
                .createdAt(item.getCreatedAt())
                .build();
    }
}
