import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Navbar from './components/Navbar';
import DashboardPage from './pages/DashboardPage';
import ProductsPage from './pages/ProductsPage';
import PricingSuggestionsPage from './pages/PricingSuggestionsPage';
import ReorderSuggestionsPage from './pages/ReorderSuggestionsPage';
import './App.css';

function App() {
  return (
    <Router>
      <div className="App">
        <Navbar />
        <main className="main-content">
          <Routes>
            <Route path="/" element={<DashboardPage />} />
            <Route path="/dashboard" element={<DashboardPage />} />
            <Route path="/products" element={<ProductsPage />} />
            <Route path="/pricing-suggestions" element={<PricingSuggestionsPage />} />
            <Route path="/reorder-suggestions" element={<ReorderSuggestionsPage />} />
          </Routes>
        </main>
      </div>
    </Router>
  );
}

export default App;