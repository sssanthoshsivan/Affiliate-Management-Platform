package com.runloyal.controller;

import com.runloyal.dto.ClickEvent;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class TrackingController {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String CLICK_QUEUE_KEY = "click_queue";

    @Value("${app.frontend.url:http://localhost}")
    private String frontendUrl;

    @GetMapping("/track")
    public void track(
            @RequestParam Long tenantId,
            @RequestParam Long affiliateId,
            @RequestParam(required = false) Long itemId,
            @RequestParam(required = false) Long campaignId,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        // Record click asynchronously via Redis queue
        ClickEvent event = ClickEvent.builder()
                .tenantId(tenantId)
                .affiliateId(affiliateId)
                .itemId(itemId)
                .campaignId(campaignId)
                .ipAddress(request.getRemoteAddr())
                .userAgent(request.getHeader("User-Agent"))
                .timestamp(LocalDateTime.now())
                .build();

        redisTemplate.opsForList().rightPush(CLICK_QUEUE_KEY, event);

        // Redirect to the Angular product page with tracking params embedded
        StringBuilder redirectUrl = new StringBuilder(frontendUrl);
        if (itemId != null) {
            redirectUrl.append("/product/").append(itemId);
        } else {
            redirectUrl.append("/");
        }
        redirectUrl.append("?tenantId=").append(tenantId)
                   .append("&affiliateId=").append(affiliateId);
        if (campaignId != null) {
            redirectUrl.append("&campaignId=").append(campaignId);
        }

        response.sendRedirect(redirectUrl.toString());
    }
}
