# TaskFlow 微服务架构说明

## 一、整体架构

### 1. 网关层（Gateway）
- **端口**: 8080
- **职责**: 统一入口、路由转发、认证鉴权、限流、熔断
- **访问方式**: 所有外部请求必须通过网关

### 2. 服务层
- **auth-service**: 8081 - 认证服务
- **project-service**: 8082 - 项目服务
- **task-service**: 8083 - 任务服务
- **notification-service**: 8084 - 通知服务
- **analytics-service**: 8085 - 分析服务
- **search-service**: 8086 - 搜索服务
- **file-service**: 8087 - 文件服务

## 二、请求路由规则

### 通过网关访问（生产环境推荐）

所有请求都通过网关转发：

```
外部请求 -> Gateway (8080) -> 具体服务
```

**路由规则**：
- `/auth/**` -> auth-service
- `/api/projects/**` -> project-service
- `/api/tasks/**` -> task-service
- `/api/notifications/**` -> notification-service
- `/api/analytics/**` -> analytics-service
- `/api/search/**` -> search-service
- `/api/files/**` -> file-service

**示例**：
```bash
# 登录
POST http://localhost:8080/auth/login

# 获取项目列表
GET http://localhost:8080/api/projects

# 获取项目详情
GET http://localhost:8080/api/projects/1

# 获取任务列表
GET http://localhost:8080/api/tasks?projectId=1
```

### 直接访问服务（开发调试用）

在开发环境中，可以直接访问具体服务进行调试：

```bash
# 直接访问 project-service
GET http://localhost:8082/projects

# 直接访问 auth-service
POST http://localhost:8081/login
```

## 三、服务间调用方式

### 1. HTTP REST API（推荐）

使用 **WebClient** 进行 HTTP 调用，这是 Spring WebFlux 推荐的方式。

**优点**：
- 简单直接，易于调试
- 支持响应式编程
- 可以直接通过浏览器或 Postman 测试

**示例**：
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

**配置 WebClient**：
```java
@Configuration
public class WebClientConfig {
    
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
```

### 2. OpenFeign（声明式）

使用 **OpenFeign** 进行声明式调用。

**优点**：
- 代码更简洁
- 支持负载均衡
- 集成度高

**示例**：
```java
@FeignClient(name = "task-service")
public interface TaskFeignClient {
    
    @GetMapping("/tasks")
    List<Task> getTasks(@RequestParam("projectId") Long projectId);
}
```

**需要添加依赖**：
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

## 四、接口暴露说明

### 1. REST API 接口（需要暴露）

**所有服务都需要暴露 REST API 接口**，用于：
- 前端调用（通过网关）
- 服务间调用（HTTP 方式）
- 测试和调试

**示例**：
```java
@RestController
@RequestMapping("/projects")
public class ProjectController {
    
    @GetMapping
    public Mono<List<Project>> list() {
        return Mono.just(projectService.findAll());
    }
    
    @GetMapping("/{id}")
    public Mono<Project> getById(@PathVariable Long id) {
        return Mono.just(projectService.findById(id));
    }
}
```

### 2. 内部服务方法（可选）

如果使用 OpenFeign，可以定义接口供其他服务调用，但不是必须的。

## 五、开发调试方法

### 方法 1：直接启动单个服务调试

1. 只启动需要调试的服务（如 project-service）
2. 直接访问该服务的端口：
   ```bash
   GET http://localhost:8082/projects
   ```

### 方法 2：通过网关调试

1. 启动网关和需要调试的服务
2. 通过网关访问：
   ```bash
   GET http://localhost:8080/api/projects
   ```

### 方法 3：使用 Postman 测试

1. 导入 API 文档
2. 选择环境（开发/测试/生产）
3. 发送请求测试

## 六、服务注册与发现

### 启用 Nacos（生产环境）

在 `application.yml` 中启用：

```yaml
spring:
  cloud:
    nacos:
      discovery:
        enabled: true
      config:
        enabled: true
```

### 禁用 Nacos（开发环境）

当前配置已禁用 Nacos，服务间调用使用 `lb://` 协议：

```java
.uri("lb://task-service")
```

这会使用服务名进行负载均衡调用。

## 七、最佳实践

### 1. 服务设计原则
- 每个服务只负责一个业务领域
- 服务间通过 REST API 通信
- 避免服务间直接数据库访问

### 2. 接口设计原则
- 使用 RESTful 风格
- 统一返回格式
- 添加版本控制

### 3. 错误处理
- 使用全局异常处理器
- 统一错误码
- 记录错误日志

### 4. 监控与日志
- 使用 Actuator 监控
- 集成 Prometheus + Grafana
- 使用 ELK 收集日志

## 八、快速开始

### 1. 启动所有服务
```bash
# 在 IDEA 中依次启动
GatewayApplication (8080)
AuthApplication (8081)
ProjectApplication (8082)
TaskApplication (8083)
...
```

### 2. 测试接口
```bash
# 测试认证
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"123456"}'

# 测试项目服务
curl http://localhost:8080/api/projects

# 直接访问项目服务（调试用）
curl http://localhost:8082/projects
```

### 3. 查看日志
每个服务都会输出日志到控制台，可以实时查看。

## 九、常见问题

### Q1: 能不能直接访问具体服务？
**A**: 可以！开发调试时可以直接访问具体服务的端口，生产环境建议通过网关。

### Q2: 服务间调用用什么方式？
**A**: 推荐 WebClient（HTTP REST），简单直接。也可以用 OpenFeign（声明式）。

### Q3: 需要暴露 RPC 接口吗？
**A**: 不需要。Spring Cloud 微服务使用 HTTP REST API 通信，不需要 RPC。

### Q4: 如何调试单个服务？
**A**: 
1. 只启动该服务
2. 直接访问该服务的端口
3. 或者启动网关 + 该服务，通过网关访问

### Q5: 网关的作用是什么？
**A**: 
- 统一入口（所有请求走一个端口）
- 路由转发（根据路径转发到不同服务）
- 认证鉴权（统一处理 JWT）
- 限流熔断（保护服务）
- 负载均衡（分发请求）
