package com.StockPulse.demo.service;

import com.StockPulse.demo.entity.Product;
import com.StockPulse.demo.entity.InventorySnapshot;
import com.StockPulse.demo.repository.ProductRepository;
import com.StockPulse.demo.repository.InventorySnapshotRepository;
import com.StockPulse.demo.event.StockLevelChangedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private InventorySnapshotRepository inventorySnapshotRepository;
    
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    public Optional<Product> getProductById(String id) {
        return productRepository.findById(id);
    }
    
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
    
    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }
    
    public void updateStockLevel(String productId, Integer newStockLevel) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            int oldStockLevel = product.getStockLevel();
            product.setStockLevel(newStockLevel);
            
            // Update status based on stock level
            if (newStockLevel <= 0) {
                product.setStatus(com.StockPulse.demo.entity.ProductStatus.OUT_OF_STOCK);
            } else if (product.getStatus() == com.StockPulse.demo.entity.ProductStatus.OUT_OF_STOCK) {
                product.setStatus(com.StockPulse.demo.entity.ProductStatus.ACTIVE);
            }
            
            productRepository.save(product);
            
            // Save inventory snapshot
            InventorySnapshot snapshot = new InventorySnapshot();
            snapshot.setProduct(product);
            snapshot.setTimestamp(LocalDateTime.now());
            snapshot.setStockLevel(newStockLevel);
            snapshot.setReservedQuantity(0);
            inventorySnapshotRepository.save(snapshot);
            
            // Publish event
            eventPublisher.publishEvent(new StockLevelChangedEvent(this, product, oldStockLevel, newStockLevel));
        }
    }
}