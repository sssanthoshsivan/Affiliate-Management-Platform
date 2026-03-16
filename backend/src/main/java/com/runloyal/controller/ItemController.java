package com.runloyal.controller;

import com.runloyal.dto.ItemRequest;
import com.runloyal.dto.ItemResponse;
import com.runloyal.service.ItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tenants/{tenantId}/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemResponse> create(@PathVariable Long tenantId,
                                                @Valid @RequestBody ItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(itemService.create(tenantId, request));
    }

    @GetMapping
    public ResponseEntity<List<ItemResponse>> findAll(@PathVariable Long tenantId) {
        return ResponseEntity.ok(itemService.findByTenantId(tenantId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse> findById(@PathVariable Long tenantId,
                                                  @PathVariable Long id) {
        return ResponseEntity.ok(itemService.findById(tenantId, id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemResponse> update(@PathVariable Long tenantId,
                                                @PathVariable Long id,
                                                @Valid @RequestBody ItemRequest request) {
        return ResponseEntity.ok(itemService.update(tenantId, id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long tenantId,
                                       @PathVariable Long id) {
        itemService.delete(tenantId, id);
        return ResponseEntity.noContent().build();
    }
}
