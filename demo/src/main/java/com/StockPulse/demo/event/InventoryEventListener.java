package com.StockPulse.demo.event;

import com.StockPulse.demo.entity.SuggestionSource;
import com.StockPulse.demo.service.SuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class InventoryEventListener {
    
    @Autowired
    private SuggestionService suggestionService;
    
    @EventListener
    @Async
    public void handleStockLevelChanged(StockLevelChangedEvent event) {
        // Check if we need to generate suggestions based on new stock level
        checkAndGenerateSuggestions(event);
    }
    
    private void checkAndGenerateSuggestions(StockLevelChangedEvent event) {
        var product = event.getProduct();
        int stockLevel = product.getStockLevel();
        int reorderThreshold = product.getReorderThreshold();
        int demandVelocity = product.getDemandVelocity();
        
        // Generate reorder suggestion if stock is below threshold
        if (stockLevel < reorderThreshold) {
            suggestionService.generateReorderSuggestion(product, SuggestionSource.AUTO);
        }
        
        // Generate pricing suggestion if stock is critically low or demand is high
        if (stockLevel < reorderThreshold / 2 || demandVelocity > 10) {
            suggestionService.generatePricingSuggestion(product, SuggestionSource.AUTO);
        }
    }
}