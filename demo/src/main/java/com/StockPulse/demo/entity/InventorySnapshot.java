package com.StockPulse.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "inventory_snapshots")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventorySnapshot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;
    
    @Column(name = "timestamp")
    private LocalDateTime timestamp;
    
    @Column(name = "stock_level")
    private Integer stockLevel;
    
    @Column(name = "reserved_quantity")
    private Integer reservedQuantity;
}