// Format currency
export const formatCurrency = (amount) => {
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD',
  }).format(amount);
};

// Format date
export const formatDate = (dateString) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  return date.toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  });
};

// Format confidence score as percentage
export const formatConfidence = (score) => {
  return `${(score * 100).toFixed(1)}%`;
};

// Format status for display
export const formatStatus = (status) => {
  return status.replace(/_/g, ' ').replace(/\w\S*/g, (txt) => {
    return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();
  });
};

// Format source for display
export const formatSource = (source) => {
  return source === 'AUTO' ? 'Auto-Generated' : 'Manual';
};

// Get status badge class
export const getStatusClass = (status) => {
  switch (status) {
    case 'ACTIVE':
      return 'status-active';
    case 'PRICE_REVIEW_PENDING':
      return 'status-low-stock';
    case 'OUT_OF_STOCK':
      return 'status-out-of-stock';
    default:
      return '';
  }
};

// Get suggestion source badge class
export const getSourceBadgeClass = (source) => {
  return source === 'AUTO' ? 'badge-auto' : 'badge-manual';
};

// Calculate stock level percentage
export const getStockLevelPercentage = (stock, threshold) => {
  if (threshold === 0) return 100;
  return Math.min(100, (stock / threshold) * 100);
};

// Get stock level class
export const getStockLevelClass = (stock, threshold) => {
  if (stock <= 0) return 'status-out-of-stock';
  if (stock < threshold) return 'status-low-stock';
  return 'status-active';
};