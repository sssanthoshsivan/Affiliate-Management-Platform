package com.runloyal.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "affiliate_links")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AffiliateLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @Column(name = "affiliate_id", nullable = false)
    private Long affiliateId;

    @Column(name = "item_id", nullable = false)
    private Long itemId;

    @Column(name = "campaign_id", nullable = false)
    private Long campaignId;

    @Column(name = "short_code", nullable = false, unique = true, length = 20)
    private String shortCode;

    @Column(name = "tracking_code", nullable = false)
    private String trackingCode;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
}
