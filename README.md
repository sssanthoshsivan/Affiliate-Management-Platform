# RunLoyal — Multi-Tenant Affiliate Management Platform

RunLoyal is a high-performance, multi-tenant affiliate management platform designed for rapid scaling. It enables multiple clients (tenants) to manage their referral partners, marketing campaigns, and conversion tracking within a single, isolated environment.

## 🚀 Key Features

*   **Multi-Tenancy**: Shared database architecture with logical isolation via `tenant_id`. Enforced at the service layer for guaranteed data privacy.
*   **High-Speed Tracking**: Redis-based async click tracking. The tracking endpoint returns in under 5ms, offloading weight to a background batch processor.
*   **Dynamic Commissioning**: Tenant-specific commission rates (default 10%) automatically applied to every attributed order.
*   **Analytics Dashboard**: Real-time performance metrics (clicks, orders, revenue, commission) with multi-filter support and 5-minute Redis caching.
*   **Link Generator**: UUID-based short links for clean tracking URLs.
*   **Dockerized**: Zero-configuration setup for Postgres, Redis, Backend, and Frontend.

## 🛠 Tech Stack

### Backend (Spring Boot 3.2 + Java 21)
*   **Spring Data JPA**: For relational data management in PostgreSQL.
*   **Flyway**: Automated database migrations.
*   **Redis**: High-speed list-based queue for click events and result caching.
*   **Jackson**: JSON processing and serialization.
*   **Lombok**: Boilerplate reduction.
*   **Validation**: Robust API request validation.

### Frontend (Angular 21)
*   **Standalone Components**: Modern, modular frontend architecture.
*   **Angular Material**: Premium UI/UX design.
*   **Reactive Forms**: For complex link generation and filtering logic.
*   **SCSS**: Custom design system with global tokens.

### Infrastructure
*   **Docker Compose**: Orchestration of all services.
*   **PostgreSQL**: Reliable persistence for core entities.

## 🏗 Architecture Details

### 1. Multi-Tenant Isolation
Every table in the database contains a `tenant_id` column.
*   **Backend**: A `TenantValidator` utility is injected into every service to verify tenant existence and ownership before any CRUD operation.
*   **Frontend**: A `TenantContextInterceptor` automatically attaches the `X-Tenant-ID` header to all outgoing HTTP requests once a tenant is selected in the UI.

### 2. High-Performance Click Pipeline
To ensure marketing redirects are instant:
1.  **Ingestion**: `/track` endpoint receives the click and instantly does an `LPUSH` into a Redis queue.
2.  **Processing**: A `@Scheduled` job wakes up every 5 seconds, pops items from Redis in batches (up to 500), and performs a bulk insert into PostgreSQL.
3.  **Fraud Prevention**: The processor checks a Redis key (`fraud:{ip}:{affiliateId}`) with a 60s TTL to deduplicate spam clicks.

### 3. Analytics Caching
Analytics queries involve heavy aggregations. To keep the UI snappy:
*   We use `@Cacheable` with a custom Redis Cache Manager.
*   **TTL**: 5 minutes.
*   **Key**: Composite key based on all 5 filter parameters (`tenantId`, `affiliateId`, `campaignId`, `itemId`, `dateRange`).

## 🚦 Getting Started

### Prerequisites
*   Docker & Docker Compose
*   Java 21 (for manual backend run)
*   Node.js 22 (for manual frontend run)

### Running with Docker (Recommended)
```bash
# Clone the repository
git clone https://github.com/sssanthoshsivan/Affiliate-Management-Platform.git
cd Affiliate-Management-Platform

# Start all services
docker-compose up -d
```
*   **Frontend**: [http://localhost:4200](http://localhost:4200)
*   **Backend API**: [http://localhost:8080](http://localhost:8080)
*   **Postgres**: localhost:5432
*   **Redis**: localhost:6379

## 🧪 API Documentation
A Postman collection is included in `docs/runloyal_api.json`. 

### Major Endpoints:
*   `POST /tenants`: Register a new platform instance.
*   `GET /tenants/{id}/analytics`: Aggregated performance data.
*   `POST /affiliate-links`: Generate tracked codes.
*   `GET /r/{shortCode}`: Secure redirect to the tracking engine.
*   `POST /orders`: Capture conversions and calculate commissions.

---
Built by **Antigravity AI** for RunLoyal.
