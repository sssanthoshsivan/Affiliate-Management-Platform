package com.runloyal.service;

import com.runloyal.dto.TenantRequest;
import com.runloyal.dto.TenantResponse;
import com.runloyal.entity.Tenant;
import com.runloyal.exception.ResourceNotFoundException;
import com.runloyal.repository.AffiliateLinkRepository;
import com.runloyal.repository.ClickRepository;
import com.runloyal.repository.OrderRepository;
import com.runloyal.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TenantService {

    private final TenantRepository tenantRepository;
    private final AffiliateLinkRepository affiliateLinkRepository;
    private final ClickRepository clickRepository;
    private final OrderRepository orderRepository;

    public TenantResponse create(TenantRequest request) {
        Tenant tenant = Tenant.builder()
                .name(request.getName())
                .domain(request.getDomain())
                .status(request.getStatus() != null ? request.getStatus() : "ACTIVE")
                .commissionRate(request.getCommissionRate() != null
                        ? request.getCommissionRate()
                        : new BigDecimal("10.00"))
                .build();
        return toResponse(tenantRepository.save(tenant));
    }

    public List<TenantResponse> findAll() {
        return tenantRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public TenantResponse findById(Long id) {
        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant", id));
        return toResponse(tenant);
    }

    public TenantResponse updateCommissionRate(Long id, BigDecimal commissionRate) {
        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant", id));
        tenant.setCommissionRate(commissionRate);
        return toResponse(tenantRepository.save(tenant));
    }

    public TenantResponse update(Long id, TenantRequest request) {
        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant", id));
        
        tenant.setName(request.getName());
        tenant.setDomain(request.getDomain());
        if (request.getStatus() != null) {
            tenant.setStatus(request.getStatus());
        }
        if (request.getCommissionRate() != null) {
            tenant.setCommissionRate(request.getCommissionRate());
        }
        
        return toResponse(tenantRepository.save(tenant));
    }

    @Transactional
    public void delete(Long id) {
        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant", id));
        
        // Cascading deletion
        affiliateLinkRepository.deleteByTenantId(id);
        clickRepository.deleteByTenantId(id);
        orderRepository.deleteByTenantId(id);
        
        tenantRepository.delete(tenant);
    }

    private TenantResponse toResponse(Tenant tenant) {
        return TenantResponse.builder()
                .id(tenant.getId())
                .name(tenant.getName())
                .domain(tenant.getDomain())
                .status(tenant.getStatus())
                .commissionRate(tenant.getCommissionRate())
                .createdAt(tenant.getCreatedAt())
                .build();
    }
}
