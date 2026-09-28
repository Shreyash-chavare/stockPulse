import axios from 'axios';

const API_BASE_URL = '/api';

// Create axios instance with default config
const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 30000, // Increased timeout to 30 seconds
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add request interceptor for logging
apiClient.interceptors.request.use(
  (config) => {
    console.log('API Request:', config.method?.toUpperCase(), config.url);
    return config;
  },
  (error) => {
    console.error('API Request Error:', error);
    return Promise.reject(error);
  }
);

// Add response interceptor for logging
apiClient.interceptors.response.use(
  (response) => {
    console.log('API Response:', response.status, response.config.url);
    return response;
  },
  (error) => {
    console.error('API Response Error:', error.response?.status, error.response?.data || error.message);
    return Promise.reject(error);
  }
);

// Products API
export const productApi = {
  getAllProducts: () => apiClient.get('/products'),
  getProductById: (id) => apiClient.get(`/products/${id}`),
  createProduct: (product) => apiClient.post('/products', product),
  updateProduct: (id, product) => apiClient.put(`/products/${id}`, product),
  deleteProduct: (id) => apiClient.delete(`/products/${id}`),
};

// Inventory API
export const inventoryApi = {
  updateStock: (productId, newStockLevel) => 
    apiClient.post(`/inventory/${productId}/stock?newStockLevel=${newStockLevel}`),
  processOrder: (productId, quantity) => 
    apiClient.post(`/inventory/${productId}/orders?quantity=${quantity}`),
};

// Suggestions API
export const suggestionApi = {
  getPricingSuggestions: () => apiClient.get('/suggestions/pricing/pending'),
  getReorderSuggestions: () => apiClient.get('/suggestions/reorder/pending'),
  approvePricingSuggestion: (id) => apiClient.post(`/suggestions/pricing/${id}/approve`),
  rejectPricingSuggestion: (id) => apiClient.post(`/suggestions/pricing/${id}/reject`),
  approveReorderSuggestion: (id) => apiClient.post(`/suggestions/reorder/${id}/approve`),
  rejectReorderSuggestion: (id) => apiClient.post(`/suggestions/reorder/${id}/reject`),
  generatePricingSuggestion: (productId) => 
    apiClient.post(`/suggestions/pricing/generate/${productId}`),
  generateReorderSuggestion: (productId) => 
    apiClient.post(`/suggestions/reorder/generate/${productId}`),
};

export default apiClient;