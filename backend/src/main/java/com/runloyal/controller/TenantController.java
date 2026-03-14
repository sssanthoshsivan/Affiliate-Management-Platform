package com.runloyal.controller;

import com.runloyal.dto.TenantRequest;
import com.runloyal.dto.TenantResponse;
import com.runloyal.service.TenantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    @PostMapping
    public ResponseEntity<TenantResponse> create(@Valid @RequestBody TenantRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tenantService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<TenantResponse>> findAll() {
        return ResponseEntity.ok(tenantService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TenantResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(tenantService.findById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TenantResponse> updateCommissionRate(@PathVariable Long id,
                                                                @RequestBody Map<String, BigDecimal> body) {
        BigDecimal rate = body.get("commissionRate");
        if (rate == null) {
            throw new IllegalArgumentException("commissionRate is required");
        }
        return ResponseEntity.ok(tenantService.updateCommissionRate(id, rate));
    }
}
