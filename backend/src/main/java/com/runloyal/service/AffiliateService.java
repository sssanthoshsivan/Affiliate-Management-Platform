package com.runloyal.service;

import com.runloyal.dto.AffiliateRequest;
import com.runloyal.dto.AffiliateResponse;
import com.runloyal.entity.Affiliate;
import com.runloyal.exception.ResourceNotFoundException;
import com.runloyal.repository.AffiliateLinkRepository;
import com.runloyal.repository.AffiliateRepository;
import com.runloyal.repository.ClickRepository;
import com.runloyal.repository.OrderRepository;
import com.runloyal.util.TenantValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AffiliateService {

    private final AffiliateRepository affiliateRepository;
    private final AffiliateLinkRepository affiliateLinkRepository;
    private final ClickRepository clickRepository;
    private final OrderRepository orderRepository;
    private final TenantValidator tenantValidator;

    public AffiliateResponse create(Long tenantId, AffiliateRequest request) {
        tenantValidator.validate(tenantId);
        Affiliate affiliate = Affiliate.builder()
                .tenantId(tenantId)
                .name(request.getName())
                .email(request.getEmail())
                .status(request.getStatus() != null ? request.getStatus() : "ACTIVE")
                .build();
        return toResponse(affiliateRepository.save(affiliate));
    }

    public List<AffiliateResponse> findByTenantId(Long tenantId) {
        tenantValidator.validate(tenantId);
        return affiliateRepository.findByTenantId(tenantId).stream()
                .map(this::toResponse)
                .toList();
    }

    public AffiliateResponse findById(Long tenantId, Long id) {
        tenantValidator.validate(tenantId);
        Affiliate affiliate = affiliateRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Affiliate", id));
        return toResponse(affiliate);
    }

    public AffiliateResponse update(Long tenantId, Long id, AffiliateRequest request) {
        tenantValidator.validate(tenantId);
        Affiliate affiliate = affiliateRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Affiliate", id));
        
        affiliate.setName(request.getName());
        affiliate.setEmail(request.getEmail());
        if (request.getStatus() != null) {
            affiliate.setStatus(request.getStatus());
        }
        
        return toResponse(affiliateRepository.save(affiliate));
    }

    @Transactional
    public void delete(Long tenantId, Long id) {
        tenantValidator.validate(tenantId);
        Affiliate affiliate = affiliateRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Affiliate", id));
        
        // Delete dependent records first to avoid foreign key violations
        affiliateLinkRepository.deleteByTenantIdAndAffiliateId(tenantId, id);
        clickRepository.deleteByTenantIdAndAffiliateId(tenantId, id);
        orderRepository.deleteByTenantIdAndAffiliateId(tenantId, id);
        
        affiliateRepository.delete(affiliate);
    }

    private AffiliateResponse toResponse(Affiliate affiliate) {
        return AffiliateResponse.builder()
                .id(affiliate.getId())
                .tenantId(affiliate.getTenantId())
                .name(affiliate.getName())
                .email(affiliate.getEmail())
                .status(affiliate.getStatus())
                .createdAt(affiliate.getCreatedAt())
                .build();
    }
}
