import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { productApi, inventoryApi, suggestionApi } from '../services/api';
import ProductCard from '../components/ProductCard';

const ProductsPage = () => {
  const navigate = useNavigate();
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [refreshing, setRefreshing] = useState(false);

  useEffect(() => {
    fetchProducts();
  }, []);

  const fetchProducts = async () => {
    try {
      setLoading(true);
      const response = await productApi.getAllProducts();
      setProducts(response.data);
    } catch (err) {
      setError('Failed to fetch products');
      console.error('Error fetching products:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleRefresh = async () => {
    try {
      setRefreshing(true);
      const response = await productApi.getAllProducts();
      setProducts(response.data);
    } catch (err) {
      setError('Failed to refresh products');
      console.error('Error refreshing products:', err);
    } finally {
      setRefreshing(false);
    }
  };

  const handleUpdateStock = async (productId) => {
    const newStockLevel = prompt('Enter new stock level:');
    if (newStockLevel !== null && !isNaN(newStockLevel)) {
      try {
        await inventoryApi.updateStock(productId, parseInt(newStockLevel));
        // Refresh the product list
        fetchProducts();
        alert('Stock level updated successfully!');
      } catch (err) {
        alert('Failed to update stock level');
        console.error('Error updating stock:', err);
      }
    }
  };

  const handleProcessOrder = async (productId) => {
    const quantity = prompt('Enter order quantity:');
    if (quantity !== null && !isNaN(quantity) && parseInt(quantity) > 0) {
      try {
        await inventoryApi.processOrder(productId, parseInt(quantity));
        // Refresh the product list
        fetchProducts();
        alert('Order processed successfully!');
      } catch (err) {
        alert('Failed to process order');
        console.error('Error processing order:', err);
      }
    }
  };

  const handleGeneratePricing = async (productId) => {
    try {
      const response = await suggestionApi.generatePricingSuggestion(productId);
      console.log('Pricing suggestion generated:', response.data);
      alert('Pricing suggestion generated successfully!');
      // Refresh products to show any changes
      fetchProducts();
    } catch (err) {
      console.error('Error generating pricing suggestion:', err);
      if (err.response) {
        alert(`Failed to generate pricing suggestion: ${err.response.status} - ${err.response.data?.message || 'Server error'}`);
      } else if (err.request) {
        alert('Failed to generate pricing suggestion: No response from server. Check if backend is running.');
      } else {
        alert(`Failed to generate pricing suggestion: ${err.message}`);
      }
    }
  };

  const handleGenerateReorder = async (productId) => {
    try {
      const response = await suggestionApi.generateReorderSuggestion(productId);
      console.log('Reorder suggestion generated:', response.data);
      alert('Reorder suggestion generated successfully!');
      // Refresh products to show any changes
      fetchProducts();
    } catch (err) {
      console.error('Error generating reorder suggestion:', err);
      if (err.response) {
        alert(`Failed to generate reorder suggestion: ${err.response.status} - ${err.response.data?.message || 'Server error'}`);
      } else if (err.request) {
        alert('Failed to generate reorder suggestion: No response from server. Check if backend is running.');
      } else {
        alert(`Failed to generate reorder suggestion: ${err.message}`);
      }
    }
  };

  if (loading) {
    return (
      <div className="container">
        <h1>Products</h1>
        <div className="card">
          <p>Loading products...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="container">
      <div className="card">
        <div className="product-header">
          <h1>Product Inventory</h1>
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

        {error && (
          <div className="alert alert-error">
            {error}
          </div>
        )}

        {/* Products Grid */}
        <div className="product-grid">
          {products.map((product) => (
            <ProductCard 
              key={product.id} 
              product={product}
              onUpdateStock={() => handleUpdateStock(product.id)}
              onProcessOrder={() => handleProcessOrder(product.id)}
              onGeneratePricing={() => handleGeneratePricing(product.id)}
              onGenerateReorder={() => handleGenerateReorder(product.id)}
            />
          ))}
        </div>
      </div>
    </div>
  );
};

export default ProductsPage;