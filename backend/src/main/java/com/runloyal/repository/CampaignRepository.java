package com.runloyal.repository;

import com.runloyal.entity.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {

    List<Campaign> findByTenantId(Long tenantId);

    Optional<Campaign> findByIdAndTenantId(Long id, Long tenantId);
}
