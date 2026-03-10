# Day 8 - Spring Boot 项目搭建与结构解析

**学习日期：** 2026-03-10
**预计时间：** 2-3 小时
**完成状态：** ⬜ 未开始 → 🟡 学习中 → ✅ 完成

---

## 📚 今日学习目标

1. 理解 Spring Boot 项目结构
2. 掌握 pom.xml 依赖配置
3. 理解 application.properties 配置文件
4. 理解启动类的作用
5. 创建一个完整的 Controller

---

## 一、Spring Boot 项目结构

### 1.1 标准目录结构

```
springboot-demo/
├── src/
│   ├── main/
│   │   ├── java/                    # Java 源代码
│   │   │   └── com.example.demo/
│   │   │       ├── DemoApplication.java    # 启动类
│   │   │       ├── controller/             # 控制器（路由）
│   │   │       ├── service/                # 业务逻辑层
│   │   │       ├── repository/             # 数据访问层
│   │   │       ├── entity/                 # 实体类（数据库表）
│   │   │       └── dto/                    # 数据传输对象
│   │   └── resources/
│   │       ├── application.properties      # 配置文件
│   │       ├── application.yml           # YAML 格式配置（可选）
│   │       └── static/                   # 静态资源
│   └── test/                              # 测试代码
├── pom.xml                                # Maven 配置文件
└── README.md
```

### 1.2 分层架构（重要！）

```
┌─────────────────────────────────────┐
│         Controller 层                │  ← 处理 HTTP 请求（路由）
│         (路由控制器)                 │     类似 Express 的 router
├─────────────────────────────────────┤
│         Service 层                   │  ← 业务逻辑
│         (业务服务)                   │     处理具体业务规则
├─────────────────────────────────────┤
│         Repository 层                │  ← 数据库操作
│         (数据访问层)                 │     类似前端的 Model
├─────────────────────────────────────┤
│         Entity 层                    │  ← 实体类
│         (数据库表模型)               │     对应数据库表结构
└─────────────────────────────────────┘
```

**类比前端 MVC 架构：**
```
Spring Boot          前端（React/Express）
──────────           ────────────────────
Controller     →     Controller/Router
Service        →     Service/业务逻辑
Repository     →     Model/数据库操作
Entity         →     Schema/数据模型
```

---

## 二、pom.xml 详解

### 2.1 什么是 Maven？

Maven = Java 的项目构建工具 + 包管理器

**类比前端：**
```
Maven          前端工具
──────         ──────────
pom.xml   →    package.json
mvn install →  npm install
mvn run     →  npm run dev
```

### 2.2 pom.xml 核心结构

```xml
<!-- 1. 项目坐标（唯一标识） -->
<groupId>com.example</groupId>      <!-- 公司域名反写 -->
<artifactId>springboot-demo</artifactId> <!-- 项目名 -->
<version>0.0.1-SNAPSHOT</version>   <!-- 版本号 -->

<!-- 2. 父项目（继承配置） -->
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.7.18</version>
</parent>

<!-- 3. 依赖（类似 dependencies） -->
<dependencies>
    <!-- Web 开发必备 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- 数据库操作 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <!-- MySQL 驱动 -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.33</version>
    </dependency>

    <!-- 简化代码的插件 -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>
</dependencies>

<!-- 4. 构建插件 -->
<build>
    <plugins>
        <!-- Spring Boot 插件：打包、运行 -->
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```

### 2.3 常用依赖说明

| 依赖 | 作用 | 前端类比 |
|------|------|---------|
| `spring-boot-starter-web` | Web 开发（MVC + Tomcat + JSON） | express + koa |
| `spring-boot-starter-data-jpa` | 数据库操作（JPA/Hibernate） | TypeORM / Prisma |
| `mysql-connector-java` | MySQL 数据库驱动 | mysql2 |
| `lombok` | 简化代码（注解生成 Getter/Setter） | - |
| `spring-boot-starter-test` | 测试框架 | Jest / Mocha |

---

## 三、配置文件详解

### 3.1 application.properties 基础

```properties
# ================== 服务器配置 ==================
# 服务端口（默认 8080）
server.port=8080

# 应用名称
spring.application.name=my-app

# ================== 数据库配置 ==================
# 数据库连接 URL
spring.datasource.url=jdbc:mysql://localhost:3306/mydb?useSSL=false&serverTimezone=Asia/Shanghai

# 用户名和密码
spring.datasource.username=root
spring.datasource.password=root

# 数据库驱动
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# ================== JPA 配置 ==================
# 自动管理表结构
spring.jpa.hibernate.ddl-auto=update

# 显示 SQL 语句
spring.jpa.show-sql=true

# ================== 日志配置 ==================
# 日志级别：ERROR > WARN > INFO > DEBUG > TRACE
logging.level.com.example=DEBUG
```

### 3.2 properties vs yml

**properties 格式：**
```properties
server.port=8080
spring.datasource.url=jdbc:mysql://localhost:3306/mydb
spring.datasource.username=root
spring.datasource.password=root
```

**yml 格式（更简洁）：**
```yaml
server:
  port: 8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mydb
    username: root
    password: root
```

---

## 四、启动类解析

### 4.1 标准启动类

```java
package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot 启动类
 * 类比：Express 的 app.listen()
 */
@SpringBootApplication  // 核心注解
public class DemoApplication {

    public static void main(String[] args) {
        // 启动 Spring Boot
        SpringApplication.run(DemoApplication.class, args);
    }
}
```

### 4.2 @SpringBootApplication 解析

这个注解是三个注解的组合：

```java
// @SpringBootApplication = 以下三个注解的组合

@Configuration          // 1. 这是一个配置类
@EnableAutoConfiguration // 2. 启用自动配置（Spring Boot 的魔法）
@ComponentScan          // 3. 自动扫描包下的组件
```

**自动配置原理：**
- 扫描 classpath 下的依赖
- 自动配置相应的 Bean
- 例如：检测到 spring-boot-starter-web → 自动配置 Tomcat + MVC

---

## 五、Controller 详解

### 5.1 创建第一个 Controller

```java
package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController  // 标记这是一个 REST 控制器（返回 JSON）
public class HelloController {

    @GetMapping("/hello")  // 映射 GET /hello
    public String hello() {
        return "Hello, Spring Boot!";
    }
}
```

### 5.2 常用请求注解

| 注解 | HTTP 方法 | 前端类比 |
|------|----------|---------|
| `@GetMapping("/xxx")` | GET | app.get('/xxx') |
| `@PostMapping("/xxx")` | POST | app.post('/xxx') |
| `@PutMapping("/xxx")` | PUT | app.put('/xxx') |
| `@DeleteMapping("/xxx")` | DELETE | app.delete('/xxx') |
| `@PatchMapping("/xxx")` | PATCH | app.patch('/xxx') |

### 5.3 接收参数的三种方式

```java
// ========== 1. 路径参数（@PathVariable）==========
// 请求：GET /user/123
@GetMapping("/user/{id}")
public User getUser(@PathVariable Long id) {
    // id = 123
}

// ========== 2. 查询参数（@RequestParam）==========
// 请求：GET /search?keyword=java&page=1
@GetMapping("/search")
public List<User> search(
    @RequestParam String keyword,
    @RequestParam(defaultValue = "1") int page
) {
    // keyword = "java", page = 1
}

// ========== 3. 请求体（@RequestBody）==========
// 请求：POST /user  Content-Type: application/json
// Body: {"name": "张三", "age": 25}
@PostMapping("/user")
public User createUser(@RequestBody User user) {
    // user.name = "张三", user.age = 25
}
```

---

## 六、实战练习

### 练习 1：创建一个返回当前时间的接口

```java
@GetMapping("/api/now")
public Map<String, Object> now() {
    Map<String, Object> result = new HashMap<>();
    result.put("now", System.currentTimeMillis());
    result.put("date", new java.util.Date());
    return result;
}
```

### 练习 2：创建一个接收两个数相加的接口

```java
@GetMapping("/api/add")
public Map<String, Object> add(
    @RequestParam int a,
    @RequestParam int b
) {
    Map<String, Object> result = new HashMap<>();
    result.put("result", a + b);
    return result;
}
// 访问：/api/add?a=5&b=3 → {"result": 8}
```

### 练习 3：创建一个获取用户信息的接口

```java
@GetMapping("/api/users/{id}")
public Map<String, Object> getUser(@PathVariable Long id) {
    Map<String, Object> user = new HashMap<>();
    user.put("id", id);
    user.put("name", "用户" + id);
    user.put("email", "user" + id + "@example.com");
    return user;
}
// 访问：/api/users/10 → {"id":10, "name":"用户 10", ...}
```

---

## 七、运行项目

### 方式 1：IDEA 运行
右键 `DemoApplication.java` → Run

### 方式 2：Maven 命令
```bash
mvn spring-boot:run
```

### 方式 3：打包后运行
```bash
mvn clean package
java -jar target/springboot-demo-0.0.1-SNAPSHOT.jar
```

### 测试接口
```bash
# 浏览器访问
http://localhost:8080/hello
http://localhost:8080/api/user

# 或用 curl
curl http://localhost:8080/api/server-time
```

---

## ✅ 今日检查清单

- [ ] 理解 Spring Boot 目录结构
- [ ] 理解 pom.xml 各部分作用
- [ ] 理解配置文件的用途
- [ ] 理解启动类的工作原理
- [ ] 能创建一个简单的 Controller
- [ ] 能运行项目并访问接口
- [ ] 完成 3 个实战练习

---

## 📝 笔记

> 在此处记录学习过程中的疑问、心得

---

## 🔗 参考资料

- [Spring Boot 官方文档](https://spring.io/projects/spring-boot)
- [Maven 入门教程](https://maven.apache.org/guides/getting-started/)
- [你的项目代码](../springboot-demo/src/main/java/com/example/demo/)

*创建日期：2026-03-10*
