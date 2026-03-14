package com.runloyal.repository;

import com.runloyal.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByTenantId(Long tenantId);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.tenantId = :tenantId " +
           "AND (:affiliateId IS NULL OR o.affiliateId = :affiliateId) " +
           "AND (:campaignId IS NULL OR o.campaignId = :campaignId) " +
           "AND (:itemId IS NULL OR o.itemId = :itemId) " +
           "AND (:from IS NULL OR o.createdAt >= :from) " +
           "AND (:to IS NULL OR o.createdAt <= :to)")
    Long countFiltered(@Param("tenantId") Long tenantId,
                       @Param("affiliateId") Long affiliateId,
                       @Param("campaignId") Long campaignId,
                       @Param("itemId") Long itemId,
                       @Param("from") LocalDateTime from,
                       @Param("to") LocalDateTime to);

    @Query("SELECT COALESCE(SUM(o.orderValue), 0) FROM Order o WHERE o.tenantId = :tenantId " +
           "AND (:affiliateId IS NULL OR o.affiliateId = :affiliateId) " +
           "AND (:campaignId IS NULL OR o.campaignId = :campaignId) " +
           "AND (:itemId IS NULL OR o.itemId = :itemId) " +
           "AND (:from IS NULL OR o.createdAt >= :from) " +
           "AND (:to IS NULL OR o.createdAt <= :to)")
    BigDecimal sumRevenue(@Param("tenantId") Long tenantId,
                          @Param("affiliateId") Long affiliateId,
                          @Param("campaignId") Long campaignId,
                          @Param("itemId") Long itemId,
                          @Param("from") LocalDateTime from,
                          @Param("to") LocalDateTime to);

    @Query("SELECT COALESCE(SUM(o.commission), 0) FROM Order o WHERE o.tenantId = :tenantId " +
           "AND (:affiliateId IS NULL OR o.affiliateId = :affiliateId) " +
           "AND (:campaignId IS NULL OR o.campaignId = :campaignId) " +
           "AND (:itemId IS NULL OR o.itemId = :itemId) " +
           "AND (:from IS NULL OR o.createdAt >= :from) " +
           "AND (:to IS NULL OR o.createdAt <= :to)")
    BigDecimal sumCommission(@Param("tenantId") Long tenantId,
                             @Param("affiliateId") Long affiliateId,
                             @Param("campaignId") Long campaignId,
                             @Param("itemId") Long itemId,
                             @Param("from") LocalDateTime from,
                             @Param("to") LocalDateTime to);

    @Query("SELECT o.affiliateId, COUNT(o), COALESCE(SUM(o.orderValue), 0), COALESCE(SUM(o.commission), 0) " +
           "FROM Order o WHERE o.tenantId = :tenantId " +
           "AND (:from IS NULL OR o.createdAt >= :from) " +
           "AND (:to IS NULL OR o.createdAt <= :to) " +
           "GROUP BY o.affiliateId")
    List<Object[]> aggregateByAffiliate(@Param("tenantId") Long tenantId,
                                        @Param("from") LocalDateTime from,
                                        @Param("to") LocalDateTime to);
}
