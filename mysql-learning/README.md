# MySQL 学习项目

## 📖 项目说明

这是 `springboot-demo` 的进阶学习项目，专注于 **MySQL 数据库**功能。

### 与 springboot-demo 的区别

| 项目 | 内容 |
|------|------|
| `springboot-demo` | 基础 CRUD、H2 内存数据库 |
| `mysql-learning` | 在 springboot-demo 基础上连接 **真实 MySQL 数据库** |

---

## 📁 项目结构

```
mysql-learning/
├── pom.xml                              # Maven 配置（添加 MySQL 驱动）
├── README.md                            # 本文件
├── src/main/java/com/example/demo/
│   ├── common/
│   │   ├── Result.java                  # 统一返回结果
│   │   └── GlobalExceptionHandler.java  # 全局异常处理
│   ├── config/
│   │   └── WebConfig.java               # Web 配置（跨域等）
│   ├── controller/
│   │   ├── HelloController.java         # 测试接口
│   │   ├── UserController.java          # 用户 CRUD 接口（JPA 方式）
│   │   ├── NativeSqlController.java     # 原生 SQL 操作示例
│   │   ├── ParamsController.java        # 参数接收示例
│   │   └── XiongController.java         # 综合练习
│   ├── entity/
│   │   └── User.java                    # 用户实体类（映射 MySQL 表）
│   ├── interceptor/
│   │   ├── LoggingInterceptor.java      # 日志拦截器
│   │   └── AuthInterceptor.java         # 认证拦截器
│   ├── repository/
│   │   └── UserRepository.java          # 数据访问层
│   ├── service/
│   │   ├── UserService.java             # 服务接口
│   │   └── impl/
│   │       └── UserServiceImpl.java     # 服务实现
│   └── DemoApplication.java             # 启动类
└── src/main/resources/
    ├── application.properties           # 配置文件（MySQL 连接）
    └── schema.sql                       # 初始化 SQL 脚本
```

---

## 🚀 快速开始

### 1. 安装 MySQL

#### 方式 1：Docker（推荐）
```bash
docker run --name mysql-local \
  -e MYSQL_ROOT_PASSWORD=123456 \
  -p 3306:3306 \
  -d mysql:8.0
```

#### 方式 2：本地安装
下载地址：https://dev.mysql.com/downloads/mysql/

### 2. 创建数据库
```sql
CREATE DATABASE demo
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;
```

### 3. 修改配置

编辑 `application.properties`，确保 MySQL 配置正确：

```properties
# MySQL 配置
spring.datasource.url=jdbc:mysql://localhost:3306/demo?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf-8
spring.datasource.username=root
spring.datasource.password=123456
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA 配置
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.format-sql=true
```

### 4. 启动项目
```bash
cd mysql-learning
mvn spring-boot:run
```

---

## 📋 API 接口列表

### JPA 方式（/api/users）

| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/users` | GET | 获取所有用户 |
| `/api/users/{id}` | GET | 获取指定用户 |
| `/api/users` | POST | 创建用户 |
| `/api/users/{id}` | PUT | 更新用户 |
| `/api/users/{id}` | DELETE | 删除用户 |
| `/api/users/search/keyword?name=xxx` | GET | 模糊查询 |
| `/api/users/age?min=20&max=30` | GET | 年龄范围查询 |

### 原生 SQL 方式（/api/sql）

| 接口 | 方法 | 说明 |
|------|------|------|
| `/api/sql/users` | GET | 查询所有用户 |
| `/api/sql/users/{id}` | GET | 根据 ID 查询 |
| `/api/sql/users/by-username/{username}` | GET | 根据用户名查询 |
| `/api/sql/users/search?keyword=xxx` | GET | 模糊查询 |
| `/api/sql/users/age-range?min=20&max=30` | GET | 年龄范围查询 |
| `/api/sql/users/count` | GET | 统计用户数 |
| `/api/sql/users/avg-age` | GET | 平均年龄 |
| `/api/sql/users` | POST | 新增用户 |
| `/api/sql/users/{id}` | PUT | 更新用户 |
| `/api/sql/users/{id}/email` | PATCH | 部分更新 |
| `/api/sql/users/{id}` | DELETE | 删除用户 |
| `/api/sql/users/batch` | POST | 批量插入 |

---

## 💻 核心代码

### 1. 实体类 - 映射数据库表

```java
// User.java
@Entity
@Table(name = "users")  // 指定表名
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 自增主键
    private Long id;

    @Column(nullable = false, unique = true, length = 50)  // 非空、唯一、长度
    private String username;

    @Column(nullable = false, length = 100)
    private String email;

    private Integer age;

    @Column(name = "create_time")  // 指定列名（下划线命名）
    @CreationTimestamp  // 自动填充创建时间
    private Date createTime;
}
```

### 2. Repository - 数据访问层

```java
// UserRepository.java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 方法命名查询 - 框架自动生成 SQL
    Optional<User> findByUsername(String username);

    List<User> findByAgeGreaterThan(Integer age);

    List<User> findByEmailContaining(String keyword);
}
```

### 3. Service - 业务逻辑层

```java
// UserServiceImpl.java
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("用户不存在"));
    }

    @Override
    public User createUser(User user) {
        // 可以先检查用户名是否存在
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("用户名已存在");
        }
        return userRepository.save(user);
    }
}
```

### 4. 原生 SQL 操作（JdbcTemplate）

```java
// NativeSqlController.java
@RestController
@RequestMapping("/api/sql")
public class NativeSqlController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 查询所有
    @GetMapping("/users")
    public Result<List<User>> list() {
        String sql = "SELECT * FROM users ORDER BY id DESC";
        List<User> users = jdbcTemplate.query(sql, userRowMapper);
        return Result.success(users);
    }

    // 根据 ID 查询
    @GetMapping("/users/{id}")
    public Result<User> getById(@PathVariable Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        User user = jdbcTemplate.queryForObject(sql, userRowMapper, id);
        return Result.success(user);
    }

    // 新增用户
    @PostMapping("/users")
    public Result<User> create(@RequestBody User user) {
        String sql = "INSERT INTO users (username, password, email, age, nickname, create_time, update_time) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        int rows = jdbcTemplate.update(sql,
            user.getUsername(), user.getPassword(), user.getEmail(),
            user.getAge(), user.getNickname(),
            new Date(), new Date());
        return Result.success("创建成功", user);
    }

    // 更新用户
    @PutMapping("/users/{id}")
    public Result<User> update(@PathVariable Long id, @RequestBody User user) {
        String sql = "UPDATE users SET username=?, password=?, email=?, age=?, nickname=?, update_time=? WHERE id=?";
        int rows = jdbcTemplate.update(sql,
            user.getUsername(), user.getPassword(), user.getEmail(),
            user.getAge(), user.getNickname(),
            new Date(), id);
        return Result.success("更新成功", user);
    }

    // 删除用户
    @DeleteMapping("/users/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(sql, id);
        return Result.success("删除成功", null);
    }
}
```

### 5. JPA vs 原生 SQL 对比

| 特性 | JPA | 原生 SQL |
|------|-----|---------|
| 代码量 | 少 | 多 |
| 灵活性 | 低 | 高 |
| SQL 可见性 | 自动生成 | 手写 |
| 复杂查询 | 支持有限 | 完全支持 |
| 数据库移植 | 容易 | 较难 |
| 性能优化 | 依赖框架 | 手动优化 |

---

## 📝 MySQL vs H2 对比

| 特性 | H2 | MySQL |
|------|----|-------|
| 类型 | 内存数据库 | 关系型数据库 |
| 安装 | 无需安装 | 需要安装 |
| 持久化 | 重启数据丢失 | 数据持久化 |
| 适用场景 | 开发测试 | 生产环境 |
| 性能 | 快（内存） | 取决于配置 |
| 并发 | 低 | 高 |

---

## 🎯 学习步骤

### 第 1 步：理解 MySQL 安装配置
- 安装 MySQL（本地或 Docker）
- 创建数据库
- 设置字符集和时区

### 第 2 步：理解配置文件
- 修改 `application.properties`
- 配置数据库连接
- 理解 JPA 相关配置

### 第 3 步：理解实体映射
- `@Entity` 注解
- `@Table` 指定表名
- `@Column` 指定列属性
- 主键生成策略

### 第 4 步：理解 Repository（JPA 方式）
- 继承 `JpaRepository`
- 方法命名查询
- 自定义查询

### 第 5 步：理解 Service 层
- 业务逻辑处理
- 异常处理
- 事务管理

### 第 6 步：理解原生 SQL（JdbcTemplate）
- `JdbcTemplate` 注入
- `query()` 查询方法
- `update()` 增删改方法
- `RowMapper` 结果映射

---

## 🔧 常见问题

### 1. 连接失败
```
错误：Communications link failure
原因：MySQL 未启动或端口不对
解决：检查 MySQL 服务状态，确认 3306 端口可访问
```

### 2. 时区问题
```
错误：The server time zone value is unrecognized
解决：URL 添加 &serverTimezone=Asia/Shanghai
```

### 3. 字符集问题
```
错误：中文乱码
解决：URL 添加 &characterEncoding=utf-8
      数据库使用 utf8mb4 字符集
```

### 4. 认证插件问题
```
错误：Public Key Retrieval is not allowed
解决：URL 添加 &allowPublicKeyRetrieval=true
```

---

## 📚 扩展学习

完成本模块后，可以继续学习：

1. **多表关联** - `@OneToMany`、`@ManyToOne` 等关联映射
2. **复杂查询** - JPQL、原生 SQL 查询
3. **事务管理** - `@Transactional` 注解
4. **分页排序** - `Pageable`、`Sort` 接口
5. **连接池** - HikariCP 配置优化

---

*创建时间：2026-03-17*
