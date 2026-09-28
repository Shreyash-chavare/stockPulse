package com.StockPulse.demo.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Random;

@Service
public class LLMService {
    
    @Value("${llm.simulation.enabled:true}")
    private boolean simulationEnabled;
    
    @Autowired(required = false)
    private LLMGateway llmGateway;
    
    private final Random random = new Random();
    
    public LLMResponse callLLM(String prompt) {
        if (simulationEnabled || llmGateway == null) {
            return generateSimulatedResponse(prompt);
        }
        
        try {
            String response = llmGateway.callLLM(prompt);
            // For simplicity, we'll use a fixed confidence score
            // In a real implementation, you might extract confidence from the response
            return new LLMResponse(response, 0.95);
        } catch (Exception e) {
            // Fallback to simulation if real API fails
            return generateSimulatedResponse(prompt);
        }
    }
    
    private LLMResponse generateSimulatedResponse(String prompt) {
        // Add a small delay to simulate API call
        try {
            Thread.sleep(100 + random.nextInt(200));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        String category = extractCategoryFromPrompt(prompt);
        double confidence = 0.85 + (random.nextDouble() * 0.15);
        double multiplier = analyzePromptAndGenerateMultiplier(prompt, category);
        String reasoning = generateSimulatedReasoning(prompt, category, multiplier, confidence);
        return new LLMResponse(reasoning, confidence);
    }
    
    private String extractCategoryFromPrompt(String prompt) {
        if (prompt.contains("ELECTRONICS")) return "ELECTRONICS";
        if (prompt.contains("APPAREL")) return "APPAREL";
        if (prompt.contains("HOME")) return "HOME";
        return "GENERAL";
    }
    
    private double analyzePromptAndGenerateMultiplier(String prompt, String category) {
        double multiplier = 1.0;
        
        if (prompt.contains("critically low")) {
            multiplier += 0.15 + (random.nextDouble() * 0.10);
        } else if (prompt.contains("low stock")) {
            multiplier += 0.10 + (random.nextDouble() * 0.05);
        }
        
        if (prompt.contains("high demand")) {
            multiplier += 0.10 + (random.nextDouble() * 0.10);
        } else if (prompt.contains("moderate demand")) {
            multiplier += 0.05 + (random.nextDouble() * 0.05);
        }
        
        switch (category) {
            case "ELECTRONICS":
                if (prompt.contains("high demand")) {
                    multiplier += 0.05;
                }
                break;
            case "APPAREL":
                if (prompt.contains("seasonal")) {
                    multiplier += 0.08;
                }
                break;
            case "HOME":
                multiplier *= 0.95;
                break;
        }
        
        multiplier = Math.max(0.7, Math.min(1.5, multiplier));
        return multiplier;
    }
    
    private String generateSimulatedReasoning(String prompt, String category, double multiplier, double confidence) {
        StringBuilder reasoning = new StringBuilder();
        reasoning.append(String.format("[SIMULATED AI] Confidence: %.1f%% - ", confidence * 100));
        
        if (multiplier > 1.0) {
            double increasePercent = (multiplier - 1.0) * 100;
            reasoning.append(String.format("Increase price by %.1f%% ", increasePercent));
        } else if (multiplier < 1.0) {
            double decreasePercent = (1.0 - multiplier) * 100;
            reasoning.append(String.format("Decrease price by %.1f%% ", decreasePercent));
        } else {
            reasoning.append("Maintain current price ");
        }
        
        if (prompt.contains("critically low")) {
            reasoning.append("due to critically low inventory. ");
        } else if (prompt.contains("low stock")) {
            reasoning.append("due to low inventory levels. ");
        }
        
        if (prompt.contains("high demand")) {
            reasoning.append("High demand velocity supports pricing. ");
        }
        
        switch (category) {
            case "ELECTRONICS":
                reasoning.append("Electronics market favors premium pricing. ");
                break;
            case "APPAREL":
                reasoning.append("Fashion trends justify dynamic pricing. ");
                break;
            case "HOME":
                reasoning.append("Home goods market is stable. ");
                break;
        }
        
        reasoning.append("Recommendation based on market analysis.");
        return reasoning.toString();
    }
}