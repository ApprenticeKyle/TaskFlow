# FlowStack 🚀

[English](./README.md) | [简体中文](./README.zh-CN.md)

**FlowStack** is a powerful microservices ecosystem built with **Spring Cloud**. It provides a scalable, distributed architecture for task management, project collaboration, and real-time analytics.

It is the backend engine powering the **[FlowBoard](https://github.com/your-repo/FlowBoard)** frontend.

---

## 🏗️ Architecture

FlowStack is designed with **DDD (Domain-Driven Design)** principles and a microservices architecture to ensure high availability and scalability.

- **Infrastructure**: Nacos (Registry/Config), MySQL, Redis, Elasticsearch.
- **Gateway**: Spring Cloud Gateway with integrated security.
- **Services**: Auth, Project, Task, Notification, Analytics, Search, File.

## 🛠️ Tech Stack

- **Core Framework**: Spring Boot 3.2.0, Spring Cloud 2023.0.0
- **Language**: Java 21
- **Persistence**: Spring Data JPA, MySQL 8.0+
- **Cache**: Redis 5.0+
- **Service Mesh**: Spring Cloud Alibaba (Nacos)
- **Security**: Spring Security + OAuth2 / JWT
- **Search**: Elasticsearch 8.x

## 📂 Service Map

| Service Name | Port | Responsibility | Database | Redis DB |
|---------|------|------|--------|----------|
| **api-gateway** | 8080 | Entry point, routing & security | - | 0 |
| **auth-service** | 8081 | Identity & Access Management | `flowstack_auth` | 1 |
| **project-service** | 8082 | Project lifecycle management | `flowstack_project` | 2 |
| **task-service** | 8083 | Task tracking & boards | `flowstack_task` | 3 |
| **notification-service** | 8084 | Real-time alerts & emails | `flowstack_notification` | 4 |
| **analytics-service** | 8085 | Data aggregation & reporting | `flowstack_analytics` | 5 |
| **search-service** | 8086 | Full-text search engine | - | 6 |
| **file-service** | 8087 | Distributed file storage | `flowstack_file` | 7 |

## 🚀 Quick Start

### 1. Prerequisites
- JDK 21
- Maven 3.6+
- Docker & Docker Compose (Recommended)

### 2. Using Docker Compose (Fastest)
```bash
mvn clean package -DskipTests
docker-compose up -d
```

### 3. Manual Startup
1. Create MySQL databases (see `init.sql`).
2. Start infrastructure (MySQL, Redis, Nacos).
3. Start `api-gateway` first.
4. Start other microservices as needed.

## 📂 Project Structure
```text
FlowStack/
├── gateway/        # API Gateway
├── auth/           # Auth Service
├── project/        # Project Module
├── task/           # Task Module
├── ...             # Other microservices
├── docker/         # Infrastructure config
└── pom.xml         # Root Maven POM
```

## 📄 Documentation
- **[Architecture Details](ARCHITECTURE.md)**
- **[API Testing Guide](API_TEST.md)**

## 🤝 Ecosystem
Managed primarily through the **[FlowBoard](https://github.com/your-repo/FlowBoard)** frontend.

## 📄 License
MIT License.
