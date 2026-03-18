# MySQL 数据库学习指南

> 📚 配合 Spring Boot JPA 学习
> 当前位置：`D:\code\code\status\springboot-demo\`

---

## 📋 目录

1. [MySQL 基础](#mysql-基础)
2. [SQL 语法速查](#sql-语法速查)
3. [JPA Repository 详解](#jpa-repository-详解)
4. [实战练习](#实战练习)

---

## 🗄️ MySQL 基础

### 什么是 MySQL？

MySQL 是一个**关系型数据库管理系统**，用于存储和管理数据。

**类比前端：**
- MySQL = 浏览器 localStorage/IndexedDB 的超级加强版
- 可以持久化存储数据，断电不丢失
- 支持复杂查询、事务、索引等

### 数据库层级结构

```
MySQL Server (数据库服务器)
    └── Database (数据库)  ← 我们创建的 `demo` 数据库
          └── Table (表)   ← 如 `users` 表
                └── Row (行)  ← 数据记录
```

**类比前端：**
```
MySQL          ↔     浏览器
─────────────────────────────
Database       ↔     域名 (origin)
Table          ↔     localStorage key
Row            ↔     JSON 对象
Column         ↔     JSON 的字段
```

---

## 📝 SQL 语法速查

### 1. 创建数据库

```sql
-- 创建数据库
CREATE DATABASE demo
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE demo;

-- 查看当前有哪些数据库
SHOW DATABASES;

-- 删除数据库
DROP DATABASE demo;
```

### 2. 创建表（DDL - Data Definition Language）

```sql
-- 创建 users 表
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,  -- 主键，自增
    username VARCHAR(50) NOT NULL UNIQUE,   -- 用户名，非空且唯一
    email VARCHAR(100),                      -- 邮箱
    age INT,                                 -- 年龄
    password VARCHAR(255) NOT NULL,          -- 密码
    nickname VARCHAR(50),                    -- 昵称
    create_time DATETIME,                    -- 创建时间
    update_time DATETIME,                    -- 更新时间
    INDEX idx_username (username),           -- 索引，加速查询
    INDEX idx_email (email)                  -- 索引
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

**字段类型对照表：**

| MySQL 类型 | Java 类型 | 前端类型 | 说明 |
|-----------|----------|---------|------|
| `INT` | `Integer` | `number` | 整数 |
| `BIGINT` | `Long` | `number/string` | 大整数 |
| `VARCHAR(n)` | `String` | `string` | 可变长度字符串 |
| `TEXT` | `String` | `string` | 长文本 |
| `DATETIME` | `Date` | `string(ISO)` | 日期时间 |
| `BOOLEAN` | `Boolean` | `boolean` | 布尔值 |
| `DECIMAL(10,2)` | `BigDecimal` | `number` | 精确小数（金额） |

### 3. 增删改查（CRUD - DML）

```sql
-- ==================== CREATE（插入） ====================
INSERT INTO users (username, email, age, password, nickname)
VALUES ('zhangsan', 'zhangsan@example.com', 25, '123456', '张三');

-- 批量插入
INSERT INTO users (username, email, age, password, nickname) VALUES
('lisi', 'lisi@example.com', 28, '123456', '李四'),
('wangwu', 'wangwu@example.com', 22, '123456', '王五');

-- ==================== READ（查询） ====================
-- 查询所有
SELECT * FROM users;

-- 查询指定字段
SELECT id, username, email FROM users;

-- 条件查询
SELECT * FROM users WHERE age > 25;
SELECT * FROM users WHERE username = 'zhangsan';
SELECT * FROM users WHERE email LIKE '%@gmail.com';  -- 模糊查询

-- 排序
SELECT * FROM users ORDER BY age DESC;  -- 年龄降序
SELECT * FROM users ORDER BY create_time ASC;  -- 时间升序

-- 分页（非常重要！）
SELECT * FROM users LIMIT 10 OFFSET 0;  -- 第一页，每页 10 条
SELECT * FROM users LIMIT 10 OFFSET 10; -- 第二页

-- 聚合函数
SELECT COUNT(*) FROM users;              -- 总数
SELECT AVG(age) FROM users;              -- 平均年龄
SELECT MAX(age), MIN(age) FROM users;    -- 最大/最小年龄

-- 分组查询
SELECT age, COUNT(*) FROM users GROUP BY age;

-- ==================== UPDATE（更新） ====================
-- 更新单个字段
UPDATE users SET email = 'new@example.com' WHERE id = 1;

-- 更新多个字段
UPDATE users
SET age = 26, nickname = '小张'
WHERE id = 1;

-- 批量更新
UPDATE users SET age = age + 1 WHERE age > 20;

-- ==================== DELETE（删除） ====================
-- 删除单条
DELETE FROM users WHERE id = 1;

-- 删除多条
DELETE FROM users WHERE age < 18;

-- 删除所有（危险！）
DELETE FROM users;  -- 慎用！
```

### 4. 条件查询详解

```sql
-- 比较运算符
SELECT * FROM users WHERE age = 25;   -- 等于
SELECT * FROM users WHERE age != 25;  -- 不等于
SELECT * FROM users WHERE age > 25;   -- 大于
SELECT * FROM users WHERE age >= 25;  -- 大于等于
SELECT * FROM users WHERE age < 25;   -- 小于
SELECT * FROM users WHERE age <= 25;  -- 小于等于

-- 逻辑运算符
SELECT * FROM users WHERE age > 20 AND age < 30;     -- 与
SELECT * FROM users WHERE age < 20 OR age > 50;      -- 或
SELECT * FROM users WHERE NOT age = 25;              -- 非

-- 范围查询
SELECT * FROM users WHERE age BETWEEN 20 AND 30;     -- 20 到 30 之间
SELECT * FROM users WHERE age IN (20, 25, 30);       -- 是指定值之一

-- 模糊查询
SELECT * FROM users WHERE username LIKE 'zhang%';    -- 张开头
SELECT * FROM users WHERE username LIKE '%san';      -- san 结尾
SELECT * FROM users WHERE username LIKE '%a%';       -- 包含 a
SELECT * FROM users WHERE email LIKE '%@gmail.com';  -- Gmail 邮箱

-- NULL 判断
SELECT * FROM users WHERE email IS NULL;             -- 邮箱为空
SELECT * FROM users WHERE email IS NOT NULL;         -- 邮箱不为空
```

---

## 🔧 JPA Repository 详解

### JPA 是什么？

JPA (Java Persistence API) 是一个**ORM 框架**，让你用面向对象的方式操作数据库，不用写 SQL。

**类比前端：**
- JPA = TypeORM / Prisma / Sequelize
-  Repository 方法名自动生成 SQL

### 你的代码解析

#### 1. 实体类 `User.java`

```java
@Entity  // 标记为实体类，JPA 会管理
@Table(name = "users")  // 指定表名
@Data  // Lombok：自动生成 Getter/Setter
public class User {

    @Id  // 主键
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 数据库自增
    private Long id;

    @Column(nullable = false, unique = true, length = 50)  // 列约束
    private String username;

    // ... 其他字段
}
```

**对应的 SQL：**
```sql
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    -- ...
);
```

#### 2. Repository `UserRepository.java`

```java
public interface UserRepository extends JpaRepository<User, Long> {

    // ===== 内置方法（自动实现） =====
    save(User user)        // 保存（INSERT 或 UPDATE）
    findById(Long id)      // SELECT * FROM users WHERE id = ?
    findAll()              // SELECT * FROM users
    deleteById(Long id)    // DELETE FROM users WHERE id = ?
    count()                // SELECT COUNT(*) FROM users
    existsById(Long id)    // SELECT EXISTS(SELECT 1 FROM users WHERE id = ?)

    // ===== 自定义方法（根据方法名自动生成 SQL） =====

    // SELECT * FROM users WHERE username = ?
    Optional<User> findByUsername(String username);

    // SELECT * FROM users WHERE email = ?
    User findByEmail(String email);

    // SELECT * FROM users WHERE age BETWEEN ? AND ?
    List<User> findByAgeBetween(Integer minAge, Integer maxAge);

    // SELECT * FROM users WHERE age > ?
    List<User> findByAgeGreaterThan(Integer age);

    // SELECT * FROM users WHERE username LIKE '%?%'
    List<User> findByUsernameContaining(String keyword);

    // SELECT * FROM users WHERE username = ? AND email = ?
    User findByUsernameAndEmail(String username, String email);

    // SELECT * FROM users ORDER BY age DESC, create_time ASC
    List<User> findAllByOrderByAgeDescCreateTimeAsc();
}
```

#### 3. Service 层 `UserService.java`

```java
@Service  // 标记为服务类
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();  // 调用 Repository
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);  // 保存用户
    }

    // ... 其他方法
}
```

#### 4. Controller 层 `UserController.java`

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public Result<List<User>> list() {
        return Result.success(userService.findAll());
    }

    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable Long id) {
        return userService.findById(id)
            .map(Result::success)
            .orElse(Result.error(404, "用户不存在"));
    }

    // ... 其他接口
}
```

---

## 🎯 实战练习

### 练习 1：连接 MySQL 数据库

#### 步骤 1：确认 MySQL 已安装

```bash
# 检查 MySQL 服务
mysql --version

# 或者进入 MySQL 命令行
mysql -u root -p
# 输入密码后进入
```

#### 步骤 2：创建数据库

```sql
CREATE DATABASE demo
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;
```

#### 步骤 3：配置 application.properties

```properties
# 当前配置
spring.datasource.url=jdbc:mysql://localhost:3306/demo?useSSL=false&serverTimezone=UTC&characterEncoding=utf-8
spring.datasource.username=root
spring.datasource.password=123456
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA 配置
spring.jpa.hibernate.ddl-auto=update  # 自动创建/更新表
spring.jpa.show-sql=true              # 显示执行的 SQL
```

#### 步骤 4：启动项目验证

```bash
cd springboot-demo
mvn spring-boot:run
```

看到 `Started DemoApplication` 表示启动成功！

### 练习 2：测试 API

```bash
# 1. 创建用户
curl -X POST http://localhost:18080/api/users \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"test1\",\"email\":\"test1@example.com\",\"age\":25,\"password\":\"123456\",\"nickname\":\"测试 1\"}"

# 2. 查询所有用户
curl http://localhost:18080/api/users

# 3. 根据 ID 查询
curl http://localhost:18080/api/users/1

# 4. 更新用户
curl -X PUT http://localhost:18080/api/users/1 \
  -H "Content-Type: application/json" \
  -d "{\"id\":1,\"username\":\"test1_updated\",\"email\":\"test1@example.com\",\"age\":26,\"password\":\"123456\",\"nickname\":\"测试更新\"}"

# 5. 删除用户
curl -X DELETE http://localhost:18080/api/users/1

# 6. 模糊查询
curl "http://localhost:18080/api/users/search/keyword?name=test"

# 7. 年龄范围查询
curl "http://localhost:18080/api/users/age?min=20&max=30"
```

### 练习 3：直接操作数据库

```bash
# 进入 MySQL 命令行
mysql -u root -p

# 切换到 demo 数据库
USE demo;

# 查看表
SHOW TABLES;

# 查看 users 表结构
DESCRIBE users;

# 查询数据
SELECT * FROM users;

# 插入测试数据
INSERT INTO users (username, email, age, password, nickname, create_time, update_time)
VALUES ('zhangsan', 'zhangsan@example.com', 25, '123456', '张三', NOW(), NOW());

# 退出
EXIT;
```

---

## 📊 JPA 命名规则速查

| 方法关键字 | SQL 条件 | 示例 |
|-----------|---------|------|
| `FindBy` | WHERE | `findByUsername` |
| `And` | AND | `findByUsernameAndEmail` |
| `Or` | OR | `findByUsernameOrNickname` |
| `Between` | BETWEEN | `findByAgeBetween` |
| `GreaterThan` | > | `findByAgeGreaterThan` |
| `LessThan` | < | `findByAgeLessThan` |
| `Like` | LIKE | `findByUsernameLike` |
| `Containing` | LIKE '%?%' | `findByUsernameContaining` |
| `StartingWith` | LIKE '?%' | `findByUsernameStartingWith` |
| `EndingWith` | LIKE '%?' | `findByUsernameEndingWith` |
| `In` | IN | `findByUsernameIn` |
| `IsNull` | IS NULL | `findByEmailIsNull` |
| `IsNotNull` | IS NOT NULL | `findByEmailIsNotNull` |
| `OrderBy` | ORDER BY | `findAllByOrderByAgeDesc` |
| `NotNull` / `Empty` | 非空判断 | 集合/字符串 |

---

## 🔍 调试技巧

### 1. 查看 JPA 执行的 SQL

```properties
# application.properties
spring.jpa.show-sql=true
spring.jpa.format-sql=true
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

### 2. 常见错误排查

| 错误信息 | 原因 | 解决 |
|---------|------|------|
| `Access denied for user` | 密码错误 | 检查 `spring.datasource.password` |
| `Unknown database 'demo'` | 数据库不存在 | 先 `CREATE DATABASE demo` |
| `Table 'users' doesn't exist` | 表不存在 | 设置 `spring.jpa.hibernate.ddl-auto=update` |
| `Column 'xxx' not found` | 字段名不匹配 | 检查 `@Column(name="xxx")` |
| `Duplicate entry` | 唯一约束冲突 | username 重复了 |

---

## 🚀 下一步学习

1. **多表关联查询** - `@OneToMany`, `@ManyToOne`
2. **事务管理** - `@Transactional`
3. **分页查询** - `Pageable`, `Slice`
4. **自定义 SQL** - `@Query` 注解
5. **数据库迁移** - Flyway / Liquibase

---

**加油！数据库是后端开发的核心技能！** 💪
