-- StockPulse Database Schema
-- Using H2-compatible SQL syntax

CREATE TABLE IF NOT EXISTS products (
    id VARCHAR(255) PRIMARY KEY,
    sku VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(100) NOT NULL,
    current_price DECIMAL(10,2) NOT NULL,
    stock_level INTEGER NOT NULL,
    reorder_threshold INTEGER NOT NULL,
    demand_velocity INTEGER NOT NULL,
    status VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS inventory_snapshots (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id VARCHAR(255) NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    stock_level INTEGER NOT NULL,
    reserved_quantity INTEGER NOT NULL,
    FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE TABLE IF NOT EXISTS pricing_suggestions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id VARCHAR(255) NOT NULL,
    suggested_price DECIMAL(10,2) NOT NULL,
    confidence_score DOUBLE NOT NULL,
    reasoning TEXT,
    status VARCHAR(50) NOT NULL,
    created_by VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    reviewed_at TIMESTAMP NULL,
    FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE TABLE IF NOT EXISTS reorder_suggestions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id VARCHAR(255) NOT NULL,
    suggested_quantity INTEGER NOT NULL,
    confidence_score DOUBLE NOT NULL,
    reasoning TEXT,
    status VARCHAR(50) NOT NULL,
    created_by VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    reviewed_at TIMESTAMP NULL,
    FOREIGN KEY (product_id) REFERENCES products(id)
);