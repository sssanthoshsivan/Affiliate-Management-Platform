package com.runloyal.service;

import com.runloyal.dto.AffiliatePerformance;
import com.runloyal.dto.AnalyticsResponse;
import com.runloyal.entity.Affiliate;
import com.runloyal.repository.AffiliateRepository;
import com.runloyal.repository.ClickRepository;
import com.runloyal.repository.OrderRepository;
import com.runloyal.util.TenantValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final ClickRepository clickRepository;
    private final OrderRepository orderRepository;
    private final AffiliateRepository affiliateRepository;
    private final TenantValidator tenantValidator;

    @Cacheable(value = "analytics", key = "#tenantId + '-' + #affiliateId + '-' + #campaignId + '-' + #itemId + '-' + #from + '-' + #to")
    public AnalyticsResponse getAnalytics(Long tenantId, Long affiliateId, Long campaignId, 
                                           Long itemId, LocalDateTime from, LocalDateTime to) {
        tenantValidator.validate(tenantId);

        Long clicks = clickRepository.countFiltered(tenantId, affiliateId, campaignId, itemId, from, to);
        Long ordersCount = orderRepository.countFiltered(tenantId, affiliateId, campaignId, itemId, from, to);
        BigDecimal revenue = orderRepository.sumRevenue(tenantId, affiliateId, campaignId, itemId, from, to);
        BigDecimal commission = orderRepository.sumCommission(tenantId, affiliateId, campaignId, itemId, from, to);

        // Per-affiliate breakdown (always for the whole tenant, filtered by date)
        List<Object[]> affiliateData = orderRepository.aggregateByAffiliate(tenantId, from, to);
        
        // Fetch names for performance table
        List<Affiliate> affiliates = affiliateRepository.findByTenantId(tenantId);
        Map<Long, String> nameMap = affiliates.stream()
                .collect(Collectors.toMap(Affiliate::getId, Affiliate::getName));

        List<AffiliatePerformance> performanceList = new ArrayList<>();
        for (Object[] row : affiliateData) {
            Long affId = (Long) row[0];
            performanceList.add(AffiliatePerformance.builder()
                    .affiliateId(affId)
                    .affiliateName(nameMap.getOrDefault(affId, "Unknown"))
                    .orders((Long) row[1])
                    .revenue((BigDecimal) row[2])
                    .commission((BigDecimal) row[3])
                    // Note: Clicks per affiliate could be joined here too
                    .build());
        }

        return AnalyticsResponse.builder()
                .totalClicks(clicks)
                .totalOrders(ordersCount)
                .totalRevenue(revenue)
                .totalCommission(commission)
                .topAffiliates(performanceList)
                .build();
    }
}
