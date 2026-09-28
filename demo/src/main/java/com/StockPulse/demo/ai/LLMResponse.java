package com.StockPulse.demo.ai;

public class LLMResponse {
    private String reasoning;
    private double confidence;

    public LLMResponse(String reasoning, double confidence) {
        this.reasoning = reasoning;
        this.confidence = confidence;
    }

    public String getReasoning() {
        return reasoning;
    }

    public void setReasoning(String reasoning) {
        this.reasoning = reasoning;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }
}