# 📚 Spring Boot 学习 - 版本对比

> 最后更新：2026-03-12

---

## 📂 项目结构

```
status/
├── v1-crud/              # 第 1 章：基础 CRUD（Controller 直调 Repository）
├── v2-service/           # 第 2 章：Service 层（三层架构）
└── springboot-demo/      # 当前学习进度（最新）
```

每个文件夹都是**独立可运行的 Spring Boot 项目**！

---

## 🚀 如何运行

### v1-crud（基础 CRUD）

```bash
cd D:/code/code/status/v1-crud
mvn spring-boot:run
```

访问：http://localhost:18080/api/users

---

### v2-service（Service 层）

```bash
cd D:/code/code/status/v2-service
mvn spring-boot:run
```

访问：http://localhost:18080/api/users

---

### springboot-demo（最新版本）

```bash
cd D:/code/code/status/springboot-demo
mvn spring-boot:run
```

访问：http://localhost:18080/api/users

---

## 📊 版本对比

| 特性 | v1-crud | v2-service |
|------|---------|------------|
| Controller → Repository | ✅ 直接调用 | ❌ 通过 Service |
| Service 层 | ❌ 无 | ✅ 有 |
| 业务逻辑位置 | Controller | Service |
| 事务控制 | ❌ 无 | ✅ @Transactional |
| 代码复用 | ❌ 难 | ✅ 易 |
| 适合场景 | 简单 CRUD | 复杂业务 |

---

## 💡 学习建议

1. **先运行 v1-crud**，理解基础 CRUD
2. **再运行 v2-service**，理解 Service 层的作用
3. **对比两个版本的 UserController**，看看有什么不同

---

## 🔍 代码差异

### v1-crud/UserController.java

```java
@Autowired
private UserRepository userRepository;  // 直接注入 Repository

@PostMapping
public Result<User> create(@RequestBody User user) {
    // 业务逻辑写在 Controller 里
    if (user.getUsername() == null || ...) { ... }
    User existingUser = userRepository.findByUsername(...);
    if (existingUser != null) { ... }
    return userRepository.save(user);
}
```

---

### v2-service/UserController.java

```java
@Autowired
private UserService userService;  // 注入 Service

@PostMapping
public Result<User> create(@RequestBody User user) {
    // Controller 只负责调用 Service
    return Result.success("创建成功", userService.createUser(user));
}
```

---

### v2-service/UserServiceImpl.java

```java
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional  // 事务控制
    public User createUser(User user) {
        // 业务逻辑写在 Service 里
        if (user.getUsername() == null || ...) { ... }
        User existingUser = userRepository.findByUsername(...);
        if (existingUser != null) { ... }
        return userRepository.save(user);
    }
}
```

---

## 📝 测试命令

```powershell
# 查询所有
curl "http://localhost:18080/api/users"

# 新增用户
curl -X POST "http://localhost:18080/api/users" `
  -H "Content-Type: application/json" `
  -d '{"username":"zhangsan","age":25}'

# 根据 ID 查询
curl "http://localhost:18080/api/users/1"

# 更新用户
curl -X PUT "http://localhost:18080/api/users/1" `
  -H "Content-Type: application/json" `
  -d '{"id":1,"username":"lisi","age":28}'

# 删除用户
curl -X DELETE "http://localhost:18080/api/users/1"

# 模糊查询
curl "http://localhost:18080/api/users/search/keyword?name=zhang"

# 年龄范围查询
curl "http://localhost:18080/api/users/age?min=20&max=30"
```

---

*每个版本都是独立项目，可以分别运行对比学习！*
