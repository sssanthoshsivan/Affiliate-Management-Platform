package com.runloyal.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @Column(name = "affiliate_id", nullable = false)
    private Long affiliateId;

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "campaign_id")
    private Long campaignId;

    @Column(name = "order_value", nullable = false)
    @Builder.Default
    private BigDecimal orderValue = BigDecimal.ZERO;

    @Column(nullable = false)
    @Builder.Default
    private BigDecimal commission = BigDecimal.ZERO;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
}
