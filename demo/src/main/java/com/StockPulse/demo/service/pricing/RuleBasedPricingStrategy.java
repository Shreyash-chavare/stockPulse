package com.StockPulse.demo.service.pricing;

import com.StockPulse.demo.entity.Product;
import com.StockPulse.demo.entity.PricingSuggestion;
import com.StockPulse.demo.entity.SuggestionStatus;
import com.StockPulse.demo.entity.SuggestionSource;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class RuleBasedPricingStrategy implements PricingStrategy {
    
    @Override
public PricingSuggestion generatePricingSuggestion(Product product) {
        PricingSuggestion suggestion = new PricingSuggestion();
        suggestion.setProduct(product);
        suggestion.setCreatedAt(LocalDateTime.now());
        suggestion.setStatus(SuggestionStatus.PENDING);
        suggestion.setConfidenceScore(0.8); // Rule-based confidence
        
        BigDecimal currentPrice = product.getCurrentPrice();
        Integer stockLevel = product.getStockLevel();
        Integer reorderThreshold = product.getReorderThreshold();
        Integer demandVelocity = product.getDemandVelocity();
        
        // Low stock -> increase price to protect inventory
        if (stockLevel < reorderThreshold) {
            BigDecimal newPrice = currentPrice.multiply(BigDecimal.valueOf(1.1)); // 10% increase
            suggestion.setSuggestedPrice(newPrice);
            suggestion.setReasoning(String.format(
                "Stock level (%d) is below reorder threshold (%d). Increasing price by 10%% to preserve inventory.",
                stockLevel, reorderThreshold));
        }
        // High demand -> increase price to maximize revenue
        else if (demandVelocity > 10) {
            BigDecimal newPrice = currentPrice.multiply(BigDecimal.valueOf(1.15)); // 15% increase
            suggestion.setSuggestedPrice(newPrice);
            suggestion.setReasoning(String.format(
                "High demand velocity (%d) detected. Increasing price by 15%% to maximize revenue.",
                demandVelocity));
        }
        // Normal conditions -> no change
        else {
            suggestion.setSuggestedPrice(currentPrice);
            suggestion.setReasoning("No significant changes in stock or demand. Maintaining current price.");
        }
        
        return suggestion;
    }
}