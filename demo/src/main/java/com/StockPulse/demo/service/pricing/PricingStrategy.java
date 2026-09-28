package com.StockPulse.demo.service.pricing;

import com.StockPulse.demo.entity.Product;
import com.StockPulse.demo.entity.PricingSuggestion;

public interface PricingStrategy {
    PricingSuggestion generatePricingSuggestion(Product product);
}