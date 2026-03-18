# JWT 认证模块学习文档

## 📁 新增文件结构

```
springboot-demo/
├── src/main/java/com/example/demo/
│   ├── jwt/                              # 【新增】JWT 模块
│   │   ├── JwtTokenProvider.java         # JWT Token 生成和验证工具类
│   │   ├── JwtAuthService.java           # 登录/注册业务逻辑
│   │   ├── LoginRequest.java             # 登录请求 DTO
│   │   └── LoginResponse.java            # 登录响应 DTO
│   ├── controller/
│   │   ├── AuthController.java           # 【新增】认证控制器
│   │   └── RegisterRequest.java          # 【新增】注册请求 DTO
│   ├── entity/
│   │   └── User.java                     # 【已修改】添加 password、nickname 字段
│   └── repository/
│       └── UserRepository.java           # 【已修改】添加 findByUsername 方法
└── src/main/resources/
    └── application.properties            # 【已修改】添加 JWT 配置
```

---

## 🔐 JWT 基础概念

### 什么是 JWT？

**JWT (JSON Web Token)** 是一种开放的认证标准，用于在网络应用间安全地传递信息。

### JWT 的结构

JWT 由三部分组成，用 `.` 分隔：

```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjEsInVzZXJuYW1lIjoiemhhbmdzYW4ifQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
```

#### 1️⃣ Header（头部）
```json
{
  "alg": "HS256",  // 签名算法
  "typ": "JWT"     // 类型
}
```

#### 2️⃣ Payload（载荷）
```json
{
  "userId": 1,           // 自定义字段：用户 ID
  "username": "zhangsan",// 自定义字段：用户名
  "iat": 1699999999,     // 签发时间
  "exp": 1700604799      // 过期时间
}
```

#### 3️⃣ Signature（签名）
```
HMACSHA256(
  base64UrlEncode(header) + "." + base64UrlEncode(payload),
  secret
)
```

---

## 📋 API 接口说明

### 1. 用户登录

**接口**: `POST /api/auth/login`

**请求**:
```bash
curl -X POST http://localhost:18080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"zhangsan","password":"123456"}'
```

**响应**:
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "username": "zhangsan",
    "userId": 1,
    "expiresIn": 604800,
    "expiresAt": 1700604799000
  }
}
```

**前端使用**:
```javascript
// 1. 登录
const res = await fetch('/api/auth/login', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ username, password })
});
const data = await res.json();

// 2. 保存 Token
localStorage.setItem('token', data.data.token);

// 3. 后续请求携带 Token
fetch('/api/users', {
  headers: {
    'Authorization': `Bearer ${data.data.token}`
  }
});
```

---

### 2. 用户注册

**接口**: `POST /api/auth/register`

**请求**:
```bash
curl -X POST http://localhost:18080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"lisi","password":"123456","nickname":"小李"}'
```

**响应**:
```json
{
  "code": 200,
  "message": "注册成功",
  "data": {
    "id": 2,
    "username": "lisi",
    "nickname": "小李",
    "email": null,
    "age": null,
    "createTime": "2026-03-16T10:00:00.000+00:00",
    "updateTime": "2026-03-16T10:00:00.000+00:00"
  }
}
```

---

### 3. 获取当前用户信息

**接口**: `GET /api/auth/me`

**请求**:
```bash
curl http://localhost:18080/api/auth/me \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

**响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "username": "zhangsan",
    "nickname": "张三",
    "email": "zhangsan@example.com",
    "age": null
  }
}
```

---

## 💻 核心代码解析

### 1. JwtTokenProvider - Token 生成

```java
public String generateToken(String username, Long userId) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + expiration);

    return Jwts.builder()
        .setHeaderParam("typ", "JWT")          // Header: 类型
        .setHeaderParam("alg", "HS256")        // Header: 算法
        .setSubject(username)                  // Payload: 主题
        .claim("userId", userId)               // Payload: 自定义字段
        .claim("username", username)           // Payload: 自定义字段
        .setIssuedAt(now)                      // Payload: 签发时间
        .setExpiration(expiryDate)             // Payload: 过期时间
        .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8))) // Signature
        .compact();
}
```

**类比前端**:
```javascript
// Node.js 使用 jsonwebtoken
const token = jwt.sign(
  { userId, username },  // payload
  secretKey,             // 密钥
  { expiresIn: '7d' }    // 过期时间
);
```

---

### 2. JwtTokenProvider - Token 验证

```java
public boolean validateToken(String token) {
    try {
        Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
            .build()
            .parseClaimsJws(token);  // 解析并验证
        return true;
    } catch (ExpiredJwtException ex) {
        System.err.println("Token 已过期");
    } catch (SecurityException ex) {
        System.err.println("签名无效");
    } catch (MalformedJwtException ex) {
        System.err.println("Token 格式错误");
    }
    return false;
}
```

**类比前端**:
```javascript
try {
  const decoded = jwt.verify(token, secretKey);
  // Token 有效，decoded 包含 payload 内容
} catch (err) {
  if (err.name === 'TokenExpiredError') {
    console.log('Token 已过期');
  } else if (err.name === 'JsonWebTokenError') {
    console.log('Token 无效');
  }
}
```

---

### 3. JwtAuthService - 登录逻辑

```java
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

    // 3. 生成 Token
    String token = jwtTokenProvider.generateToken(user.getUsername(), user.getId());

    // 4. 返回登录信息
    return new LoginResponse(token, user.getUsername(), user.getId(), TOKEN_EXPIRATION);
}
```

**流程图解**:
```
用户请求登录
     ↓
查询数据库找用户
     ↓
密码正确？──否──→ 抛出异常
     ↓ 是
生成 JWT Token
     ↓
返回 Token 给前端
```

---

## 🔧 配置文件说明

### application.properties

```properties
# JWT 密钥（至少 32 个字符）
# 实际项目中应该使用环境变量，不要硬编码在代码里
jwt.secret=mySecretKey123456789012345678901234567890

# Token 过期时间（毫秒）
# 7 天 = 7 * 24 * 60 * 60 * 1000 = 604800000
jwt.expiration=604800000
```

---

## 🚀 测试步骤

### 步骤 1：启动项目

```bash
cd springboot-demo
mvn spring-boot:run
```

### 步骤 2：注册用户

```bash
curl -X POST http://localhost:18080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"zhangsan","password":"123456","nickname":"张三"}'
```

### 步骤 3：登录获取 Token

```bash
curl -X POST http://localhost:18080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"zhangsan","password":"123456"}'
```

复制返回的 `token` 值。

### 步骤 4：使用 Token 访问受保护的接口

```bash
# 替换 YOUR_TOKEN_HERE 为实际的 token
curl http://localhost:18080/api/auth/me \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

---

## ⚠️ 安全注意事项

### 当前代码的安全问题

1. **密码明文存储**
   ```java
   // 当前代码：密码直接存储
   user.setPassword(password);

   // 应该使用：BCrypt 加密
   String encodedPassword = passwordEncoder.encode(password);
   user.setPassword(encodedPassword);
   ```

2. **密钥硬编码**
   ```properties
   # 当前：密钥写在配置文件
   jwt.secret=mySecretKey...

   # 应该：使用环境变量
   jwt.secret=${JWT_SECRET}
   ```

3. **没有 Token 刷新机制**
   - 当前 Token 过期后需要重新登录
   - 可以添加 refresh token 机制

### 生产环境建议

- 使用 BCrypt 或 Argon2 加密密码
- JWT 密钥使用环境变量或密钥管理服务
- 启用 HTTPS
- 设置合理的 Token 过期时间
- 添加 Token 黑名单机制

---

## 📚 扩展学习

### 下一步可以学习：

1. **密码加密** - 集成 Spring Security 的 PasswordEncoder
2. **Token 刷新** - 实现 refresh token 机制
3. **权限控制** - 基于角色的访问控制（RBAC）
4. **登出功能** - Token 黑名单实现

---

*创建时间：2026-03-16*
