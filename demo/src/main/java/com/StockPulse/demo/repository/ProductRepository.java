package com.StockPulse.demo.repository;

import com.StockPulse.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findByStockLevelLessThan(Integer threshold);
    List<Product> findByStockLevelLessThanAndDemandVelocityGreaterThan(Integer stockThreshold, Integer velocityThreshold);
    Optional<Product> findBySku(String sku);
}