package com.StockPulse.demo.event;

import com.StockPulse.demo.entity.Product;
import org.springframework.context.ApplicationEvent;

public class StockLevelChangedEvent extends ApplicationEvent {
    private final Product product;
    private final int oldStockLevel;
    private final int newStockLevel;

    public StockLevelChangedEvent(Object source, Product product, int oldStockLevel, int newStockLevel) {
        super(source);
        this.product = product;
        this.oldStockLevel = oldStockLevel;
        this.newStockLevel = newStockLevel;
    }

    public Product getProduct() {
        return product;
    }

    public int getOldStockLevel() {
        return oldStockLevel;
    }

    public int getNewStockLevel() {
        return newStockLevel;
    }
}