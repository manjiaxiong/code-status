# Spring Boot Demo 项目结构说明

## 📁 目录结构

```
springboot-demo/
├── pom.xml                                          # Maven 配置文件
├── README.md                                        # 项目使用说明
├── PROJECT_STRUCTURE.md                             # 项目结构说明（本文件）
├── JWT 学习文档.md                                    # JWT 认证学习文档
├── src/
│   ├── main/
│   │   ├── java/com/example/demo/
│   │   │   ├── DemoApplication.java                 # Spring Boot 启动类
│   │   │   ├── config/                              # 配置类目录
│   │   │   │   └── WebConfig.java                   # Web 配置（拦截器注册、跨域）
│   │   │   ├── controller/                          # 控制器层（处理 HTTP 请求）
│   │   │   │   ├── HelloController.java             # 基础接口示例
│   │   │   │   ├── ParamsController.java            # 参数接收示例
│   │   │   │   ├── UserController.java              # 用户管理 CRUD
│   │   │   │   ├── XiongController.java             # 测试接口
│   │   │   │   ├── AuthController.java              # JWT 认证控制器【新增】
│   │   │   │   └── RegisterRequest.java             # 注册请求 DTO【新增】
│   │   │   ├── entity/                              # 实体层（数据库映射）
│   │   │   │   └── User.java                        # 用户实体类
│   │   │   ├── repository/                          # 数据访问层（DAO）
│   │   │   │   └── UserRepository.java              # 用户数据访问接口
│   │   │   ├── service/                             # 业务逻辑层
│   │   │   │   ├── UserService.java                 # 用户服务接口
│   │   │   │   └── impl/
│   │   │   │       └── UserServiceImpl.java         # 用户服务实现
│   │   │   ├── interceptor/                         # 拦截器层
│   │   │   │   ├── LoggingInterceptor.java          # 请求日志拦截器
│   │   │   │   └── AuthInterceptor.java             # 权限认证拦截器
│   │   │   ├── jwt/                                 # JWT 模块【新增】
│   │   │   │   ├── JwtTokenProvider.java            # JWT Token 生成/验证工具
│   │   │   │   ├── JwtAuthService.java              # JWT 认证服务
│   │   │   │   ├── LoginRequest.java                # 登录请求 DTO
│   │   │   │   └── LoginResponse.java               # 登录响应 DTO
│   │   │   └── common/                              # 公共类
│   │   │       ├── Result.java                      # 统一返回结果封装
│   │   │       └── GlobalExceptionHandler.java      # 全局异常处理器
│   │   └── resources/
│   │       └── application.properties               # 应用配置文件
│   └── test/                                        # 测试代码目录
└── target/                                          # Maven 编译输出目录
```

---

## 📋 分层架构说明

本项目采用经典的 **MVC 分层架构**：

```
┌─────────────────────────────────────────────────┐
│              Controller 层                       │
│  (HelloController, ParamsController, ...)       │
│  职责：接收 HTTP 请求，调用 Service，返回响应     │
├─────────────────────────────────────────────────┤
│              Service 层                          │
│  (UserService, UserServiceImpl)                 │
│  职责：业务逻辑处理，事务管理                    │
├─────────────────────────────────────────────────┤
│              Repository 层                       │
│  (UserRepository)                               │
│  职责：数据库操作，ORM 映射                       │
├─────────────────────────────────────────────────┤
│              Entity 层                           │
│  (User)                                         │
│  职责：数据模型，数据库表映射                    │
└─────────────────────────────────────────────────┘
```

---

## 🔧 各模块详细说明

### 1. 启动类 - `DemoApplication.java`

```
位置：src/main/java/com/example/demo/DemoApplication.java
作用：Spring Boot 应用入口，包含 main 方法
注解：@SpringBootApplication
```

### 2. 配置层 - `config/`

| 文件 | 说明 |
|------|------|
| `WebConfig.java` | Web MVC 配置类，注册拦截器、配置跨域 |

### 3. 控制器层 - `controller/`

| 文件 | 接口路径 | 说明 |
|------|----------|------|
| `HelloController.java` | `/hello`, `/api/user`, `/api/server-time` | 基础示例接口 |
| `ParamsController.java` | `/api/params/query`, `/api/params/path/{id}`, `/api/params/body` | 参数接收示例 |
| `UserController.java` | `/api/users` | 用户 CRUD 接口 |
| `XiongController.java` | `/xiong` | 测试接口 |
| `AuthController.java` | `/api/auth/login`, `/api/auth/register`, `/api/auth/me` | JWT 认证接口【新增】 |
| `RegisterRequest.java` | - | 注册请求 DTO【新增】 |

### 4. 实体层 - `entity/`

| 文件 | 说明 |
|------|------|
| `User.java` | 用户实体，使用 JPA 注解映射到 `users` 表 |

### 5. 数据访问层 - `repository/`

| 文件 | 说明 |
|------|------|
| `UserRepository.java` | 继承 JpaRepository，提供 CRUD 和自定义查询方法 |

### 6. 业务逻辑层 - `service/`

| 文件 | 说明 |
|------|------|
| `UserService.java` | 用户服务接口，定义业务方法 |
| `UserServiceImpl.java` | 用户服务实现，使用 `@Transactional` 事务管理 |

### 7. 拦截器层 - `interceptor/`

| 文件 | 说明 |
|------|------|
| `LoggingInterceptor.java` | 记录请求日志（请求方法、URL、耗时等） |
| `AuthInterceptor.java` | Token 认证，支持白名单配置 |

### 8. 公共类 - `common/`

| 文件 | 说明 |
|------|------|
| `Result.java` | 统一 API 返回格式：`{code, message, data}` |
| `GlobalExceptionHandler.java` | 全局异常捕获，统一错误响应 |

### 9. JWT 模块 - `jwt/`【新增】

| 文件 | 说明 |
|------|------|
| `JwtTokenProvider.java` | JWT Token 生成、验证、解析工具类 |
| `JwtAuthService.java` | 登录/注册业务逻辑，Token 相关服务 |
| `LoginRequest.java` | 登录请求 DTO（接收 username/password） |
| `LoginResponse.java` | 登录响应 DTO（返回 token 和用户信息） |

---

## ⚙️ 配置文件

### `application.properties`

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
spring.h2.console.path=/h2-console
```

---

## 🛠️ 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Java | 1.8 | 开发语言 |
| Spring Boot | 2.7.18 | 核心框架 |
| Spring Data JPA | - | ORM 框架 |
| H2 Database | - | 内存数据库（开发环境） |
| Lombok | - | 简化代码 |
| Maven | - | 构建工具 |
| JWT (io.jsonwebtoken) | 0.11.5 | JWT Token 生成和验证 |

---

## 🚀 快速开始

```bash
# 1. 进入项目目录
cd springboot-demo

# 2. 编译项目
mvn clean install

# 3. 运行项目
mvn spring-boot:run

# 4. 访问测试接口
curl http://localhost:18080/hello
```

---

## 📖 学习路线参考

本项目作为 Java 后端学习项目，建议按以下顺序学习：

1. **入门**：`HelloController.java` - 理解 RESTful 接口
2. **参数处理**：`ParamsController.java` - 学习各类参数接收方式
3. **完整 CRUD**：`UserController.java` + `UserService` + `UserRepository` - 掌握完整开发流程
4. **拦截器**：`interceptor/` - 学习拦截器、认证授权
5. **工程化**：`Result.java` + `GlobalExceptionHandler.java` - 学习规范化开发
6. **JWT 认证**：`jwt/` + `AuthController.java` - 学习用户登录、Token 认证（★ 新增）

---

## 📝 更新日志

| 日期 | 更新内容 |
|------|----------|
| 2026-03-16 | 新增 JWT 认证模块（JwtTokenProvider、JwtAuthService、AuthController） |
| 2026-03-16 | 新增 `JWT 学习文档.md` |

---

*最后更新：2026-03-16*
