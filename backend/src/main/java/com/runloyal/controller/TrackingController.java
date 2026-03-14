package com.runloyal.controller;

import com.runloyal.dto.ClickEvent;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class TrackingController {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String CLICK_QUEUE_KEY = "click_queue";

    @GetMapping("/track")
    public ResponseEntity<String> track(
            @RequestParam Long tenantId,
            @RequestParam Long affiliateId,
            @RequestParam(required = false) Long itemId,
            @RequestParam(required = false) Long campaignId,
            HttpServletRequest request) {

        ClickEvent event = ClickEvent.builder()
                .tenantId(tenantId)
                .affiliateId(affiliateId)
                .itemId(itemId)
                .campaignId(campaignId)
                .ipAddress(request.getRemoteAddr())
                .userAgent(request.getHeader("User-Agent"))
                .timestamp(LocalDateTime.now())
                .build();

        // Push to Redis queue (returns instantly)
        redisTemplate.opsForList().rightPush(CLICK_QUEUE_KEY, event);

        return ResponseEntity.ok("Tracked");
    }
}
