# StockPulse - Reactive Commerce Advisor

StockPulse is an intelligent commerce advisory system that automatically detects inventory issues and provides AI-powered recommendations for pricing and replenishment decisions.

## Features

- **Real-time Inventory Monitoring**: Automatically detects when stock levels drop below thresholds
- **AI-Powered Recommendations**: Uses LLMs to generate intelligent pricing and reorder suggestions
- **Event-Driven Architecture**: Responds immediately to inventory changes without manual intervention
- **Human-in-the-Loop Approval**: All suggestions require merchandiser approval before implementation
- **AUTO vs MANUAL Distinction**: Clear differentiation between system-generated and user-requested suggestions
- **Comprehensive Dashboard**: Real-time visibility into product status and pending actions

## Problem Solved

Traditional e-commerce systems rely on manual monitoring and decision-making for pricing and inventory management. This leads to:
- Slow response to inventory issues
- Inconsistent pricing decisions
- Missed opportunities during high-demand periods
- Manual effort that scales poorly

StockPulse solves these problems by creating an autonomous recommendation loop that detects inventory signals, uses AI to suggest optimal actions, and presents them for approval.

## Technology Stack

- **Backend**: Spring Boot 3.1.1 (Java 21)
- **Frontend**: React 18 with custom CSS styling
- **Database**: H2 in-memory database for development
- **AI Integration**: Simulated LLM service with potential for real LLM providers (Gemini, OpenAI, etc.)
- **Architecture**: Event-driven microservices pattern

## Key Components

1. **Domain Model**: Product, InventorySnapshot, PricingSuggestion, ReorderSuggestion with explicit state machines
2. **Commerce Engine**: Pluggable strategy interface with rule-based and AI-powered implementations
3. **AI Commerce Advisor**: LLM examines product context, stock level, demand velocity, and trigger situation
4. **Agentic Recommendation Loop**: Automatic suggestion generation when inventory thresholds are crossed
5. **Merchandising Console**: React UI showing pending suggestions with AI reasoning and approval controls

## Getting Started

### Prerequisites
- Java 21+
- Node.js 16+
- Maven 3.8+

### Backend Setup
```bash
cd demo
mvn spring-boot:run
```

### Frontend Setup
```bash
cd demo-frontend
npm install
npm start
```

### Access the Application
- Frontend: http://localhost:3000
- Backend API: http://localhost:8080
- H2 Console: http://localhost:8080/h2-console (JDBC URL: jdbc:h2:mem:testdb)

## Demo Paths

1. **Low Inventory Scenario**: Process orders on PRD-003 (T-Shirt) until stock < 15 → auto pricing + reorder suggestions
2. **Demand Spike Scenario**: Process multiple orders on PRD-008 (Hoodie) → velocity crosses threshold → spike-triggered suggestions

## Architecture Decision Records

See [ADR.md](ADR.md) for detailed architectural decisions.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.