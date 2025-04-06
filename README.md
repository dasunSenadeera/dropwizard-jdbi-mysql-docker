# Part 1: Hands-on Coding Challenge (1 Hour)

## 🎤 Speaker Session Submission API

This project provides a RESTful API for managing speaker session submissions using **Dropwizard** and **MySQL**. It supports CRUD operations, API key authentication, input validation, logging, pagination, and unit testing, and is fully containerized using **Docker**.

---

### ✅ Features

- 🔐 API key-based authentication
- 🧾 CRUD operations for session management
- 🧹 Input validation using Jakarta annotations
- 🗃️ MySQL integration via JDBI3
- 📦 Dockerized app and DB setup
- 📜 Pagination for session listing
- ⚡ Optimized queries with indexing
- 💧 Connection pooling (Tomcat JDBC)
- 🧪 Unit tests for core functionalities
- 🔎 Logging and error handling

---

### 🔧 Tech Stack

- Java 17
- Dropwizard
- JDBI3
- MySQL 8
- Docker & Docker Compose
- JUnit 5
- SLF4J + Logback
- Lombok

---

### 🔐 Authentication

All API requests must include a valid API key:

```
Header: X-API-Key: your_secret_api_key
```

Requests missing or using an incorrect key will be rejected with `401 Unauthorized`.

The API key is configured through the Docker config.yml file (or can be passed via env var in Docker).

---

### 📌 API Endpoints

| Method | Endpoint           | Description                             |
|--------|--------------------|-----------------------------------------|
| POST   | `/sessions`        | Submit a new speaker session            |
| GET    | `/sessions/{id}`   | Retrieve session by ID                  |
| GET    | `/sessions`        | Retrieve all sessions (paginated)       |
| PUT    | `/sessions/{id}`   | Update an existing session              |
| DELETE | `/sessions/{id}`   | Delete a session                        |

---

### 📄 Example Payload

```json
{
  "title": "Advanced Java Memory Management",
  "description": "Deep dive into JVM internals",
  "speakerName": "Alice Johnson",
  "fileUploadUrl": "https://example.com/slides.pdf"
}
```

---

### 🧾 Pagination Example

```http
GET /sessions?offset=0&limit=5
X-API-Key: your_secret_api_key
```

---

### 🧱 Database Schema

#### Table: `sessions`

```sql
CREATE TABLE sessions (
  id INT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  description TEXT NOT NULL,
  speaker_name VARCHAR(255) NOT NULL,
  file_upload_url TEXT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

#### Indexing

```sql
CREATE INDEX idx_speaker_name ON sessions(speaker_name);
```

---

### 🐳 Docker Setup

#### 1. Clone the repo

```bash
git clone https://github.com/your-org/speaker-session-api.git
cd speaker-session-api
```

#### 2. Start services

```bash
docker-compose up --build
```

This will:
- Build and run the Dropwizard service
- Initialize the MySQL database
- Execute SQL scripts to create tables and seed data

#### 3. Check health

```bash
curl http://localhost:8081/healthcheck
```

---


### 🧩 Project Structure

```
.
├── api/
│   └── SessionResource.java       # REST endpoints
├── auth/
│   └── ApiKeyAuthFilter.java      # API key filter
├── db/
│   └── SessionDAO.java            # JDBI DAO interface
├── model/
│   └── Session.java               # Session POJO
├── resources/
│   └── 01-speaker-sessions.sql    # DB init script
├── SessionServiceApplication.java # App entry point
├── SessionServiceConfiguration.java
├── config.yml                     # App configuration
└── docker-compose.yml             # Docker stack
```

---

### 🚀 Sample Curl Commands

#### Create a session

```bash
curl -X POST http://localhost:8080/sessions   -H "Content-Type: application/json"   -H "X-API-Key: your_secret_api_key"   -d '{"title":"Docker 101","description":"Basics of Docker","speakerName":"John Doe","fileUploadUrl":"http://file.com/doc.pdf"}'
```

#### List sessions

```bash
curl -H "X-API-Key: your_secret_api_key" http://localhost:8080/sessions
```
---

### 🧠 Future Enhancements

- Swagger/OpenAPI docs
- OAuth or JWT-based authentication
- Frontend dashboard for admin users
- File upload integration
- Rate limiting and analytics

## Part 2: Theoretical Questions (30 Minutes)

### Question 1: AWS Design Question

1. Compute:

Use AWS Fargate (via ECS) for serverless container hosting.

Deploy in multiple Availability Zones behind an Application Load Balancer (ALB).

2. Database:

Use Amazon Aurora (MySQL-Compatible) with Multi-AZ for high availability.

Add read replicas for scaling read operations.

3. Storage:

Store uploaded files in Amazon S3.

Use S3 presigned URLs or direct upload endpoints.

4. Security:

Use IAM roles for access control.

Store secrets (API key, DB creds) in AWS Secrets Manager.

Add WAF to ALB for protection.

Use API Gateway (optional) to expose and throttle API endpoints.

5. Scaling:

Fargate Auto Scaling based on CPU/Memory or request load.

Aurora Auto Scaling for read replicas.

S3 and ALB scale automatically.

### Question 2: Debugging & Performance Optimization

#### Immediate Fixes

##### 1) Add Indexes
Create database indexes on frequently queried columns (like user_id, created_at) to speed up searches.

##### 2) Implement Pagination
Limit results per request (e.g., 50 sessions per page) to reduce data transfer and processing time.

##### 3) Enable Caching
Cache frequent queries (e.g., recent sessions) using Redis or HTTP caching to avoid repeated database hits.

#### Database-Level Optimizations
#####  1) Use Read Replicas
   Offload read requests to replica databases to reduce load on the primary database.

#####  2)Optimize Connection Pooling
Configure database connection pooling to reuse connections efficiently and reduce overhead.

#### Scaling & Profiling
   Load Testing & Profiling
   Simulate traffic (using tools like k6) to identify bottlenecks. Profile queries and API performance to pinpoint slow operations.

Deploy Multiple Instances
Scale horizontally by running multiple instances of the service behind a load balancer to handle more concurrent requests.

#### Verification
   Monitor performance after each change to measure improvement.

Gradually roll out optimizations to ensure stability.

### Question 3: Security Scenario

#### 🚨 Immediate Response Actions

##### Infrastructure Protection
- **✅ Activate Cloud Protections**: Enable AWS Shield/Cloudflare/Azure DDoS Protection
- **✅ Traffic Filtering**:
    - Configure WAF rules (rate limiting, geo-blocking)
    - Implement IP blacklisting
- **✅ Elastic Scaling**:
    - Auto-scale Kubernetes pods (HPA)
    - Expand AWS Auto Scaling Groups

##### Application Protection
- **✅ Circuit Breaking**: Implement Resilience4j/Hystrix for fail-fast behavior
- **✅ Rate Limiting**: Enforce per-IP/endpoint limits via Spring Cloud Gateway
- **✅ CDN Offloading**: Serve static assets through Cloudflare/AWS CloudFront

#### 🏗️ Long-Term Architecture Improvements

##### Infrastructure
- **🛡 Multi-Region Deployment**: Active-active setup across cloud regions
- **🛡 Traffic Scrubbing**: Filter attacks at edge (Cloudflare Spectrum)

##### Application
- **🛡 Smart Caching**: Redis/Memcached for high-traffic endpoints
- **🛡 Async Processing**: Kafka/RabbitMQ for heavy operations
- **🛡 JVM Optimization**: Tuned GC and heap settings
- **🛡 Stateless Design**: JWT-based sessions for scaling

#### 🔍 Monitoring & Improvement

##### Observability
- **📊 Dashboards**: Prometheus+Grafana metrics
- **📝 Logging**: ELK Stack analysis
- **🚨 Alerts**: Slack/PagerDuty notifications

### Question 4: Database Improvements

**Describe key database-related improvements you implemented in this project:**

#### ❌ Missing Resource Management
- **Problem:** Open connections, statements, and result sets were never closed, leading to memory leaks.
- **Fix:** Implemented try-with-resources to ensure automatic resource closure.

#### ❌ Hardcoded DB Credentials
- **Problem:** Database credentials were hardcoded in source files.
- **Fix:** Migrated to environment variables and configuration files.

#### ❌ SQL Injection Vulnerabilities
- **Problem:** Used basic Statement interface risking SQL injection.
- **Fix:** Replaced all queries with parameterized PreparedStatement.

#### ❌ Inefficient Data Retrieval
- **Problem:** Used `SELECT *` queries fetching unnecessary columns.
- **Fix:** Specified exact columns needed (e.g., `SELECT id, name, email`).

#### ❌ Poor Error Handling
- **Problem:** Database errors caused application crashes.
- **Fix:** Implemented comprehensive try-catch blocks with proper error logging.

#### ❌ No Caching Mechanism
- **Problem:** Repeated identical queries overloaded the database.
- **Fix:** Integrated Redis cache for frequently accessed data.

**Impact of Changes:**
1. Improved security against SQL injection
2. Better memory management
3. Reduced database load
4. More stable application performance
5. Configurable credentials for different environments
