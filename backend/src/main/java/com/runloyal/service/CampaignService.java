package com.runloyal.service;

import com.runloyal.dto.CampaignRequest;
import com.runloyal.dto.CampaignResponse;
import com.runloyal.entity.Campaign;
import com.runloyal.exception.ResourceNotFoundException;
import com.runloyal.repository.CampaignRepository;
import com.runloyal.util.TenantValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.runloyal.repository.AffiliateLinkRepository;
import com.runloyal.repository.ClickRepository;
import com.runloyal.repository.OrderRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CampaignService {

    private final CampaignRepository campaignRepository;
    private final AffiliateLinkRepository affiliateLinkRepository;
    private final ClickRepository clickRepository;
    private final OrderRepository orderRepository;
    private final TenantValidator tenantValidator;

    public CampaignResponse create(Long tenantId, CampaignRequest request) {
        tenantValidator.validate(tenantId);
        Campaign campaign = Campaign.builder()
                .tenantId(tenantId)
                .name(request.getName())
                .description(request.getDescription())
                .build();
        return toResponse(campaignRepository.save(campaign));
    }

    public List<CampaignResponse> findByTenantId(Long tenantId) {
        tenantValidator.validate(tenantId);
        return campaignRepository.findByTenantId(tenantId).stream()
                .map(this::toResponse)
                .toList();
    }

    public CampaignResponse findById(Long tenantId, Long id) {
        tenantValidator.validate(tenantId);
        Campaign campaign = campaignRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Campaign", id));
        return toResponse(campaign);
    }

    public CampaignResponse update(Long tenantId, Long id, CampaignRequest request) {
        tenantValidator.validate(tenantId);
        Campaign campaign = campaignRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Campaign", id));
        
        campaign.setName(request.getName());
        campaign.setDescription(request.getDescription());
        
        return toResponse(campaignRepository.save(campaign));
    }

    @Transactional
    public void delete(Long tenantId, Long id) {
        tenantValidator.validate(tenantId);
        Campaign campaign = campaignRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Campaign", id));
        
        // Delete dependent records
        affiliateLinkRepository.deleteByTenantIdAndCampaignId(tenantId, id);
        clickRepository.deleteByTenantIdAndCampaignId(tenantId, id);
        orderRepository.deleteByTenantIdAndCampaignId(tenantId, id);
        
        campaignRepository.delete(campaign);
    }

    private CampaignResponse toResponse(Campaign campaign) {
        return CampaignResponse.builder()
                .id(campaign.getId())
                .tenantId(campaign.getTenantId())
                .name(campaign.getName())
                .description(campaign.getDescription())
                .createdAt(campaign.getCreatedAt())
                .build();
    }
}
