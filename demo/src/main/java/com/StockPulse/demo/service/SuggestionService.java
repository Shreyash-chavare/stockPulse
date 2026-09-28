package com.StockPulse.demo.service;

import com.StockPulse.demo.entity.*;
import com.StockPulse.demo.repository.PricingSuggestionRepository;
import com.StockPulse.demo.repository.ReorderSuggestionRepository;
import com.StockPulse.demo.repository.ProductRepository; // Added import
import com.StockPulse.demo.service.pricing.RuleBasedPricingStrategy;
import com.StockPulse.demo.service.pricing.AIPricingStrategy;
import com.StockPulse.demo.service.pricing.PricingStrategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SuggestionService {
    
    @Autowired
    private PricingSuggestionRepository pricingSuggestionRepository;
    
    @Autowired
    private ReorderSuggestionRepository reorderSuggestionRepository;
    
    @Autowired
    private ProductRepository productRepository; // Added dependency
    
    @Autowired
    private RuleBasedPricingStrategy ruleBasedPricingStrategy;
    
    @Autowired
    private AIPricingStrategy aiPricingStrategy;
    
    @Value("${pricing.strategy:rule-based}")
    private String pricingStrategy;
    
    public PricingSuggestion generatePricingSuggestion(Product product, SuggestionSource source) {
        PricingStrategy strategy = "ai".equals(pricingStrategy) ? 
            aiPricingStrategy : ruleBasedPricingStrategy;
            
        PricingSuggestion suggestion = strategy.generatePricingSuggestion(product);
        suggestion.setCreatedBy(source);
        return pricingSuggestionRepository.save(suggestion);
    }
    
    public ReorderSuggestion generateReorderSuggestion(Product product, SuggestionSource source) {
        ReorderSuggestion suggestion = new ReorderSuggestion();
        suggestion.setProduct(product);
        suggestion.setCreatedAt(LocalDateTime.now());
        suggestion.setStatus(SuggestionStatus.PENDING);
        suggestion.setCreatedBy(source);
        
        Integer stockLevel = product.getStockLevel();
        Integer reorderThreshold = product.getReorderThreshold();
        Integer demandVelocity = product.getDemandVelocity();
        
        // Calculate reorder quantity based on demand velocity and safety stock
        int reorderQuantity = Math.max(
            reorderThreshold * 2, // Minimum reorder quantity
            demandVelocity * 7    // One week of demand
        );
        
        suggestion.setSuggestedQuantity(reorderQuantity);
        suggestion.setConfidenceScore(0.7); // Rule-based confidence
        suggestion.setReasoning(String.format(
            "Current stock (%d) is below reorder threshold (%d). Based on demand velocity (%d), recommending reorder of %d units.",
            stockLevel, reorderThreshold, demandVelocity, reorderQuantity));
            
        return reorderSuggestionRepository.save(suggestion);
    }
    
    public List<PricingSuggestion> getPendingPricingSuggestions() {
        return pricingSuggestionRepository.findByStatusOrderByCreatedAtAsc(SuggestionStatus.PENDING);
    }
    
    public List<ReorderSuggestion> getPendingReorderSuggestions() {
        return reorderSuggestionRepository.findByStatusOrderByCreatedAtAsc(SuggestionStatus.PENDING);
    }
    
    public void approvePricingSuggestion(Long suggestionId) {
        Optional<PricingSuggestion> suggestionOpt = pricingSuggestionRepository.findById(suggestionId);
        if (suggestionOpt.isPresent()) {
            PricingSuggestion suggestion = suggestionOpt.get();
            suggestion.setStatus(SuggestionStatus.APPROVED);
            suggestion.setReviewedAt(LocalDateTime.now());
            pricingSuggestionRepository.save(suggestion);
            
            // Update product price
            Product product = suggestion.getProduct();
            product.setCurrentPrice(suggestion.getSuggestedPrice());
            productRepository.save(product); // Actually save the product with updated price
        }
    }
    
    public void rejectPricingSuggestion(Long suggestionId) {
        Optional<PricingSuggestion> suggestionOpt = pricingSuggestionRepository.findById(suggestionId);
        if (suggestionOpt.isPresent()) {
            PricingSuggestion suggestion = suggestionOpt.get();
            suggestion.setStatus(SuggestionStatus.REJECTED);
            suggestion.setReviewedAt(LocalDateTime.now());
            pricingSuggestionRepository.save(suggestion);
        }
    }
    
    public void approveReorderSuggestion(Long suggestionId) {
        Optional<ReorderSuggestion> suggestionOpt = reorderSuggestionRepository.findById(suggestionId);
        if (suggestionOpt.isPresent()) {
            ReorderSuggestion suggestion = suggestionOpt.get();
            suggestion.setStatus(SuggestionStatus.APPROVED);
            suggestion.setReviewedAt(LocalDateTime.now());
            reorderSuggestionRepository.save(suggestion);
            
            // In a full implementation, this would trigger a purchase order
            // For now, we're just marking it as approved
        }
    }
    
    public void rejectReorderSuggestion(Long suggestionId) {
        Optional<ReorderSuggestion> suggestionOpt = reorderSuggestionRepository.findById(suggestionId);
        if (suggestionOpt.isPresent()) {
            ReorderSuggestion suggestion = suggestionOpt.get();
            suggestion.setStatus(SuggestionStatus.REJECTED);
            suggestion.setReviewedAt(LocalDateTime.now());
            reorderSuggestionRepository.save(suggestion);
        }
    }
}