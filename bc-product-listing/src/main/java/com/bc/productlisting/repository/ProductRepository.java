package com.bc.productlisting.repository;

import com.bc.productlisting.repository.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByMerchantIdAndInventoryGreaterThan(String merchantId, Integer minInventory, Pageable pageable);

}
