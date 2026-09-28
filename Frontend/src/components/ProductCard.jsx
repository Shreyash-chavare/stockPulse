import React from 'react';
import { formatCurrency, formatStatus } from '../utils/formatting';

const ProductCard = ({ product, onUpdateStock, onProcessOrder, onGeneratePricing, onGenerateReorder }) => {
  const getStatusClass = (status) => {
    switch (status) {
      case 'ACTIVE': return 'status-active';
      case 'PRICE_REVIEW_PENDING': return 'status-low-stock';
      case 'OUT_OF_STOCK': return 'status-out-of-stock';
      default: return '';
    }
  };

  const getStockLevelClass = (stockLevel, threshold) => {
    if (stockLevel === 0) return 'stock-critical';
    if (stockLevel < threshold) return 'stock-low';
    return 'stock-normal';
  };

  const getStockBarWidth = (stockLevel, threshold) => {
    const percentage = Math.min(100, (stockLevel / threshold) * 100);
    return `${percentage}%`;
  };

  return (
    <div className="product-card">
      <div className="product-header">
        <h3 className="product-title">{product.name}</h3>
        <span className={`badge ${getStatusClass(product.status)}`}>
          {formatStatus(product.status)}
        </span>
      </div>
      
      <p className="product-sku">{product.sku}</p>
      <p className="product-category">{product.category}</p>
      
      <div className="product-price-stock">
        <span className="product-price">{formatCurrency(product.currentPrice)}</span>
        <div className="stock-info">
          <div className={`stock-indicator ${getStockLevelClass(product.stockLevel, product.reorderThreshold)}`}></div>
          <span className="stock-text">{product.stockLevel} in stock</span>
        </div>
      </div>
      
      <div className="demand-velocity">
        <div className="detail-item">
          <span className="detail-label">Demand Velocity:</span>
          <span className="detail-value">{product.demandVelocity}/day</span>
        </div>
        <div className="stock-bar">
          <div 
            className={`stock-level ${getStockLevelClass(product.stockLevel, product.reorderThreshold)}`} 
            style={{ width: getStockBarWidth(product.stockLevel, product.reorderThreshold) }}
          ></div>
        </div>
      </div>
      
      <div className="threshold-info">
        Threshold: {product.reorderThreshold}
      </div>
      
      <div className="product-actions">
        <div className="action-group">
          <button 
            className="btn btn-secondary btn-small"
            onClick={() => onUpdateStock(product.id)}
          >
            Update Stock
          </button>
          <button 
            className="btn btn-secondary btn-small"
            onClick={() => onProcessOrder(product.id)}
          >
            Process Order
          </button>
        </div>
        <div className="action-group">
          <button 
            className="btn btn-primary btn-small"
            onClick={() => onGeneratePricing(product.id)}
          >
            Generate Pricing Suggestion
          </button>
          <button 
            className="btn btn-primary btn-small"
            onClick={() => onGenerateReorder(product.id)}
          >
            Generate Reorder Suggestion
          </button>
        </div>
      </div>
    </div>
  );
};

export default ProductCard;