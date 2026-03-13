# 📚 Spring Boot 学习进度

> 最后更新：2026-03-12
> 学习方式：看懂 → 模仿 → 独立写

---

## ✅ 已完成的内容

### 1. 拦截器（Interceptor）
- [x] `LoggingInterceptor.java` - 日志拦截器
- [x] `AuthInterceptor.java` - 权限验证拦截器
- [x] `WebConfig.java` - 拦截器配置

**核心知识点**：
- `HandlerInterceptor` 接口
- `preHandle()` - 请求前处理
- `afterCompletion()` - 请求后处理
- 白名单机制

---

### 2. 参数接收（Controller）
- [x] `@RequestParam` - 查询参数
- [x] `@PathVariable` - 路径参数
- [x] `@RequestBody` - JSON 请求体

**文件**：`ParamsController.java`

---

### 3. JPA 数据库操作
- [x] `User.java` - 实体类（Entity）
- [x] `UserRepository.java` - 数据访问层
- [x] `UserController.java` - CRUD 接口

**核心知识点**：
- `@Entity` `@Table` `@Id` `@GeneratedValue`
- `JpaRepository<T, ID>`
- 方法命名规则：`findByUsername`、`findByAgeGreaterThan`
- 内置方法：`save()`、`findById()`、`findAll()`、`deleteById()`

**数据库**：H2 内存数据库（配置在 `application.properties`）

---

### 4. 统一返回格式
- [x] `Result<T>` - 统一返回类
- [x] `GlobalExceptionHandler` - 全局异常处理

**返回格式**：
```json
{
  "code": 200,
  "message": "success",
  "data": {...}
}
```

---

## 📂 项目文件结构

```
springboot-demo/src/main/java/com/example/demo/
├── common/
│   ├── Result.java                    # 统一返回类
│   └── GlobalExceptionHandler.java    # 全局异常处理
├── config/
│   └── WebConfig.java                 # Web 配置（拦截器注册）
├── controller/
│   ├── HelloController.java
│   ├── ParamsController.java          # 参数接收练习
│   ├── UserController.java            # 用户 CRUD（完整示例）
│   └── XiongController.java
├── entity/
│   └── User.java                      # 用户实体
├── interceptor/
│   ├── LoggingInterceptor.java        # 日志拦截器
│   └── AuthInterceptor.java           # 权限拦截器
├── repository/
│   └── UserRepository.java            # 用户数据访问层
└── DemoApplication.java               # 启动类
```

---

## 🔧 常用命令

### 启动项目
```bash
mvn spring-boot:run
```

### 测试接口
```bash
# 查询所有用户
curl "http://localhost:18080/api/users"

# 新增用户
curl -X POST "http://localhost:18080/api/users" \
  -H "Content-Type: application/json" \
  -d '{"username":"zhangsan","age":25}'

# 根据 ID 查询
curl "http://localhost:18080/api/users/1"

# 更新用户
curl -X PUT "http://localhost:18080/api/users/1" \
  -H "Content-Type: application/json" \
  -d '{"id":1,"username":"lisi","age":28}'

# 删除用户
curl -X DELETE "http://localhost:18080/api/users/1"

# 模糊查询
curl "http://localhost:18080/api/users/search/keyword?name=zhang"

# 年龄范围查询
curl "http://localhost:18080/api/users/age?min=20&max=30"
```

---

## 📝 待学习内容

### 下一步计划（按推荐顺序）

1. **Service 层** - 业务逻辑层
   - 把业务逻辑从 Controller 分离出来
   - 学习 `@Service` 注解

2. **JWT 认证** - 真正的登录功能
   - 用户注册/登录接口
   - JWT Token 生成和验证
   - 拦截器验证 Token

3. **MySQL 配置** - 切换到 MySQL
   - 安装 MySQL
   - 修改 `application.properties`

4. **实战项目** - 完整功能开发
   - 用户管理模块
   - 权限控制
   - 前后端联调

---

## 💡 学习方法记录

### 当前阶段：看懂 → 模仿

**用户反馈**：能看懂代码，但脱离 AI 写不出来

**调整策略**：
- 不追求完全脱离 AI
- 改为 AI 引导 + 用户尝试写
- 每一步先思考，再对比答案
- 多问"为什么要这样写"

### 好用的类比（前端 → 后端）

| 前端 | 后端 |
|------|------|
| Express/Koa | Spring MVC |
| Middleware | Interceptor |
| Prisma/TypeORM | JPA/Hibernate |
| Controller | Controller |
| Model/Schema | Entity |
| npm | Maven |
| package.json | pom.xml |
| .env | application.properties |

---

## 🐛 遇到的问题及解决

### 问题 1：启动报错 - UserRepository 找不到
**原因**：启动类排除了数据库自动配置
**解决**：移除 `@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})`
       添加 `@EnableJpaRepositories(basePackages = "com.example.demo.repository")`

### 问题 2：curl 请求返回 400 - JSON parse error
**原因**：PowerShell 中 JSON 引号格式错误
**解决**：使用 `curl -X POST ... -d '{"username":"zhangsan"}'`（双引号）

### 问题 3：查询返回空数组 []
**原因**：H2 内存数据库重启后数据清空
**解决**：先新增测试数据再查询

---

## 📖 核心概念速查

### Optional 类
```java
Optional<User> userOpt = userRepository.findById(1L);
userOpt.isPresent()   // 判断是否有值
userOpt.get()         // 取值
userOpt.orElse(null)  // 有值返回值，没有返回 null
```

### Result 工具类
```java
Result.success(data)           // 成功
Result.success("消息", data)   // 成功 + 自定义消息
Result.error("错误消息")        // 失败
Result.error(404, "不存在")     // 失败 + 状态码
```

### Repository 方法命名规则
```java
findByUsername(String username)           // WHERE username = ?
findByAgeGreaterThan(Integer age)         // WHERE age > ?
findByUsernameContaining(String keyword)  // WHERE username LIKE %?%
findByAgeBetween(Integer min, Integer max)// WHERE age BETWEEN ? AND ?
findAllByOrderByAgeDesc()                 // ORDER BY age DESC
```

---

## 🎯 下一步行动

**等待用户选择**：
- [ ] Service 层
- [ ] JWT 认证
- [ ] MySQL 配置
- [ ] 实战项目

---

*本文件由 AI 辅助维护，记录学习进度和关键知识点*
