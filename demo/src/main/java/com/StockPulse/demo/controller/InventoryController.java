package com.StockPulse.demo.controller;

import com.StockPulse.demo.entity.Product;
import com.StockPulse.demo.service.ProductService;
import com.StockPulse.demo.service.InventoryMonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private InventoryMonitoringService inventoryMonitoringService;
    
    @PostMapping("/{productId}/stock")
    public ResponseEntity<Void> updateStock(@PathVariable String productId, @RequestParam Integer newStockLevel) {
        Optional<Product> productOpt = productService.getProductById(productId);
        if (productOpt.isPresent()) {
            productService.updateStockLevel(productId, newStockLevel);
            
            // Monitor inventory levels after update
            Product updatedProduct = productService.getProductById(productId).get();
            inventoryMonitoringService.checkInventoryLevels(updatedProduct);
            
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Simulate processing an order that reduces stock
    @PostMapping("/{productId}/orders")
    public ResponseEntity<Void> processOrder(@PathVariable String productId, @RequestParam Integer quantity) {
        Optional<Product> productOpt = productService.getProductById(productId);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            int newStockLevel = Math.max(0, product.getStockLevel() - quantity);
            productService.updateStockLevel(productId, newStockLevel);
            
            // Monitor inventory levels after order processing
            Product updatedProduct = productService.getProductById(productId).get();
            inventoryMonitoringService.checkInventoryLevels(updatedProduct);
            
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}