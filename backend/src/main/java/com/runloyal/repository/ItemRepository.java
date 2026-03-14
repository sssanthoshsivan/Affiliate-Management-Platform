package com.runloyal.repository;

import com.runloyal.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByTenantId(Long tenantId);

    Optional<Item> findByIdAndTenantId(Long id, Long tenantId);
}
