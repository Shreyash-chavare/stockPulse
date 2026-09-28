# StockPulse Commerce Advisor - Demo Script

## Introduction (30 seconds)
"Welcome to StockPulse, an intelligent commerce advisor that automatically detects inventory issues and provides AI-powered recommendations for pricing and replenishment decisions.

Traditional manual approaches are slow, inconsistent, and fail silently. Our system solves this by creating an autonomous feedback loop that detects problems and brings AI-powered solutions directly to merchandisers for approval."

## Part 1: System Overview (1 minute)
### Show the Dashboard
- "This is our merchant console showing key inventory metrics"
- Point out: 8 total products, 2 with pending suggestions, 2 with low stock
- "The system is already monitoring inventory 24/7"

### Show the Products Page
- "Here we can see all our products with their current status"
- Highlight PRD-003 (T-Shirt): "This organic cotton t-shirt has stock level 8, which is below its reorder threshold of 15"
- Highlight PRD-008 (Hoodie): "This hoodie has stock level 11, just below threshold 12, with high demand velocity of 15 per day"

## Part 2: Automatic Detection (1 minute)
### Show Pending Suggestions
- Navigate to Pricing Suggestions: "The system has already detected the t-shirt's low inventory and generated a pricing suggestion"
- Show AUTO badge: "Notice this has an AUTO badge, meaning the system triggered it automatically"
- Read the AI reasoning: "The AI recommends increasing price by 10% due to low stock, with 92.3% confidence"

- Navigate to Reorder Suggestions: "It also generated a reorder suggestion for 30 units"
- Show confidence bar and reasoning

### Demonstrate Event-Driven System
- Go back to Products page
- Find the Hoodie (PRD-008)
- Click "Process Order" with quantity 2
- "This reduces stock from 11 to 9, which is now below threshold"
- "The system automatically detects this change and generates new suggestions"
- Refresh suggestions pages to show new AUTO-generated suggestions

## Part 3: Human-in-the-Loop Workflow (1 minute)
### Show Approval Process
- "All suggestions require human approval before taking effect"
- Go to Pricing Suggestions
- Click "Approve" on a suggestion
- "This updates the product price in our system"
- Navigate to Products page to show updated price

### Show Manual Override
- Find any product
- Click "Generate Pricing Suggestion"
- "Users can also manually request suggestions when needed"
- Show MANUAL badge on new suggestion
- "This creates a suggestion with the same AI intelligence but tagged as MANUAL"

## Part 4: AI Capabilities (1 minute)
### Show Intelligent Reasoning
- "Our AI considers multiple factors:"
  - Stock levels vs thresholds
  - Demand velocity trends
  - Product category dynamics
  - Market context
- Show different reasoning for different products:
  - Electronics: "premium pricing favored"
  - Apparel: "fashion trends justify dynamic pricing"
  - Home goods: "market is stable"

### Confidence Scoring
- "Every recommendation includes a confidence score"
- Show confidence bars ranging from low to high
- "Merchandisers can prioritize high-confidence suggestions"

## Conclusion (30 seconds)
"This system transforms inventory management from a reactive, manual process to an intelligent, autonomous advisor. Key benefits:

1. **Zero Manual Monitoring**: System detects issues 24/7
2. **AI-Powered Insights**: Intelligent recommendations with context
3. **Human Oversight**: All decisions approved by merchandisers
4. **Clear Attribution**: AUTO vs MANUAL tagging for accountability
5. **Extensible Design**: Ready for future features like competitor pricing

The agentic recommendation loop means merchandisers focus on strategic decisions rather than constantly monitoring dashboards."