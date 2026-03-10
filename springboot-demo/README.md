# Spring Boot Day 1 - 第一个 API

> 📍 位置：`D:\code\code\status\springboot-demo\`

---

## 📁 项目结构（已创建）

```
springboot-demo/
├── pom.xml                          # Maven 配置文件（类似 package.json）
├── src/main/
│   ├── java/com/example/demo/
│   │   ├── DemoApplication.java     # 启动类（入口文件）
│   │   └── controller/
│   │       └── HelloController.java # 控制器（路由处理）
│   └── resources/
│       └── application.properties   # 配置文件
```

---

## 🚀 运行方式

### 方式 1：IDEA（推荐）

1. 用 **IntelliJ IDEA** 打开 `springboot-demo` 文件夹
2. 找到 `src/main/java/com/example/demo/DemoApplication.java`
3. 点击文件中的绿色运行按钮（▶）
4. 或右键 → Run 'DemoApplication'

### 方式 2：Maven 命令行

```bash
# 进入项目目录
cd D:/code/code/status/springboot-demo

# 运行项目
mvn spring-boot:run
```

### 方式 3：先打包再运行

```bash
# 打包
mvn clean package

# 运行 JAR
java -jar target/springboot-demo-0.0.1-SNAPSHOT.jar
```

---

## 🌐 测试接口

启动成功后（看到 `Spring Boot 启动成功!`），访问：

### 1. Hello 接口
```
GET http://localhost:8080/hello
```
**返回：**
```
Hello, Spring Boot!
```

### 2. 用户信息接口
```
GET http://localhost:8080/api/user
```
**返回：**
```json
{
  "id": 1,
  "name": "张三",
  "age": 25,
  "email": "zhangsan@example.com"
}
```

### 3. 服务器时间接口
```
GET http://localhost:8080/api/server-time
```
**返回：**
```json
{
  "timestamp": 1710000000000,
  "message": "当前服务器时间"
}
```

---

## 📝 前端对比理解

| Spring Boot | 前端 Express | 说明 |
|-------------|--------------|------|
| `@RestController` | `express()` | 创建路由处理器 |
| `@GetMapping("/xxx")` | `app.get('/xxx')` | 定义 GET 路由 |
| `@PostMapping("/xxx")` | `app.post('/xxx')` | 定义 POST 路由 |
| 方法返回值 | `res.json()` | 返回响应 |
| `application.properties` | `.env` / `config.js` | 配置文件 |
| `pom.xml` | `package.json` | 依赖管理 |

---

## ✅ 今日任务

- [ ] 成功运行 Spring Boot 项目
- [ ] 访问 3 个测试接口
- [ ] 理解 @RestController 和 @GetMapping 的作用
- [ ] 修改 HelloController，添加一个新的接口

### 练习：添加一个自我介绍接口

在 `HelloController.java` 中添加：

```java
@GetMapping("/api/about")
public Map<String, Object> about() {
    Map<String, Object> info = new HashMap<>();
    info.put("name", "你的名字");
    info.put("role", "前端开发工程师");
    info.put("skill", "React/Vue/Node.js");
    return info;
}
```

重启项目，访问 `http://localhost:8080/api/about` 测试。

---

## 🆘 常见问题

### 1. 端口被占用（Port 8080 was already in use）

**解决：** 修改 `application.properties`
```properties
server.port=8081  # 改成其他端口
```

### 2. 找不到 main 方法

**原因：** 没打开正确的文件夹

**解决：** IDEA 打开 `springboot-demo` 文件夹（包含 pom.xml 的目录）

### 3. Maven 依赖下载慢

**解决：** 配置阿里云镜像

在 `pom.xml` 的 `<project>` 标签内添加：
```xml
<repositories>
    <repository>
        <id>aliyun</id>
        <url>https://maven.aliyun.com/repository/public</url>
    </repository>
</repositories>
```

### 4. 中文乱码

**解决：** 在 `application.properties` 添加
```properties
spring.http.encoding.force=true
spring.http.encoding.charset=UTF-8
```

---

## 📚 知识点总结

### 注解解释

| 注解 | 作用 | 类比 |
|------|------|------|
| `@SpringBootApplication` | 标记启动类 | `app.listen()` |
| `@RestController` | 标记控制器（返回 JSON） | `router` |
| `@GetMapping` | 映射 GET 请求 | `app.get()` |

### 返回类型

- `String` → 返回纯文本
- `Map<String, Object>` → 返回 JSON 对象
- `List<T>` → 返回 JSON 数组
- 自定义对象 → 返回 JSON 对象（需要 Getter）

---

## 🎯 明天预告（Day 2）

- 路径参数 `@PathVariable`
- 查询参数 `@RequestParam`
- 请求体 `@RequestBody`
- 实战：用户管理 API

---

**加油！你已经迈出了全栈开发的第一步！** 🚀
