package com.runloyal.service;

import com.runloyal.dto.ItemRequest;
import com.runloyal.dto.ItemResponse;
import com.runloyal.entity.Item;
import com.runloyal.exception.ResourceNotFoundException;
import com.runloyal.repository.ItemRepository;
import com.runloyal.util.TenantValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.runloyal.repository.AffiliateLinkRepository;
import com.runloyal.repository.ClickRepository;
import com.runloyal.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final AffiliateLinkRepository affiliateLinkRepository;
    private final ClickRepository clickRepository;
    private final OrderRepository orderRepository;
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

    public ItemResponse update(Long tenantId, Long id, ItemRequest request) {
        tenantValidator.validate(tenantId);
        Item item = itemRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Item", id));
        
        item.setName(request.getName());
        item.setType(request.getType());
        item.setCategory(request.getCategory());
        item.setPrice(request.getPrice() != null ? request.getPrice() : BigDecimal.ZERO);
        
        return toResponse(itemRepository.save(item));
    }

    @Transactional
    public void delete(Long tenantId, Long id) {
        tenantValidator.validate(tenantId);
        Item item = itemRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Item", id));
        
        // Delete dependent records
        affiliateLinkRepository.deleteByTenantIdAndItemId(tenantId, id);
        clickRepository.deleteByTenantIdAndItemId(tenantId, id);
        orderRepository.deleteByTenantIdAndItemId(tenantId, id);
        
        itemRepository.delete(item);
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
