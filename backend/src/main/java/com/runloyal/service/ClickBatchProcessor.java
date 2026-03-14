package com.runloyal.service;

import com.runloyal.dto.ClickEvent;
import com.runloyal.entity.Click;
import com.runloyal.repository.ClickRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClickBatchProcessor {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ClickRepository clickRepository;

    private static final String CLICK_QUEUE_KEY = "click_queue";
    private static final String FRAUD_PREFIX = "fraud:";
    private static final int BATCH_SIZE = 500;

    @Scheduled(fixedRate = 5000)
    public void processClicks() {
        List<Click> clicksToSave = new ArrayList<>();
        
        for (int i = 0; i < BATCH_SIZE; i++) {
            ClickEvent event = (ClickEvent) redisTemplate.opsForList().leftPop(CLICK_QUEUE_KEY);
            if (event == null) break;

            // Fraud Check: Only track one click per IP/Affiliate every 60s
            String fraudKey = FRAUD_PREFIX + event.getIpAddress() + ":" + event.getAffiliateId();
            Boolean isDuplicate = redisTemplate.hasKey(fraudKey);

            if (Boolean.FALSE.equals(isDuplicate)) {
                // Set fraud TTL
                redisTemplate.opsForValue().set(fraudKey, "1", 60, TimeUnit.SECONDS);

                // Convert to Entity
                clicksToSave.add(Click.builder()
                        .tenantId(event.getTenantId())
                        .affiliateId(event.getAffiliateId())
                        .itemId(event.getItemId())
                        .campaignId(event.getCampaignId())
                        .ipAddress(event.getIpAddress())
                        .userAgent(event.getUserAgent())
                        .createdAt(event.getTimestamp())
                        .build());
            }
        }

        if (!clicksToSave.isEmpty()) {
            clickRepository.saveAll(clicksToSave);
            log.info("Persisted {} clicks to database", clicksToSave.size());
        }
    }
}
