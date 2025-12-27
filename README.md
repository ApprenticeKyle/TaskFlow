[English](#english) | [中文](#chinese)

<a name="chinese"></a>
# TaskFlow 微服务项目

## 项目简介

TaskFlow 是一个基于 Spring Cloud 的微服务架构项目，采用前后端分离设计，提供任务管理、项目管理、通知、分析等完整功能。

## 技术栈

### 核心框架
- **Spring Boot**: 3.2.0
- **Spring Cloud**: 2023.0.0
- **Spring Cloud Alibaba**: 2022.0.0.0
- **Java**: 21

### 基础设施
- **Nacos**: 服务注册与配置中心
- **Redis**: 缓存与会话管理
- **MySQL**: 关系型数据库
- **Elasticsearch**: 搜索引擎
- **Prometheus**: 监控指标
- **Zipkin**: 链路追踪

### 网关与安全
- **Spring Cloud Gateway**: API 网关
- **Spring Security + OAuth2**: 认证授权
- **Resilience4j**: 熔断器
- **Redis**: 限流

## 服务列表

| 服务名称 | 端口 | 职责 | 数据库 | Redis DB |
|---------|------|------|--------|----------|
| api-gateway | 8080 | API 网关 | 0 |
| auth-service | 8081 | 认证服务 | taskflow_auth | 1 |
| project-service | 8082 | 项目服务 | taskflow_project | 2 |
| task-service | 8083 | 任务服务 | taskflow_task | 3 |
| notification-service | 8084 | 通知服务 | taskflow_notification | 4 |
| analytics-service | 8085 | 分析服务 | taskflow_analytics | 5 |
| search-service | 8086 | 搜索服务 | - | 6 |
| file-service | 8087 | 文件服务 | taskflow_file | 7 |

## 项目结构

```
TaskFlow/
├── gateway/              # API 网关
├── auth/                 # 认证服务
├── project/               # 项目服务
├── task/                 # 任务服务
├── notification/          # 通知服务
├── analytics/             # 分析服务
├── search/               # 搜索服务
├── file/                 # 文件服务
├── pom.xml              # 父 POM
├── ARCHITECTURE.md      # 架构说明
└── API_TEST.md          # API 测试文档
```

## 快速开始

### 前置条件

1. JDK 21+
2. Maven 3.6+
3. MySQL 8.0+
4. Redis 5.0+
5. Nacos 2.0+（可选）

### 启动步骤

#### 1. 克隆项目
```bash
git clone <repository-url>
cd TaskFlow
```

#### 2. 配置数据库

创建数据库：
```sql
CREATE DATABASE taskflow_auth CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE taskflow_project CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE taskflow_task CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE taskflow_notification CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE taskflow_analytics CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE taskflow_file CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### 3. 配置环境变量（可选）

```bash
export MYSQL_HOST=localhost
export MYSQL_PORT=3306
export MYSQL_USERNAME=root
export MYSQL_PASSWORD=your_password

export REDIS_HOST=localhost
export REDIS_PORT=6379
export REDIS_PASSWORD=your_password

export NACOS_HOST=localhost
export NACOS_PORT=8848
```

#### 4. 启动服务

在 IDEA 中依次启动各个服务的 Application 类：

1. GatewayApplication (8080)
2. AuthApplication (8081)
3. ProjectApplication (8082)
4. TaskApplication (8083)
5. NotificationApplication (8084)
6. AnalyticsApplication (8085)
7. SearchApplication (8086)
8. FileApplication (8087)

或者使用 Maven 命令：
```bash
mvn clean install
cd gateway && mvn spring-boot:run
cd ../auth && mvn spring-boot:run
# ... 其他服务
```

## 开发指南

### 调试单个服务

**方法 1：直接访问服务端口**
```bash
# 只启动 project-service
GET http://localhost:8082/projects
```

**方法 2：通过网关访问**
```bash
# 启动 gateway + project-service
GET http://localhost:8080/api/projects
```

### 服务间调用

使用 **WebClient** 进行 HTTP 调用：

```java
@Service
public class TaskServiceClient {
    
    @Autowired
    private WebClient.Builder webClientBuilder;

    public Mono<Map> getTasksByProjectId(Long projectId) {
        return webClientBuilder
            .build()
            .get()
            .uri("lb://task-service/tasks?projectId=" + projectId)
            .retrieve()
            .bodyToMono(Map.class);
    }
}
```

详细说明请参考 [ARCHITECTURE.md](ARCHITECTURE.md)。

### API 测试

使用 Postman 或 curl 测试 API：

```bash
# 登录
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"123456"}'

# 获取项目列表
curl http://localhost:8080/api/projects
```

详细 API 文档请参考 [API_TEST.md](API_TEST.md)。

## 部署指南

### Docker 部署

#### 1. 构建镜像
```bash
mvn clean package -DskipTests
docker build -t taskflow/gateway:latest ./gateway
docker build -t taskflow/auth:latest ./auth
# ... 其他服务
```

#### 2. 运行容器
```bash
docker-compose up -d
```

### Kubernetes 部署

使用 Helm Chart 部署：

```bash
helm install taskflow ./helm/taskflow
```

## 监控与运维

### 健康检查

```bash
# 网关健康检查
curl http://localhost:8080/actuator/health

# 各服务健康检查
curl http://localhost:8081/actuator/health
curl http://localhost:8082/actuator/health
# ...
```

### 查看日志

在 IDEA 控制台或使用日志收集工具查看。

### 性能监控

访问 Prometheus + Grafana：
- Prometheus: http://localhost:9090
- Grafana: http://localhost:3000

## 常见问题

### Q: 如何禁用 Nacos？

A: 在 `application.yml` 中设置：
```yaml
spring:
  cloud:
    nacos:
      config:
        enabled: false
        import-check:
          enabled: false
      discovery:
        enabled: false
```

### Q: 服务间调用用什么方式？

A: 推荐 WebClient（HTTP REST），也可以用 OpenFeign（声明式）。

### Q: 能不能直接访问具体服务？

A: 可以！开发调试时可以直接访问服务端口，生产环境建议通过网关。

### Q: 需要暴露 RPC 接口吗？

A: 不需要。Spring Cloud 微服务使用 HTTP REST API 通信。

## 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 联系方式

- 项目主页: [GitHub Repository]
- 问题反馈: [Issues]
- 邮箱: support@taskflow.com

---

<a name="english"></a>
# TaskFlow Microservices Project

## Introduction

TaskFlow is a microservices-based project built with Spring Cloud. It adopts a separation of frontend and backend design, providing complete functionalities such as task management, project management, notifications, and analytics.

## Tech Stack

### Core Frameworks
- **Spring Boot**: 3.2.0
- **Spring Cloud**: 2023.0.0
- **Spring Cloud Alibaba**: 2022.0.0.0
- **Java**: 21

### Infrastructure
- **Nacos**: Service Registration & Configuration Center
- **Redis**: Caching & Session Management
- **MySQL**: Relational Database
- **Elasticsearch**: Search Engine
- **Prometheus**: Metrics Monitoring
- **Zipkin**: Distributed Tracing

### Gateway & Security
- **Spring Cloud Gateway**: API Gateway
- **Spring Security + OAuth2**: Authentication & Authorization
- **Resilience4j**: Circuit Breaker
- **Redis**: Rate Limiting

## Services List

| Service Name | Port | Responsibility | Database | Redis DB |
|---------|------|------|--------|----------|
| api-gateway | 8080 | API Gateway | 0 |
| auth-service | 8081 | Authentication Service | taskflow_auth | 1 |
| project-service | 8082 | Project Service | taskflow_project | 2 |
| task-service | 8083 | Task Service | taskflow_task | 3 |
| notification-service | 8084 | Notification Service | taskflow_notification | 4 |
| analytics-service | 8085 | Analytics Service | taskflow_analytics | 5 |
| search-service | 8086 | Search Service | - | 6 |
| file-service | 8087 | File Service | taskflow_file | 7 |

## Project Structure

```
TaskFlow/
├── gateway/              # API Gateway
├── auth/                 # Authentication Service
├── project/               # Project Service
├── task/                 # Task Service
├── notification/          # Notification Service
├── analytics/             # Analytics Service
├── search/               # Search Service
├── file/                 # File Service
├── pom.xml              # Parent POM
├── ARCHITECTURE.md      # Architecture Documentation
└── API_TEST.md          # API Test Documentation
```

## Quick Start

### Prerequisites

1. JDK 21+
2. Maven 3.6+
3. MySQL 8.0+
4. Redis 5.0+
5. Nacos 2.0+ (Optional)

### Startup Steps

#### 1. Clone Project
```bash
git clone <repository-url>
cd TaskFlow
```

#### 2. Configure Database

Create databases:
```sql
CREATE DATABASE taskflow_auth CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE taskflow_project CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE taskflow_task CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE taskflow_notification CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE taskflow_analytics CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE taskflow_file CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### 3. Configure Environment Variables (Optional)

```bash
export MYSQL_HOST=localhost
export MYSQL_PORT=3306
export MYSQL_USERNAME=root
export MYSQL_PASSWORD=your_password

export REDIS_HOST=localhost
export REDIS_PORT=6379
export REDIS_PASSWORD=your_password

export NACOS_HOST=localhost
export NACOS_PORT=8848
```

#### 4. Start Services

Start each service's Application class in IDEA:

1. GatewayApplication (8080)
2. AuthApplication (8081)
3. ProjectApplication (8082)
4. TaskApplication (8083)
5. NotificationApplication (8084)
6. AnalyticsApplication (8085)
7. SearchApplication (8086)
8. FileApplication (8087)

Or use Maven commands:
```bash
mvn clean install
cd gateway && mvn spring-boot:run
cd ../auth && mvn spring-boot:run
# ... other services
```

## Development Guide

### Debugging Single Service

**Method 1: Direct Service Access**
```bash
# Start only project-service
GET http://localhost:8082/projects
```

**Method 2: Access via Gateway**
```bash
# Start gateway + project-service
GET http://localhost:8080/api/projects
```

### Inter-service Calls

Use **WebClient** for HTTP calls:

```java
@Service
public class TaskServiceClient {
    
    @Autowired
    private WebClient.Builder webClientBuilder;

    public Mono<Map> getTasksByProjectId(Long projectId) {
        return webClientBuilder
            .build()
            .get()
            .uri("lb://task-service/tasks?projectId=" + projectId)
            .retrieve()
            .bodyToMono(Map.class);
    }
}
```

For more details, please refer to [ARCHITECTURE.md](ARCHITECTURE.md).

### API Testing

Use Postman or curl to test APIs:

```bash
# Login
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"123456"}'

# Get Project List
curl http://localhost:8080/api/projects
```

For detailed API documentation, please refer to [API_TEST.md](API_TEST.md).

## Deployment Guide

### Docker Deployment

#### 1. Build Images
```bash
mvn clean package -DskipTests
docker build -t taskflow/gateway:latest ./gateway
docker build -t taskflow/auth:latest ./auth
# ... other services
```

#### 2. Run Containers
```bash
docker-compose up -d
```

### Kubernetes Deployment

Deploy using Helm Chart:

```bash
helm install taskflow ./helm/taskflow
```

## Monitoring & Operations

### Health Check

```bash
# Gateway Health Check
curl http://localhost:8080/actuator/health

# Service Health Checks
curl http://localhost:8081/actuator/health
curl http://localhost:8082/actuator/health
# ...
```

### View Logs

View in IDEA console or use log collection tools.

### Performance Monitoring

Access Prometheus + Grafana:
- Prometheus: http://localhost:9090
- Grafana: http://localhost:3000

## FAQ

### Q: How to disable Nacos?

A: Set in `application.yml`:
```yaml
spring:
  cloud:
    nacos:
      config:
        enabled: false
        import-check:
          enabled: false
      discovery:
        enabled: false
```

### Q: What method is used for inter-service calls?

A: Recommended is WebClient (HTTP REST), or OpenFeign (Declarative).

### Q: Can I access specific services directly?

A: Yes! You can access service ports directly for debugging, but use the gateway for production.

### Q: Is it necessary to expose RPC interfaces?

A: No. Spring Cloud microservices use HTTP REST APIs for communication.

## Contribution Guide

1. Fork this repository
2. Create Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to Branch (`git push origin feature/AmazingFeature`)
5. Open Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Contact

- Project Home: [GitHub Repository]
- Issues: [Issues]
- Email: support@taskflow.com
