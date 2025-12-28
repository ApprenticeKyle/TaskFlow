# FlowStack 🚀

[English](./README.md) | [简体中文](./README.zh-CN.md)

**FlowStack** 是一个基于 **Spring Cloud** 构建的强大微服务生态系统。它为任务管理、项目协作和实时分析提供了可扩展的分布式架构。

它是驱动 **[FlowBoard](https://github.com/your-repo/FlowBoard)** 前端界面的后端引擎。

---

## 🏗️ 架构设计

FlowStack 采用 **DDD (领域驱动设计)** 原则和微服务架构，以确保高可用性和可扩展性。

- **基础设施**: Nacos (注册/配置)、MySQL、Redis、Elasticsearch。
- **网关**: 集成了安全防护的 Spring Cloud Gateway。
- **核心服务**: 认证、项目、任务、通知、分析、搜索、文件。

## 🛠️ 技术栈

- **核心框架**: Spring Boot 3.2.0, Spring Cloud 2023.0.0
- **语言**: Java 21
- **持久层**: Spring Data JPA, MySQL 8.0+
- **缓存**: Redis 5.0+
- **服务网格**: Spring Cloud Alibaba (Nacos)
- **安全**: Spring Security + OAuth2 / JWT
- **搜索**: Elasticsearch 8.x

## 📂 服务地图

| 服务名称 | Port | 职责 | 数据库 | Redis DB |
|---------|------|------|--------|----------|
| **api-gateway** | 8080 | 统一入口、路由及安全控制 | - | 0 |
| **auth-service** | 8081 | 身份标识与访问管理 | `flowstack_auth` | 1 |
| **project-service** | 8082 | 项目生命周期管理 | `flowstack_project` | 2 |
| **task-service** | 8083 | 任务追踪与看板 | `flowstack_task` | 3 |
| **notification-service** | 8084 | 实时通知与邮件 | `flowstack_notification` | 4 |
| **analytics-service** | 8085 | 数据聚合与报表分析 | `flowstack_analytics` | 5 |
| **search-service** | 8086 | 全文搜索引擎 | - | 6 |
| **file-service** | 8087 | 分布式文件存储 | `flowstack_file` | 7 |

## 🚀 快速开始

### 1. 前置条件
- JDK 21
- Maven 3.6+
- Docker & Docker Compose (推荐)

### 2. 使用 Docker Compose (最快方式)
```bash
mvn clean package -DskipTests
docker-compose up -d
```

### 3. 手动启动
1. 创建 MySQL 数据库（参考 `init.sql`）。
2. 启动基础设施（MySQL, Redis, Nacos）。
3. 首先启动 `api-gateway` first.
4. 根据需要启动其他微服务。

## 📂 项目结构
```text
FlowStack/
├── gateway/        # API 网关
├── auth/           # 认证服务
├── project/        # 项目模块
├── task/           # 任务模块
├── ...             # 其他微服务
├── docker/         # 基础设施配置
└── pom.xml         # 根 Maven POM
```

## 📄 文档
- **[架构详细说明](ARCHITECTURE.md)**
- **[API 测试指南](API_TEST.md)**

## 🤝 生态系统
主要通过 **[FlowBoard](https://github.com/your-repo/FlowBoard)** 前端进行操作管理。

## 📄 许可证
MIT 许可证。
