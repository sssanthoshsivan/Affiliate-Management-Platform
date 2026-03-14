package com.runloyal.controller;

import com.runloyal.dto.AnalyticsResponse;
import com.runloyal.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/tenants/{tenantId}/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping
    public ResponseEntity<AnalyticsResponse> getAnalytics(
            @PathVariable Long tenantId,
            @RequestParam(required = false) Long affiliateId,
            @RequestParam(required = false) Long campaignId,
            @RequestParam(required = false) Long itemId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {

        return ResponseEntity.ok(analyticsService.getAnalytics(tenantId, affiliateId, campaignId, itemId, from, to));
    }
}
