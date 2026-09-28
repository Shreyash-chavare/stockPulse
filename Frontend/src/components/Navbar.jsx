import React from 'react';
import { Link, useLocation } from 'react-router-dom';

const Navbar = () => {
  const location = useLocation();

  const isActive = (path) => {
    return location.pathname === path;
  };

  return (
    <nav className="navbar">
      <div className="container">
        <div className="navbar-brand">
          <Link to="/">StockPulse Advisor</Link>
        </div>
        <ul className="navbar-nav">
          <li className={`nav-item ${isActive('/') || isActive('/dashboard') ? 'active' : ''}`}>
            <Link to="/dashboard">Dashboard</Link>
          </li>
          <li className={`nav-item ${isActive('/products') ? 'active' : ''}`}>
            <Link to="/products">Products</Link>
          </li>
          <li className={`nav-item ${isActive('/pricing-suggestions') ? 'active' : ''}`}>
            <Link to="/pricing-suggestions">Pricing</Link>
          </li>
          <li className={`nav-item ${isActive('/reorder-suggestions') ? 'active' : ''}`}>
            <Link to="/reorder-suggestions">Reordering</Link>
          </li>
        </ul>
      </div>
    </nav>
  );
};

export default Navbar;