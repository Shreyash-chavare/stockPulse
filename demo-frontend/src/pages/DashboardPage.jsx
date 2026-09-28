import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { productApi, suggestionApi } from '../services/api';
import { formatCurrency } from '../utils/formatting';

const DashboardPage = () => {
  const navigate = useNavigate();
  const [stats, setStats] = useState({
    totalProducts: 0,
    lowStockProducts: 0,
    outOfStockProducts: 0,
    pendingPricingSuggestions: 0,
    pendingReorderSuggestions: 0,
  });

  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchDashboardData = async () => {
      try {
        // Fetch all data in parallel
        const [productsRes, pricingSuggestionsRes, reorderSuggestionsRes] = await Promise.all([
          productApi.getAllProducts(),
          suggestionApi.getPricingSuggestions(),
          suggestionApi.getReorderSuggestions(),
        ]);

        const products = productsRes.data;
        const pricingSuggestions = pricingSuggestionsRes.data;
        const reorderSuggestions = reorderSuggestionsRes.data;

        // Calculate statistics
        const totalProducts = products.length;
        const lowStockProducts = products.filter(p => 
          p.stockLevel < p.reorderThreshold && p.stockLevel > 0
        ).length;
        const outOfStockProducts = products.filter(p => p.stockLevel <= 0).length;

        setStats({
          totalProducts,
          lowStockProducts,
          outOfStockProducts,
          pendingPricingSuggestions: pricingSuggestions.length,
          pendingReorderSuggestions: reorderSuggestions.length,
        });
      } catch (error) {
        console.error('Error fetching dashboard data:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchDashboardData();
  }, []);

  if (loading) {
    return (
      <div className="container">
        <h1>Dashboard</h1>
        <div className="card">
          <p>Loading dashboard data...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="container">
      <h1>Commerce Advisor Dashboard</h1>
      
      {/* Stats Cards */}
      <div className="stats-grid">
        <div className="card stat-card">
          <h3>Total Products</h3>
          <p className="stat-value">{stats.totalProducts}</p>
        </div>
        
        <div className="card stat-card">
          <h3>Low Stock Items</h3>
          <p className="stat-value" style={{color: '#ffc107'}}>{stats.lowStockProducts}</p>
        </div>
        
        <div className="card stat-card">
          <h3>Out of Stock</h3>
          <p className="stat-value" style={{color: '#dc3545'}}>{stats.outOfStockProducts}</p>
        </div>
        
        <div className="card stat-card">
          <h3>Pricing Suggestions</h3>
          <p className="stat-value" style={{color: '#007bff'}}>{stats.pendingPricingSuggestions}</p>
        </div>
        
        <div className="card stat-card">
          <h3>Reorder Suggestions</h3>
          <p className="stat-value" style={{color: '#28a745'}}>{stats.pendingReorderSuggestions}</p>
        </div>
      </div>

      {/* Quick Actions */}
      <div className="card">
        <h2>Quick Actions</h2>
        <div className="quick-actions">
          <button 
            className="btn btn-primary"
            onClick={() => navigate('/products')}
          >
            View All Products
          </button>
          <button 
            className="btn btn-primary"
            onClick={() => navigate('/pricing-suggestions')}
          >
            Review Pricing Suggestions
          </button>
          <button 
            className="btn btn-success"
            onClick={() => navigate('/reorder-suggestions')}
          >
            Review Reorder Suggestions
          </button>
        </div>
      </div>

      {/* Information */}
      <div className="alert alert-info">
        <h3>How It Works</h3>
        <p>
          StockPulse automatically monitors inventory levels and demand velocity to provide 
          intelligent pricing and reorder recommendations.
        </p>
        <ul>
          <li><strong>Auto-generated suggestions</strong> are triggered when stock levels cross thresholds</li>
          <li><strong>AI-powered analysis</strong> considers market conditions and product context</li>
          <li><strong>Manual suggestions</strong> can be generated for any product at any time</li>
        </ul>
      </div>
    </div>
  );
};

export default DashboardPage;