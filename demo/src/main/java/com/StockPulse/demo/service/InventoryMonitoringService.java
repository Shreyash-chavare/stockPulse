package com.StockPulse.demo.service;

import com.StockPulse.demo.entity.Product;
import com.StockPulse.demo.entity.SuggestionSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventoryMonitoringService {
    
    @Autowired
    private SuggestionService suggestionService;
    
    public void checkInventoryLevels(Product product) {
        // Check if stock is below threshold
        if (product.getStockLevel() < product.getReorderThreshold()) {
            // Generate reorder suggestion
            suggestionService.generateReorderSuggestion(product, SuggestionSource.AUTO);
            
            // If stock is critically low, also generate pricing suggestion
            if (product.getStockLevel() < product.getReorderThreshold() / 2) {
                suggestionService.generatePricingSuggestion(product, SuggestionSource.AUTO);
            }
        }
        
        // Check for demand spikes
        if (isDemandSpikeDetected(product)) {
            suggestionService.generatePricingSuggestion(product, SuggestionSource.AUTO);
            suggestionService.generateReorderSuggestion(product, SuggestionSource.AUTO);
        }
    }
    
    private boolean isDemandSpikeDetected(Product product) {
        // Simple heuristic: if demand velocity exceeds 3x normal threshold
        // In a real implementation, this would analyze historical data
        return product.getDemandVelocity() > 10; // Threshold for demonstration
    }
}