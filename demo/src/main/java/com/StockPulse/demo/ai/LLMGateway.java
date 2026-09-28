package com.StockPulse.demo.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Provider-specific HTTP for Gemini, Groq, Ollama.
 * Returns raw text — parsing, validation, fallback are yours.
 *
 * Configure in application.properties:
 *   llm.provider  = gemini
 *   llm.api-key   = ${LLM_API_KEY}
 *   llm.model     = gemini-1.5-flash
 *   llm.base-url  = https://generativelanguage.googleapis.com
 */
@Component
public class LLMGateway {

    @Value("${llm.provider}") private String provider;
    @Value("${llm.api-key:}") private String apiKey;
    @Value("${llm.model}") private String model;
    @Value("${llm.base-url}") private String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    public String callLLM(String prompt) {
        System.out.println("LLM Gateway called with provider: " + provider);
        System.out.println("API Key length: " + (apiKey != null ? apiKey.length() : "null"));
        System.out.println("API Key starts with: " + (apiKey != null && apiKey.length() > 10 ? apiKey.substring(0, 10) + "..." : "null"));
        
        return switch (provider.toLowerCase()) {
            case "gemini" -> callGemini(prompt);
            case "groq"   -> callOpenAICompatible(prompt, baseUrl + "/openai/v1/chat/completions");
            case "ollama" -> callOpenAICompatible(prompt, baseUrl + "/v1/chat/completions");
            default      -> throw new IllegalStateException("Unknown provider: " + provider);
        };
    }

    private String callGemini(String prompt) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new IllegalStateException("Gemini API key is required but not provided");
        }
        
        // Trim the API key to remove any whitespace
        String trimmedApiKey = apiKey.trim();
        System.out.println("Using trimmed API key length: " + trimmedApiKey.length());
        
        String url = String.format("%s/v1beta/models/%s:generateContent?key=%s", baseUrl, model, trimmedApiKey);
        System.out.println("Calling URL: " + url.substring(0, Math.min(url.length(), 100)) + "...");
        
        // Create the request body
        Map<String, Object> requestBody = new HashMap<>();
        Map<String, Object> content = new HashMap<>();
        content.put("parts", List.of(Map.of("text", prompt)));
        requestBody.put("contents", List.of(content));
        
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            
            // Extract the response text
            Map<String, Object> responseBody = response.getBody();
            if (responseBody != null && responseBody.containsKey("candidates")) {
                List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseBody.get("candidates");
                if (candidates != null && !candidates.isEmpty()) {
                    Map<String, Object> candidate = candidates.get(0);
                    if (candidate.containsKey("content")) {
                        Map<String, Object> contentMap = (Map<String, Object>) candidate.get("content");
                        if (contentMap.containsKey("parts")) {
                            List<Map<String, Object>> parts = (List<Map<String, Object>>) contentMap.get("parts");
                            if (parts != null && !parts.isEmpty()) {
                                return (String) parts.get(0).get("text");
                            }
                        }
                    }
                }
            }
            return "No response from Gemini API";
        } catch (HttpClientErrorException e) {
            System.err.println("HTTP Client Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            return "Error calling Gemini API: " + e.getStatusCode() + " " + e.getResponseBodyAsString();
        } catch (HttpServerErrorException e) {
            System.err.println("HTTP Server Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            return "Error calling Gemini API: " + e.getStatusCode() + " " + e.getResponseBodyAsString();
        } catch (Exception e) {
            System.err.println("Unexpected error calling Gemini API: " + e.getMessage());
            e.printStackTrace();
            return "Error calling Gemini API: " + e.getMessage();
        }
    }
    
    private String callOpenAICompatible(String prompt, String url) {
        // Simplified implementation for demo purposes
        return "OpenAI compatible API not fully implemented in this demo";
    }
}