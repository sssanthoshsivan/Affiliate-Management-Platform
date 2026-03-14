package com.runloyal.service;

import com.runloyal.dto.AffiliateLinkRequest;
import com.runloyal.dto.AffiliateLinkResponse;
import com.runloyal.entity.AffiliateLink;
import com.runloyal.exception.ResourceNotFoundException;
import com.runloyal.repository.AffiliateLinkRepository;
import com.runloyal.repository.AffiliateRepository;
import com.runloyal.repository.CampaignRepository;
import com.runloyal.repository.ItemRepository;
import com.runloyal.util.TenantValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AffiliateLinkService {

    private final AffiliateLinkRepository affiliateLinkRepository;
    private final AffiliateRepository affiliateRepository;
    private final ItemRepository itemRepository;
    private final CampaignRepository campaignRepository;
    private final TenantValidator tenantValidator;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String LINK_CACHE_PREFIX = "link:";

    public AffiliateLinkResponse generateLink(AffiliateLinkRequest request) {
        tenantValidator.validate(request.getTenantId());

        // Validate entities exist
        if (!affiliateRepository.existsById(request.getAffiliateId()))
            throw new ResourceNotFoundException("Affiliate", request.getAffiliateId());
        if (!itemRepository.existsById(request.getItemId()))
            throw new ResourceNotFoundException("Item", request.getItemId());
        if (!campaignRepository.existsById(request.getCampaignId()))
            throw new ResourceNotFoundException("Campaign", request.getCampaignId());

        // Check if link already exists
        Optional<AffiliateLink> existing = affiliateLinkRepository.findByAffiliateIdAndItemIdAndCampaignId(
                request.getAffiliateId(), request.getItemId(), request.getCampaignId());

        AffiliateLink link;
        if (existing.isPresent()) {
            link = existing.get();
        } else {
            String shortCode = UUID.randomUUID().toString().substring(0, 8);
            String trackingCode = "aff_" + request.getAffiliateId() + "_itm_" + request.getItemId();

            link = AffiliateLink.builder()
                    .tenantId(request.getTenantId())
                    .affiliateId(request.getAffiliateId())
                    .itemId(request.getItemId())
                    .campaign_id(request.getCampaignId()) // Fixed field name from previous entity edit
                    .shortCode(shortCode)
                    .trackingCode(trackingCode)
                    .build();
            link = affiliateLinkRepository.save(link);
        }

        // Cache in Redis for fast lookup
        String trackingUrl = "/track?tenantId=" + link.getTenantId() +
                "&affiliateId=" + link.getAffiliateId() +
                "&itemId=" + link.getItemId() +
                "&campaignId=" + link.getCampaignId();

        redisTemplate.opsForValue().set(LINK_CACHE_PREFIX + link.getShortCode(), trackingUrl, 24, TimeUnit.HOURS);

        return AffiliateLinkResponse.builder()
                .shortCode(link.getShortCode())
                .trackingUrl(trackingUrl)
                .shortUrl("/r/" + link.getShortCode())
                .build();
    }

    public String resolveUrl(String shortCode) {
        // Try cache
        String cachedUrl = (String) redisTemplate.opsForValue().get(LINK_CACHE_PREFIX + shortCode);
        if (cachedUrl != null) {
            return cachedUrl;
        }

        // Try DB
        AffiliateLink link = affiliateLinkRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new ResourceNotFoundException("Link not found: " + shortCode));

        String trackingUrl = "/track?tenantId=" + link.getTenantId() +
                "&affiliateId=" + link.getAffiliateId() +
                "&itemId=" + link.getItemId() +
                "&campaignId=" + link.getCampaignId();

        // Populate cache
        redisTemplate.opsForValue().set(LINK_CACHE_PREFIX + shortCode, trackingUrl, 24, TimeUnit.HOURS);

        return trackingUrl;
    }
}
