package com.runloyal.repository;

import com.runloyal.entity.AffiliateLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AffiliateLinkRepository extends JpaRepository<AffiliateLink, Long> {

    List<AffiliateLink> findByTenantId(Long tenantId);

    Optional<AffiliateLink> findByShortCode(String shortCode);

    Optional<AffiliateLink> findByAffiliateIdAndItemIdAndCampaignId(
            Long affiliateId, Long itemId, Long campaignId);
}
