import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { suggestionApi, productApi } from '../services/api';
import SuggestionCard from '../components/SuggestionCard';

const PricingSuggestionsPage = () => {
  const navigate = useNavigate();
  const [suggestions, setSuggestions] = useState([]);
  const [products, setProducts] = useState({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [refreshing, setRefreshing] = useState(false);

  useEffect(() => {
    fetchSuggestions();
  }, []);

  const fetchSuggestions = async () => {
    try {
      setLoading(true);
      const [suggestionsRes, productsRes] = await Promise.all([
        suggestionApi.getPricingSuggestions(),
        productApi.getAllProducts()
      ]);

      setSuggestions(suggestionsRes.data);

      // Create a product lookup map
      const productMap = {};
      productsRes.data.forEach(product => {
        productMap[product.id] = product;
      });
      setProducts(productMap);
    } catch (err) {
      setError('Failed to fetch pricing suggestions');
      console.error('Error fetching suggestions:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleRefresh = async () => {
    try {
      setRefreshing(true);
      const [suggestionsRes, productsRes] = await Promise.all([
        suggestionApi.getPricingSuggestions(),
        productApi.getAllProducts()
      ]);

      setSuggestions(suggestionsRes.data);

      // Create a product lookup map
      const productMap = {};
      productsRes.data.forEach(product => {
        productMap[product.id] = product;
      });
      setProducts(productMap);
    } catch (err) {
      setError('Failed to refresh pricing suggestions');
      console.error('Error refreshing suggestions:', err);
    } finally {
      setRefreshing(false);
    }
  };

  const handleApprove = async (suggestionId) => {
    try {
      await suggestionApi.approvePricingSuggestion(suggestionId);
      // Remove the approved suggestion from the list
      setSuggestions(prev => prev.filter(s => s.id !== suggestionId));
    } catch (err) {
      setError('Failed to approve suggestion');
      console.error('Error approving suggestion:', err);
    }
  };

  const handleReject = async (suggestionId) => {
    try {
      await suggestionApi.rejectPricingSuggestion(suggestionId);
      // Remove the rejected suggestion from the list
      setSuggestions(prev => prev.filter(s => s.id !== suggestionId));
    } catch (err) {
      setError('Failed to reject suggestion');
      console.error('Error rejecting suggestion:', err);
    }
  };

  const handleGenerate = async (productId) => {
    try {
      await suggestionApi.generatePricingSuggestion(productId);
      // Refresh the suggestions list
      await fetchSuggestions();
    } catch (err) {
      setError('Failed to generate suggestion');
      console.error('Error generating suggestion:', err);
    }
  };

  if (loading) {
    return (
      <div className="container">
        <h1>Pricing Suggestions</h1>
        <div className="card">
          <p>Loading pricing suggestions...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="container">
      <h1>Pricing Suggestions</h1>
      
      {error && (
        <div className="alert alert-error">
          {error}
        </div>
      )}

      <div className="card">
        <div className="suggestions-header">
          <h2>Pending Suggestions</h2>
          <div className="actions">
            <button 
              className={`btn btn-primary ${refreshing ? 'disabled' : ''}`}
              onClick={handleRefresh}
              disabled={refreshing}
            >
              {refreshing ? 'Refreshing...' : 'Refresh'}
            </button>
          </div>
        </div>

        {suggestions.length === 0 ? (
          <div className="alert alert-info">
            No pending pricing suggestions.
          </div>
        ) : (
          <div className="suggestion-grid">
            {suggestions.map((suggestion) => {
              // Merge product info if available
              const product = suggestion.product ? { ...suggestion.product, ...products[suggestion.product.id] } : products[suggestion.productId];
              
              return (
                <SuggestionCard
                  key={suggestion.id}
                  suggestion={{ ...suggestion, product }}
                  type="pricing"
                  onApprove={handleApprove}
                  onReject={handleReject}
                  onGenerate={handleGenerate}
                />
              );
            })}
          </div>
        )}
      </div>
    </div>
  );
};

export default PricingSuggestionsPage;