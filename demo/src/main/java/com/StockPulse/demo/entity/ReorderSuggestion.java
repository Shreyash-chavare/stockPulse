package com.StockPulse.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "reorder_suggestions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReorderSuggestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;
    
    @Column(name = "suggested_quantity")
    private Integer suggestedQuantity;
    
    @Column(name = "confidence_score")
    private Double confidenceScore;
    
    @Column(name = "reasoning", columnDefinition = "TEXT")
    private String reasoning;
    
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private SuggestionStatus status;
    
    @Column(name = "created_by")
    @Enumerated(EnumType.STRING)
    private SuggestionSource createdBy;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;
}