# TaskFlow API 测试示例

## 一、认证接口

### 1.1 用户登录
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "123456"
  }'
```

**响应**：
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "mock-jwt-token-123456",
    "userId": "1",
    "username": "admin"
  }
}
```

### 1.2 用户注册
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "newuser",
    "password": "123456",
    "email": "user@example.com"
  }'
```

## 二、项目接口

### 2.1 获取项目列表
```bash
# 通过网关访问
curl http://localhost:8080/api/projects

# 直接访问服务（调试用）
curl http://localhost:8082/projects
```

**响应**：
```json
{
  "code": 200,
  "message": "获取项目列表成功",
  "data": ["项目1", "项目2", "项目3"]
}
```

### 2.2 获取项目详情
```bash
curl http://localhost:8080/api/projects/1
```

**响应**：
```json
{
  "code": 200,
  "message": "获取项目详情成功",
  "data": {
    "id": 1,
    "name": "项目1",
    "description": "这是项目1的描述"
  }
}
```

### 2.3 创建项目
```bash
curl -X POST http://localhost:8080/api/projects \
  -H "Content-Type: application/json" \
  -d '{
    "name": "新项目",
    "description": "项目描述"
  }'
```

### 2.4 获取项目的任务（服务间调用示例）
```bash
curl http://localhost:8080/api/projects/1/tasks
```

**说明**：这个接口会通过 WebClient 调用 task-service。

## 三、任务接口

### 3.1 获取任务列表
```bash
curl http://localhost:8080/api/tasks?projectId=1
```

### 3.2 创建任务
```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "projectId": 1,
    "title": "新任务",
    "description": "任务描述"
  }'
```

## 四、通知接口

### 4.1 获取通知列表
```bash
curl http://localhost:8080/api/notifications
```

## 五、分析接口

### 5.1 获取分析数据
```bash
curl http://localhost:8080/api/analytics
```

## 六、搜索接口

### 6.1 搜索内容
```bash
curl http://localhost:8080/api/search?q=keyword
```

## 七、文件接口

### 7.1 上传文件
```bash
curl -X POST http://localhost:8080/api/files/upload \
  -F "file=@/path/to/file.txt"
```

### 7.2 下载文件
```bash
curl http://localhost:8080/api/files/download/1 \
  -o downloaded_file.txt
```

## 八、Postman 测试

### 8.1 导入环境变量

在 Postman 中创建环境变量：

| 变量名 | 值 | 说明 |
|--------|-----|------|
| gateway_url | http://localhost:8080 | 网关地址 |
| auth_url | http://localhost:8081 | 认证服务地址 |
| project_url | http://localhost:8082 | 项目服务地址 |
| task_url | http://localhost:8083 | 任务服务地址 |

### 8.2 测试流程

1. **登录获取 token**
   - Method: POST
   - URL: `{{gateway_url}}/auth/login`
   - Body: JSON
   ```json
   {
     "username": "admin",
     "password": "123456"
   }
   ```

2. **使用 token 访问受保护接口**
   - 添加 Header: `Authorization: Bearer {{token}}`
   - URL: `{{gateway_url}}/api/projects`

## 九、调试技巧

### 9.1 查看服务日志

在 IDEA 控制台查看每个服务的日志输出。

### 9.2 使用 Actuator 监控

```bash
# 查看健康状态
curl http://localhost:8080/actuator/health

# 查看服务信息
curl http://localhost:8080/actuator/info

# 查看指标
curl http://localhost:8080/actuator/metrics
```

### 9.3 测试服务间调用

在 ProjectControllerWithServiceCall 中设置断点，查看如何通过 WebClient 调用 task-service。

### 9.4 网关路由测试

```bash
# 测试路由是否正确
curl http://localhost:8080/actuator/gateway/routes
```

## 十、常见错误处理

### 10.1 服务不可用

**错误**：
```json
{
  "code": 503,
  "message": "Service Unavailable"
}
```

**解决**：
- 检查服务是否启动
- 检查 Nacos 注册状态（如果启用）

### 10.2 限流

**错误**：
```json
{
  "code": 429,
  "message": "Too Many Requests"
}
```

**解决**：
- 降低请求频率
- 检查限流配置

### 10.3 熔断

**错误**：
```json
{
  "code": 503,
  "message": "Circuit Breaker Open"
}
```

**解决**：
- 检查下游服务状态
- 等待熔断器恢复
