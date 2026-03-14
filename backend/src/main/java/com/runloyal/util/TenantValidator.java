package com.runloyal.util;

import com.runloyal.entity.Tenant;
import com.runloyal.exception.ResourceNotFoundException;
import com.runloyal.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TenantValidator {

    private final TenantRepository tenantRepository;

    /**
     * Validates that a tenant exists. Returns the tenant if found.
     * @throws ResourceNotFoundException if tenant does not exist
     */
    public Tenant validateAndGet(Long tenantId) {
        return tenantRepository.findById(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Tenant", tenantId));
    }

    /**
     * Validates that a tenant exists (void — just checks).
     * @throws ResourceNotFoundException if tenant does not exist
     */
    public void validate(Long tenantId) {
        if (!tenantRepository.existsById(tenantId)) {
            throw new ResourceNotFoundException("Tenant", tenantId);
        }
    }
}
