package com.runloyal.controller;

import com.runloyal.dto.AffiliateRequest;
import com.runloyal.dto.AffiliateResponse;
import com.runloyal.service.AffiliateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tenants/{tenantId}/affiliates")
@RequiredArgsConstructor
public class AffiliateController {

    private final AffiliateService affiliateService;

    @PostMapping
    public ResponseEntity<AffiliateResponse> create(@PathVariable Long tenantId,
                                                     @Valid @RequestBody AffiliateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(affiliateService.create(tenantId, request));
    }

    @GetMapping
    public ResponseEntity<List<AffiliateResponse>> findAll(@PathVariable Long tenantId) {
        return ResponseEntity.ok(affiliateService.findByTenantId(tenantId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AffiliateResponse> findById(@PathVariable Long tenantId,
                                                       @PathVariable Long id) {
        return ResponseEntity.ok(affiliateService.findById(tenantId, id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AffiliateResponse> update(@PathVariable Long tenantId,
                                                     @PathVariable Long id,
                                                     @Valid @RequestBody AffiliateRequest request) {
        return ResponseEntity.ok(affiliateService.update(tenantId, id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long tenantId,
                                       @PathVariable Long id) {
        affiliateService.delete(tenantId, id);
        return ResponseEntity.noContent().build();
    }
}
