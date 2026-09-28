import React from 'react';
import { formatCurrency, formatStatus, formatSource } from '../utils/formatting';

const SuggestionCard = ({ suggestion, type, onApprove, onReject, onGenerate }) => {
  const isPricing = type === 'pricing';
  
  const getSourceClass = (source) => {
    return source === 'AUTO' ? 'badge-auto' : 'badge-manual';
  };

  const getConfidenceClass = (confidence) => {
    if (confidence >= 0.8) return 'confidence-high';
    if (confidence >= 0.6) return 'confidence-medium';
    return 'confidence-low';
  };

  const getConfidenceText = (confidence) => {
    if (confidence >= 0.8) return 'High';
    if (confidence >= 0.6) return 'Medium';
    return 'Low';
  };

  const getConfidencePercentage = (confidence) => {
    return Math.round(confidence * 100);
  };

  const getConfidenceBarWidth = (confidence) => {
    return `${confidence * 100}%`;
  };

  return (
    <div className={`card suggestion-card ${isPricing ? 'pricing' : 'reorder'}`}>
      <div className="suggestion-header">
        <div className="product-info">
          <h3 className="product-name">
            {suggestion.product?.name || 'Product Name'}
          </h3>
          <p className="product-sku">{suggestion.product?.sku}</p>
        </div>
        <div className="badges">
          <span className={`badge ${getSourceClass(suggestion.createdBy)}`}>
            {formatSource(suggestion.createdBy)}
          </span>
          <span className={`badge confidence-badge ${getConfidenceClass(suggestion.confidenceScore)}`}>
            {getConfidenceText(suggestion.confidenceScore)} ({getConfidencePercentage(suggestion.confidenceScore)}%)
          </span>
        </div>
      </div>

      <p className="product-category">{suggestion.product?.category}</p>

      <div className="suggestion-details">
        {isPricing ? (
          <div className="pricing-details">
            <div className="price-row">
              <span className="label">Current Price:</span>
              <span className="value">{formatCurrency(suggestion.product?.currentPrice || 0)}</span>
            </div>
            <div className="price-row suggested">
              <span className="label">Suggested Price:</span>
              <span className="value suggested-price">{formatCurrency(suggestion.suggestedPrice)}</span>
            </div>
          </div>
        ) : (
          <div className="reorder-details">
            <div className="stock-row">
              <span className="label">Current Stock:</span>
              <span className="value">{suggestion.product?.stockLevel || 0}</span>
            </div>
            <div className="stock-row suggested">
              <span className="label">Suggested Quantity:</span>
              <span className="value suggested-quantity">{suggestion.suggestedQuantity}</span>
            </div>
          </div>
        )}
      </div>

      <div className="reasoning-section">
        <h4 className="reasoning-title">
          {isPricing ? 'AI Pricing Reasoning:' : 'Reorder Reasoning:'}
        </h4>
        <p className="reasoning-text">{suggestion.reasoning}</p>
        
        <div className="confidence-bar">
          <div 
            className={`confidence-level ${getConfidenceClass(suggestion.confidenceScore)}`} 
            style={{ width: getConfidenceBarWidth(suggestion.confidenceScore) }}
          ></div>
        </div>
      </div>

      <div className="suggestion-footer">
        <span className="creation-date">
          Created: {new Date(suggestion.createdAt).toLocaleString()}
        </span>
        <div className="actions">
          {onGenerate && (
            <button 
              onClick={() => onGenerate(suggestion.product?.id)}
              className="btn btn-secondary"
            >
              Regenerate
            </button>
          )}
          {onReject && (
            <button 
              onClick={() => onReject(suggestion.id)}
              className="btn btn-danger"
            >
              Reject
            </button>
          )}
          {onApprove && (
            <button 
              onClick={() => onApprove(suggestion.id)}
              className="btn btn-success"
            >
              Approve
            </button>
          )}
        </div>
      </div>
    </div>
  );
};

export default SuggestionCard;