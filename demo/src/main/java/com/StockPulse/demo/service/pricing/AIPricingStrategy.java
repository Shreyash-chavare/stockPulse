package com.StockPulse.demo.service.pricing;

import com.StockPulse.demo.entity.Product;
import com.StockPulse.demo.entity.PricingSuggestion;
import com.StockPulse.demo.entity.SuggestionStatus;
import com.StockPulse.demo.entity.SuggestionSource;
import com.StockPulse.demo.ai.LLMService;
import com.StockPulse.demo.ai.LLMResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class AIPricingStrategy implements PricingStrategy {
    
    @Autowired
    private LLMService llmService;
    
    @Override
    public PricingSuggestion generatePricingSuggestion(Product product) {
        PricingSuggestion suggestion = new PricingSuggestion();
        suggestion.setProduct(product);
        suggestion.setCreatedAt(LocalDateTime.now());
        suggestion.setStatus(SuggestionStatus.PENDING);
        suggestion.setCreatedBy(SuggestionSource.AUTO);
        
        // Create context for the AI
        String productContext = createProductContext(product);
        
        // Call the LLM service
        LLMResponse llmResponse = llmService.callLLM(productContext);
        
        // Extract price recommendation from AI response
        BigDecimal suggestedPrice = calculateSuggestedPrice(product, llmResponse);
        
        suggestion.setSuggestedPrice(suggestedPrice);
        suggestion.setConfidenceScore(llmResponse.getConfidence());
        suggestion.setReasoning(llmResponse.getReasoning());
        
        return suggestion;
    }
    
    private String createProductContext(Product product) {
        StringBuilder context = new StringBuilder();
        context.append("Product Analysis Request:\n");
        context.append(String.format("SKU: %s\n", product.getSku()));
        context.append(String.format("Name: %s\n", product.getName()));
        context.append(String.format("Category: %s\n", product.getCategory()));
        context.append(String.format("Current Price: $%.2f\n", product.getCurrentPrice()));
        context.append(String.format("Current Stock Level: %d\n", product.getStockLevel()));
        context.append(String.format("Reorder Threshold: %d\n", product.getReorderThreshold()));
        context.append(String.format("Demand Velocity: %d\n", product.getDemandVelocity()));
        context.append(String.format("Product Status: %s\n", product.getStatus()));
        
        // Add contextual analysis
        Integer stockLevel = product.getStockLevel();
        Integer reorderThreshold = product.getReorderThreshold();
        Integer demandVelocity = product.getDemandVelocity();
        
        if (stockLevel < reorderThreshold * 0.5) {
            context.append("ALERT: Critically low stock levels detected.\n");
        } else if (stockLevel < reorderThreshold) {
            context.append("WARNING: Stock levels below reorder threshold.\n");
        }
        
        if (demandVelocity > 10) {
            context.append("ALERT: High demand velocity spike detected.\n");
        } else if (demandVelocity > 5) {
            context.append("NOTICE: Moderate demand velocity observed.\n");
        }
        
        context.append("\nPlease provide:\n");
        context.append("1. Recommended price adjustment with percentage\n");
        context.append("2. Confidence score (0.0-1.0)\n");
        context.append("3. Plain English reasoning for merchandising team\n");
        context.append("4. Market context considerations\n");
        
        return context.toString();
    }
    
    private BigDecimal calculateSuggestedPrice(Product product, LLMResponse llmResponse) {
        BigDecimal currentPrice = product.getCurrentPrice();
        
        // Extract price multiplier from reasoning (simplified approach)
        String reasoning = llmResponse.getReasoning().toLowerCase();
        double multiplier = 1.0;
        
        // This is a simplified extraction - in a real implementation, 
        // you might use regex or JSON parsing
        if (reasoning.contains("increase")) {
            if (reasoning.contains("25%")) {
                multiplier = 1.25;
            } else if (reasoning.contains("20%")) {
                multiplier = 1.20;
            } else if (reasoning.contains("15%")) {
                multiplier = 1.15;
            } else if (reasoning.contains("10%")) {
                multiplier = 1.10;
            } else if (reasoning.contains("5%")) {
                multiplier = 1.05;
            }
        } else if (reasoning.contains("decrease")) {
            if (reasoning.contains("25%")) {
                multiplier = 0.75;
            } else if (reasoning.contains("20%")) {
                multiplier = 0.80;
            } else if (reasoning.contains("15%")) {
                multiplier = 0.85;
            } else if (reasoning.contains("10%")) {
                multiplier = 0.90;
            } else if (reasoning.contains("5%")) {
                multiplier = 0.95;
            }
        }
        
        // Ensure reasonable bounds
        multiplier = Math.max(0.5, Math.min(2.0, multiplier));
        
        return currentPrice.multiply(BigDecimal.valueOf(multiplier));
    }
}