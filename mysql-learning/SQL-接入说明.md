# MySQL 接入配置说明

## 📌 核心配置三要素

### 1. pom.xml - 添加依赖

```xml
<!-- MySQL 驱动 -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.33</version>
</dependency>

<!-- Spring Boot JDBC（用于原生 SQL 操作） -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>

<!-- Spring Boot JPA（可选，用于 JPA 方式） -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

### 2. application.properties - 数据库连接

```properties
# 数据库连接 URL
spring.datasource.url=jdbc:mysql://localhost:3306/demo?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf-8&allowPublicKeyRetrieval=true

# 数据库用户名和密码
spring.datasource.username=root
spring.datasource.password=123456

# MySQL 驱动类名
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

### 3. 代码注入 - 使用数据库

#### 方式 1：JPA 方式（推荐简单场景）

```java
// Repository 层
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}

// Service 层
@Service
public class UserServiceImpl {
    @Autowired
    private UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();  // 自动生成 SELECT * FROM users
    }
}
```

#### 方式 2：原生 SQL 方式（推荐复杂查询）

```java
@RestController
@RequestMapping("/api/sql")
public class NativeSqlController {

    @Autowired
    private JdbcTemplate jdbcTemplate;  // Spring 自动注入

    // 查询
    @GetMapping("/users")
    public List<User> list() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, userRowMapper);
    }

    // 新增
    @PostMapping("/users")
    public int create(User user) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        return jdbcTemplate.update(sql, user.getUsername(), user.getPassword());
    }

    // 更新
    @PutMapping("/users/{id}")
    public int update(Long id, User user) {
        String sql = "UPDATE users SET username=? WHERE id=?";
        return jdbcTemplate.update(sql, user.getUsername(), id);
    }

    // 删除
    @DeleteMapping("/users/{id}")
    public int delete(Long id) {
        String sql = "DELETE FROM users WHERE id=?";
        return jdbcTemplate.update(sql, id);
    }
}
```

---

## 🔧 Spring Boot 自动配置原理

### 启动流程

```
1. Spring Boot 启动
       ↓
2. 读取 application.properties 配置
       ↓
3. 发现 spring.datasource.* 配置
       ↓
4. 自动创建 DataSource Bean
       ↓
5. 自动创建 JdbcTemplate Bean
       ↓
6. 自动创建 EntityManagerFactory (如果有 JPA 依赖)
       ↓
7. 扫描 @Repository、@Service、@Controller
       ↓
8. 注入依赖，应用启动完成
```

### 自动配置类

Spring Boot 通过以下自动配置类完成数据库接入：

- `DataSourceAutoConfiguration` - 自动配置数据源
- `DataSourceTransactionManagerAutoConfiguration` - 自动配置事务管理器
- `JdbcTemplateAutoConfiguration` - 自动配置 JdbcTemplate
- `HibernateJpaAutoConfiguration` - 自动配置 JPA

**你不需要手动配置这些，Spring Boot 会自动处理！**

---

## ✅ 验证接入是否成功

### 1. 启动项目

```bash
cd mysql-learning
mvn spring-boot:run
```

### 2. 查看控制台日志

如果看到以下内容，说明接入成功：

```
✅ HikariPool-1 - Start completed.
✅ Initialized JPA EntityManagerFactory for persistence unit 'default'
✅ Spring Boot 启动成功!
```

### 3. 测试接口

```bash
# 测试 JPA 方式
curl http://localhost:18080/api/users

# 测试原生 SQL 方式
curl http://localhost:18080/api/sql/users
```

### 4. 查看 SQL 执行日志

配置文件中开启：

```properties
spring.jpa.show-sql=true
spring.jpa.format-sql=true
logging.level.org.springframework.jdbc=DEBUG
```

---

## ⚠️ 常见问题

### 1. 驱动找不到

```
错误：Cannot load class 'com.mysql.cj.jdbc.Driver'
解决：检查 pom.xml 是否有 MySQL 依赖，运行 mvn install
```

### 2. 连接失败

```
错误：Communications link failure
解决：
  1. 确认 MySQL 已启动
  2. 确认端口 3306 可访问
  3. 检查 URL 是否正确
```

### 3. 认证失败

```
错误：Access denied for user 'root'@'localhost'
解决：检查用户名密码是否正确
```

### 4. 数据库不存在

```
错误：Unknown database 'demo'
解决：先创建数据库
  CREATE DATABASE demo CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 5. 时区错误

```
错误：The server time zone value is unrecognized
解决：URL 添加 &serverTimezone=Asia/Shanghai
```

### 6. 公钥检索错误

```
错误：Public Key Retrieval is not allowed
解决：URL 添加 &allowPublicKeyRetrieval=true
```

---

## 📚 参考文件

- `pom.xml` - Maven 依赖配置
- `application.properties` - 数据库连接配置
- `DatabaseConfig.java` - 手动配置示例（可选）
- `NativeSqlController.java` - 原生 SQL 使用示例
- `UserRepository.java` - JPA 使用示例
- `sql/05-full-example.sql` - 完整 SQL 脚本

---

*创建时间：2026-03-17*
