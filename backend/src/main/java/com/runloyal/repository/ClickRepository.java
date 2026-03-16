package com.runloyal.repository;

import com.runloyal.entity.Click;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClickRepository extends JpaRepository<Click, Long> {

    List<Click> findByTenantId(Long tenantId);

    @Query("SELECT COUNT(c) FROM Click c WHERE c.tenantId = :tenantId " +
           "AND (:affiliateId IS NULL OR c.affiliateId = :affiliateId) " +
           "AND (:campaignId IS NULL OR c.campaignId = :campaignId) " +
           "AND (:itemId IS NULL OR c.itemId = :itemId) " +
           "AND (cast(:from as timestamp) IS NULL OR c.createdAt >= :from) " +
           "AND (cast(:to as timestamp) IS NULL OR c.createdAt <= :to)")
    Long countFiltered(@Param("tenantId") Long tenantId,
                       @Param("affiliateId") Long affiliateId,
                       @Param("campaignId") Long campaignId,
                       @Param("itemId") Long itemId,
                       @Param("from") LocalDateTime from,
                       @Param("to") LocalDateTime to);

    @Query("SELECT c.affiliateId, COUNT(c) FROM Click c WHERE c.tenantId = :tenantId " +
           "AND (cast(:from as timestamp) IS NULL OR c.createdAt >= :from) " +
           "AND (cast(:to as timestamp) IS NULL OR c.createdAt <= :to) " +
           "GROUP BY c.affiliateId")
    List<Object[]> countByAffiliate(@Param("tenantId") Long tenantId,
                                    @Param("from") LocalDateTime from,
                                    @Param("to") LocalDateTime to);
}
