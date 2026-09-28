package com.StockPulse.demo.controller;

import com.StockPulse.demo.entity.PricingSuggestion;
import com.StockPulse.demo.entity.ReorderSuggestion;
import com.StockPulse.demo.service.SuggestionService;
import com.StockPulse.demo.entity.Product;
import com.StockPulse.demo.service.ProductService;
import com.StockPulse.demo.entity.SuggestionSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/suggestions")
public class SuggestionController {
    
    @Autowired
    private SuggestionService suggestionService;
    
    @Autowired
    private ProductService productService;
    
    @GetMapping("/pricing/pending")
    public List<PricingSuggestion> getPendingPricingSuggestions() {
        return suggestionService.getPendingPricingSuggestions();
    }
    
    @GetMapping("/reorder/pending")
    public List<ReorderSuggestion> getPendingReorderSuggestions() {
        return suggestionService.getPendingReorderSuggestions();
    }
    
    @PostMapping("/pricing/{id}/approve")
    public ResponseEntity<Void> approvePricingSuggestion(@PathVariable Long id) {
        suggestionService.approvePricingSuggestion(id);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/pricing/{id}/reject")
    public ResponseEntity<Void> rejectPricingSuggestion(@PathVariable Long id) {
        suggestionService.rejectPricingSuggestion(id);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/reorder/{id}/approve")
    public ResponseEntity<Void> approveReorderSuggestion(@PathVariable Long id) {
        suggestionService.approveReorderSuggestion(id);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/reorder/{id}/reject")
    public ResponseEntity<Void> rejectReorderSuggestion(@PathVariable Long id) {
        suggestionService.rejectReorderSuggestion(id);
        return ResponseEntity.ok().build();
    }
    
    // Manual trigger for pricing suggestions
    @PostMapping("/pricing/generate/{productId}")
    public ResponseEntity<PricingSuggestion> generatePricingSuggestion(@PathVariable String productId) {
        Optional<Product> productOpt = productService.getProductById(productId);
        if (productOpt.isPresent()) {
            PricingSuggestion suggestion = suggestionService.generatePricingSuggestion(
                productOpt.get(), SuggestionSource.MANUAL);
            return ResponseEntity.ok(suggestion);
        }
        return ResponseEntity.notFound().build();
    }
    
    // Manual trigger for reorder suggestions
    @PostMapping("/reorder/generate/{productId}")
    public ResponseEntity<ReorderSuggestion> generateReorderSuggestion(@PathVariable String productId) {
        Optional<Product> productOpt = productService.getProductById(productId);
        if (productOpt.isPresent()) {
            ReorderSuggestion suggestion = suggestionService.generateReorderSuggestion(
                productOpt.get(), SuggestionSource.MANUAL);
            return ResponseEntity.ok(suggestion);
        }
        return ResponseEntity.notFound().build();
    }
}