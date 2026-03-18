# JWT 认证 - 学习项目

## 📖 项目说明

这是 `springboot-demo` 的进阶学习项目，专注于 **JWT 用户认证**功能。

### 与 springboot-demo 的区别

| 项目 | 内容 |
|------|------|
| `springboot-demo` | 基础 CRUD、拦截器、统一返回 |
| `jwt-demo` | 在 springboot-demo 基础上增加 **JWT 登录/注册** 功能 |

---

## 📁 新增内容

相比 `springboot-demo`，本项目新增了以下内容：

```
jwt-demo/
├── src/main/java/com/example/demo/
│   ├── jwt/                              # 【新增】JWT 模块
│   │   ├── JwtTokenProvider.java         # JWT Token 生成和验证工具类
│   │   ├── JwtAuthService.java           # 登录/注册业务逻辑
│   │   ├── LoginRequest.java             # 登录请求 DTO
│   │   └── LoginResponse.java            # 登录响应 DTO
│   ├── controller/
│   │   ├── AuthController.java           # 【新增】认证控制器
│   │   └── RegisterRequest.java          # 【新增】注册请求 DTO
│   └── entity/
│       └── User.java                     # 【已修改】添加 password、nickname 字段
└── src/main/resources/
    └── application.properties            # 【已修改】添加 JWT 配置
```

---

## 🚀 快速开始

### 1. 启动项目

```bash
cd jwt-demo
mvn spring-boot:run
```

### 2. 测试接口

#### 注册用户

```bash
curl -X POST http://localhost:18080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"zhangsan","password":"123456","nickname":"张三"}'
```

#### 登录获取 Token

```bash
curl -X POST http://localhost:18080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"zhangsan","password":"123456"}'
```

#### 使用 Token 访问

```bash
curl http://localhost:18080/api/auth/me \
  -H "Authorization: Bearer <你的 token>"
```

---

## 📋 API 接口列表

| 接口 | 方法 | 说明 | 是否需要登录 |
|------|------|------|-------------|
| `/api/auth/register` | POST | 用户注册 | ❌ |
| `/api/auth/login` | POST | 用户登录 | ❌ |
| `/api/auth/me` | GET | 获取当前用户信息 | ✅ |

---

## 🔐 JWT 核心概念

### JWT 是什么？

JWT (JSON Web Token) 是一种开放的认证标准，用于在网络应用间安全地传递信息。

### JWT 的结构

```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjEsInVzZXJuYW1lIjoiemhhbmdzYW4ifQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
│                             │                             │
└───── Header (头部) ─────┘└───── Payload (载荷) ─────┘└───── Signature (签名) ─────┘
```

### 工作流程

```
┌──────────┐                          ┌──────────┐
│   前端    │                          │   后端    │
└────┬─────┘                          └────┬─────┘
     │                                     │
     │  POST /api/auth/login               │
     │  {username, password}               │
     │ ─────────────────────────────────>  │
     │                                     │
     │                               验证密码
     │                               生成 JWT Token
     │                                     │
     │  {token, userId, username}          │
     │ <────────────────────────────────── │
     │                                     │
     │ 保存 Token (localStorage)           │
     │                                     │
     │  GET /api/users                     │
     │  Authorization: Bearer <token>      │
     │ ─────────────────────────────────>  │
     │                                     │
     │                               验证 Token
     │                               返回用户数据
     │                                     │
     │  {users...}                         │
     │ <────────────────────────────────── │
     │                                     │
```

---

## 💻 核心代码

### 1. 生成 Token

```java
// JwtTokenProvider.java
public String generateToken(String username, Long userId) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + expiration);

    return Jwts.builder()
        .setHeaderParam("typ", "JWT")
        .setHeaderParam("alg", "HS256")
        .setSubject(username)
        .claim("userId", userId)
        .claim("username", username)
        .setIssuedAt(now)
        .setExpiration(expiryDate)
        .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
        .compact();
}
```

### 2. 验证 Token

```java
public boolean validateToken(String token) {
    try {
        Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
            .build()
            .parseClaimsJws(token);
        return true;
    } catch (ExpiredJwtException ex) {
        System.err.println("Token 已过期");
    } catch (SecurityException ex) {
        System.err.println("签名无效");
    }
    return false;
}
```

### 3. 登录逻辑

```java
// JwtAuthService.java
public LoginResponse login(String username, String password) {
    // 1. 根据用户名查找用户
    Optional<User> userOptional = userRepository.findByUsername(username);
    if (!userOptional.isPresent()) {
        throw new RuntimeException("用户不存在");
    }
    User user = userOptional.get();

    // 2. 验证密码
    if (!password.equals(user.getPassword())) {
        throw new RuntimeException("密码错误");
    }

    // 3. 生成 JWT Token
    String token = jwtTokenProvider.generateToken(user.getUsername(), user.getId());

    // 4. 返回登录信息
    return new LoginResponse(token, user.getUsername(), user.getId(), TOKEN_EXPIRATION);
}
```

---

## 📝 配置文件

### application.properties

```properties
# 服务端口
server.port=18080

# 数据库配置（H2 内存数据库）
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA 配置
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# H2 控制台
spring.h2.console.enabled=true

# ------------------- JWT 配置 -------------------
# JWT 密钥（至少 32 个字符）
jwt.secret=mySecretKey123456789012345678901234567890
# Token 过期时间（毫秒）- 7 天
jwt.expiration=604800000
```

---

## 🎯 学习步骤

### 第 1 步：理解 JWT 结构
- 阅读 `JwtTokenProvider.java`
- 了解 Header、Payload、Signature 的作用

### 第 2 步：理解登录流程
- 阅读 `JwtAuthService.java`
- 理解如何验证密码、生成 Token

### 第 3 步：理解认证拦截
- 阅读 `AuthController.java`
- 理解如何从请求头提取 Token

### 第 4 步：前端联调
- 使用 React/Next.js 创建登录页面
- 保存 Token 到 localStorage
- 在请求头中携带 Token

---

## ⚠️ 安全注意事项

### 当前代码的简化处理

1. **密码明文存储** - 实际项目应使用 BCrypt 加密
2. **密钥硬编码** - 实际项目应使用环境变量
3. **无 Token 刷新机制** - Token 过期需重新登录

### 生产环境建议

- ✅ 使用 BCrypt 或 Argon2 加密密码
- ✅ JWT 密钥使用环境变量
- ✅ 启用 HTTPS
- ✅ 设置合理的 Token 过期时间
- ✅ 添加 Token 黑名单机制

---

## 📚 扩展学习

完成本模块后，可以继续学习：

1. **密码加密** - 集成 Spring Security 的 PasswordEncoder
2. **Token 刷新** - 实现 refresh token 机制
3. **权限控制** - 基于角色的访问控制（RBAC）
4. **前后端联调** - 用 React 创建登录页面

---

*创建时间：2026-03-16*
