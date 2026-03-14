package com.runloyal.controller;

import com.runloyal.dto.CampaignRequest;
import com.runloyal.dto.CampaignResponse;
import com.runloyal.service.CampaignService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tenants/{tenantId}/campaigns")
@RequiredArgsConstructor
public class CampaignController {

    private final CampaignService campaignService;

    @PostMapping
    public ResponseEntity<CampaignResponse> create(@PathVariable Long tenantId,
                                                     @Valid @RequestBody CampaignRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(campaignService.create(tenantId, request));
    }

    @GetMapping
    public ResponseEntity<List<CampaignResponse>> findAll(@PathVariable Long tenantId) {
        return ResponseEntity.ok(campaignService.findByTenantId(tenantId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CampaignResponse> findById(@PathVariable Long tenantId,
                                                       @PathVariable Long id) {
        return ResponseEntity.ok(campaignService.findById(tenantId, id));
    }
}
