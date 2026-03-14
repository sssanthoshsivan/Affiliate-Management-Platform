package com.runloyal.service;

import com.runloyal.dto.AffiliateRequest;
import com.runloyal.dto.AffiliateResponse;
import com.runloyal.entity.Affiliate;
import com.runloyal.exception.ResourceNotFoundException;
import com.runloyal.repository.AffiliateRepository;
import com.runloyal.util.TenantValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AffiliateService {

    private final AffiliateRepository affiliateRepository;
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
