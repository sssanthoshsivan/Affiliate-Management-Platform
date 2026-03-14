package com.runloyal.service;

import com.runloyal.dto.OrderRequest;
import com.runloyal.dto.OrderResponse;
import com.runloyal.entity.Order;
import com.runloyal.entity.Tenant;
import com.runloyal.repository.OrderRepository;
import com.runloyal.util.TenantValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final TenantValidator tenantValidator;

    @Transactional
    public OrderResponse recordOrder(OrderRequest request) {
        Tenant tenant = tenantValidator.validateAndGet(request.getTenantId());

        // Commission = orderValue * (tenantRate / 100)
        BigDecimal commission = request.getOrderValue()
                .multiply(tenant.getCommissionRate())
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

        Order order = Order.builder()
                .tenantId(request.getTenantId())
                .affiliateId(request.getAffiliateId())
                .itemId(request.getItemId())
                .campaignId(request.getCampaignId())
                .orderValue(request.getOrderValue())
                .commission(commission)
                .build();

        order = orderRepository.save(order);

        return toResponse(order);
    }

    private OrderResponse toResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .tenantId(order.getTenantId())
                .affiliateId(order.getAffiliateId())
                .itemId(order.getItemId())
                .campaignId(order.getCampaignId())
                .orderValue(order.getOrderValue())
                .commission(order.getCommission())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
