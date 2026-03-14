package com.runloyal.repository;

import com.runloyal.entity.Affiliate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AffiliateRepository extends JpaRepository<Affiliate, Long> {

    List<Affiliate> findByTenantId(Long tenantId);

    Optional<Affiliate> findByIdAndTenantId(Long id, Long tenantId);
}
