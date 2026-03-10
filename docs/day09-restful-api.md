# Day 9 - RESTful API 设计与 Controller 进阶

**学习日期：** 2026-03-11
**预计时间：** 2-3 小时
**前置知识：** Day 8 - Spring Boot 入门

---

## 📚 今日学习目标

1. 理解 RESTful API 设计规范
2. 掌握各种 HTTP 方法的使用场景
3. 学会统一响应格式
4. 掌握全局异常处理

---

## 一、RESTful API 设计规范

### 1.1 什么是 RESTful？

REST = Representational State Transfer（表述性状态转移）

**核心思想：** 把所有东西都看作"资源"，用 HTTP 方法操作资源。

### 1.2 RESTful 风格对比

| 操作 | 传统风格 | RESTful 风格 |
|------|---------|------------|
| 获取用户列表 | `GET /getUsers` | `GET /api/users` |
| 获取单个用户 | `GET /getUser?id=1` | `GET /api/users/1` |
| 创建用户 | `POST /createUser` | `POST /api/users` |
| 更新用户 | `POST /updateUser?id=1` | `PUT /api/users/1` |
| 删除用户 | `POST /deleteUser?id=1` | `DELETE /api/users/1` |

### 1.3 RESTful 核心规则

```
┌─────────────┬──────────────────┬─────────────────────────────┐
│ HTTP 方法    │ 资源路径          │ 描述                        │
├─────────────┼──────────────────┼─────────────────────────────┤
│ GET         │ /api/users       │ 获取所有用户                │
│ GET         │ /api/users/1     │ 获取 ID 为 1 的用户           │
│ POST        │ /api/users       │ 创建新用户                  │
│ PUT         │ /api/users/1     │ 更新 ID 为 1 的用户（全量）  │
│ PATCH       │ /api/users/1     │ 更新 ID 为 1 的用户（部分）  │
│ DELETE      │ /api/users/1     │ 删除 ID 为 1 的用户          │
└─────────────┴──────────────────┴─────────────────────────────┘
```

### 1.4 资源命名规范

```java
// ✅ 正确：使用复数名词
@GetMapping("/api/users")
@GetMapping("/api/articles")
@GetMapping("/api/comments")

// ❌ 错误：使用动词
@GetMapping("/api/getUsers")
@GetMapping("/api/createUser")
@GetMapping("/api/deleteUser")

// ✅ 正确：嵌套资源
@GetMapping("/api/users/1/articles")  // 获取用户 1 的文章
@GetMapping("/api/articles/1/comments") // 获取文章 1 的评论
```

---

## 二、HTTP 方法详解

### 2.1 GET - 查询资源

```java
/**
 * 获取所有用户
 * GET /api/users
 */
@GetMapping("/api/users")
public List<Map<String, Object>> getAllUsers() {
    List<Map<String, Object>> users = new ArrayList<>();

    Map<String, Object> user1 = new HashMap<>();
    user1.put("id", 1);
    user1.put("name", "张三");
    user1.put("email", "zhangsan@example.com");

    Map<String, Object> user2 = new HashMap<>();
    user2.put("id", 2);
    user2.put("name", "李四");
    user2.put("email", "lisi@example.com");

    users.add(user1);
    users.add(user2);

    return users;
}

/**
 * 获取单个用户
 * GET /api/users/{id}
 */
@GetMapping("/api/users/{id}")
public Map<String, Object> getUserById(@PathVariable Long id) {
    Map<String, Object> user = new HashMap<>();
    user.put("id", id);
    user.put("name", "用户" + id);
    user.put("email", "user" + id + "@example.com");
    return user;
}
```

### 2.2 POST - 创建资源

```java
/**
 * 创建新用户
 * POST /api/users
 * Request Body: {"name": "王五", "email": "wangwu@example.com"}
 */
@PostMapping("/api/users")
public Map<String, Object> createUser(@RequestBody Map<String, Object> userData) {
    Map<String, Object> newUser = new HashMap<>();
    newUser.put("id", 3);  // 实际应该由数据库生成
    newUser.put("name", userData.get("name"));
    newUser.put("email", userData.get("email"));
    newUser.put("message", "用户创建成功");

    return newUser;
}
```

### 2.3 PUT - 更新资源（全量）

```java
/**
 * 更新用户（全量更新）
 * PUT /api/users/{id}
 * Request Body: {"name": "新名字", "email": "新邮箱"}
 */
@PutMapping("/api/users/{id}")
public Map<String, Object> updateUser(
    @PathVariable Long id,
    @RequestBody Map<String, Object> userData
) {
    Map<String, Object> updatedUser = new HashMap<>();
    updatedUser.put("id", id);
    updatedUser.put("name", userData.get("name"));
    updatedUser.put("email", userData.get("email"));
    updatedUser.put("message", "用户更新成功");

    return updatedUser;
}
```

### 2.4 PATCH - 更新资源（部分）

```java
/**
 * 部分更新用户
 * PATCH /api/users/{id}
 * Request Body: {"name": "新名字"}  只更新名字
 */
@PatchMapping("/api/users/{id}")
public Map<String, Object> patchUser(
    @PathVariable Long id,
    @RequestBody Map<String, Object> userData
) {
    // 实际场景中，只更新提供的字段
    Map<String, Object> updatedUser = new HashMap<>();
    updatedUser.put("id", id);
    updatedUser.put("updatedFields", userData.keySet());
    updatedUser.put("message", "用户部分更新成功");

    return updatedUser;
}
```

### 2.5 DELETE - 删除资源

```java
/**
 * 删除用户
 * DELETE /api/users/{id}
 */
@DeleteMapping("/api/users/{id}")
public Map<String, Object> deleteUser(@PathVariable Long id) {
    Map<String, Object> result = new HashMap<>();
    result.put("success", true);
    result.put("message", "用户 " + id + " 已删除");

    return result;
}
```

---

## 三、统一响应格式

### 3.1 为什么需要统一响应格式？

```java
// ❌ 不推荐：每个接口返回的格式不一样
// 接口 1
{"name": "张三", "age": 25}

// 接口 2
{"success": true, "data": {...}}

// 接口 3
{"code": 200, "msg": "success", "result": {...}}
```

```java
// ✅ 推荐：所有接口返回统一格式
{
    "code": 200,      // 状态码
    "message": "success",  // 消息
    "data": {        // 数据
        "name": "张三",
        "age": 25
    }
}
```

### 3.2 创建统一响应类

```java
package com.example.demo.common;

import lombok.Data;

/**
 * 统一响应结果封装
 *
 * 类似前端的 APIResponse 类型定义
 */
@Data
public class Result<T> {

    /** 状态码：200=成功，其他=失败 */
    private Integer code;

    /** 响应消息 */
    private String message;

    /** 响应数据（泛型，可以是任何类型） */
    private T data;

    /**
     * 成功响应（无数据）
     */
    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("success");
        return result;
    }

    /**
     * 成功响应（有数据）
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("success");
        result.setData(data);
        return result;
    }

    /**
     * 成功响应（自定义消息 + 数据）
     */
    public static <T> Result<T> success(String message, T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    /**
     * 失败响应
     */
    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<>();
        result.setCode(500);
        result.setMessage(message);
        return result;
    }

    /**
     * 失败响应（指定状态码）
     */
    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}
```

### 3.3 使用统一响应类

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping
    public Result<List<Map<String, Object>>> getAllUsers() {
        List<Map<String, Object>> users = new ArrayList<>();
        // ... 构建用户列表
        return Result.success(users);
    }

    @GetMapping("/{id}")
    public Result<Map<String, Object>> getUser(@PathVariable Long id) {
        Map<String, Object> user = new HashMap<>();
        user.put("id", id);
        user.put("name", "用户" + id);
        return Result.success(user);
    }

    @PostMapping
    public Result<Map<String, Object>> createUser(@RequestBody Map<String, Object> userData) {
        // ... 创建用户逻辑
        return Result.success("用户创建成功", newUser);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        // ... 删除用户逻辑
        return Result.success("删除成功", null);
    }
}
```

---

## 四、全局异常处理

### 4.1 创建全局异常处理器

```java
package com.example.demo.common;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * 类似前端的全局 error handler
 *
 * @RestControllerAdvice = 全局控制器增强
 * @ExceptionHandler = 捕获指定类型的异常
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 捕获所有 Exception
     */
    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception e) {
        return Result.error(500, "服务器内部错误：" + e.getMessage());
    }

    /**
     * 捕获参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return Result.error(400, "参数错误：" + e.getMessage());
    }

    /**
     * 捕获资源不存在异常
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public Result<String> handleResourceNotFoundException(ResourceNotFoundException e) {
        return Result.error(404, e.getMessage());
    }
}
```

### 4.2 自定义异常类

```java
package com.example.demo.exception;

import lombok.Getter;

/**
 * 自定义异常：资源不存在
 */
@Getter
public class ResourceNotFoundException extends RuntimeException {

    private final Long resourceId;

    public ResourceNotFoundException(String resourceName, Long resourceId) {
        super(resourceName + " 不存在，ID: " + resourceId);
        this.resourceId = resourceId;
    }
}
```

### 4.3 在 Controller 中使用

```java
@GetMapping("/api/users/{id}")
public Result<Map<String, Object>> getUser(@PathVariable Long id) {
    // 模拟查找用户
    if (id > 100) {
        // 抛出异常，由全局异常处理器捕获
        throw new ResourceNotFoundException("用户", id);
    }

    Map<String, Object> user = new HashMap<>();
    user.put("id", id);
    user.put("name", "用户" + id);
    return Result.success(user);
}
```

---

## 五、完整的 Controller 示例

```java
package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.exception.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 用户控制器
 *
 * 完整演示 RESTful API 的各种操作
 */
@RestController
@RequestMapping("/api/users")  // 所有路由的前缀
public class UserController {

    // 模拟数据库
    private static final Map<Long, Map<String, Object>> DB = new HashMap<>();

    static {
        // 初始化一些测试数据
        Map<String, Object> user1 = new HashMap<>();
        user1.put("id", 1L);
        user1.put("name", "张三");
        user1.put("email", "zhangsan@example.com");
        user1.put("age", 25);
        DB.put(1L, user1);

        Map<String, Object> user2 = new HashMap<>();
        user2.put("id", 2L);
        user2.put("name", "李四");
        user2.put("email", "lisi@example.com");
        user2.put("age", 30);
        DB.put(2L, user2);
    }

    /**
     * 获取所有用户
     * GET /api/users
     */
    @GetMapping
    public Result<List<Map<String, Object>>> getAllUsers() {
        List<Map<String, Object>> users = new ArrayList<>(DB.values());
        return Result.success(users);
    }

    /**
     * 获取单个用户
     * GET /api/users/{id}
     */
    @GetMapping("/{id}")
    public Result<Map<String, Object>> getUser(@PathVariable Long id) {
        Map<String, Object> user = DB.get(id);
        if (user == null) {
            throw new ResourceNotFoundException("用户", id);
        }
        return Result.success(user);
    }

    /**
     * 创建用户
     * POST /api/users
     */
    @PostMapping
    public Result<Map<String, Object>> createUser(@RequestBody Map<String, Object> userData) {
        // 验证必填字段
        if (!userData.containsKey("name")) {
            return Result.error(400, "姓名不能为空");
        }

        // 生成新 ID
        Long newId = DB.size() + 1L;

        // 创建新用户
        Map<String, Object> newUser = new HashMap<>();
        newUser.put("id", newId);
        newUser.put("name", userData.get("name"));
        newUser.put("email", userData.get("email"));
        newUser.put("age", userData.get("age"));

        // 保存到"数据库"
        DB.put(newId, newUser);

        return Result.success("用户创建成功", newUser);
    }

    /**
     * 更新用户（全量）
     * PUT /api/users/{id}
     */
    @PutMapping("/{id}")
    public Result<Map<String, Object>> updateUser(
        @PathVariable Long id,
        @RequestBody Map<String, Object> userData
    ) {
        if (!DB.containsKey(id)) {
            throw new ResourceNotFoundException("用户", id);
        }

        // 更新用户
        Map<String, Object> user = DB.get(id);
        user.put("name", userData.get("name"));
        user.put("email", userData.get("email"));
        user.put("age", userData.get("age"));

        return Result.success("用户更新成功", user);
    }

    /**
     * 删除用户
     * DELETE /api/users/{id}
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        if (!DB.containsKey(id)) {
            throw new ResourceNotFoundException("用户", id);
        }

        DB.remove(id);
        return Result.success("删除成功", null);
    }
}
```

---

## 六、实战练习

### 练习 1：创建文章管理 API

```
需求：管理博客文章
资源：/api/articles

接口列表：
- GET /api/articles         - 获取所有文章
- GET /api/articles/{id}    - 获取单篇文章
- POST /api/articles        - 创建文章
- PUT /api/articles/{id}    - 更新文章
- DELETE /api/articles/{id} - 删除文章
```

### 练习 2：添加分页功能

```java
/**
 * 分页获取用户列表
 * GET /api/users?page=1&size=10
 */
@GetMapping
public Result<Map<String, Object>> getUsers(
    @RequestParam(defaultValue = "1") int page,
    @RequestParam(defaultValue = "10") int size
) {
    // TODO: 实现分页逻辑
}
```

### 练习 3：添加搜索功能

```java
/**
 * 搜索用户
 * GET /api/users/search?keyword=张
 */
@GetMapping("/search")
public Result<List<Map<String, Object>>> searchUsers(
    @RequestParam String keyword
) {
    // TODO: 实现搜索逻辑
}
```

---

## ✅ 今日检查清单

- [ ] 理解 RESTful API 设计规范
- [ ] 掌握 GET/POST/PUT/DELETE 的用法
- [ ] 能创建统一响应格式
- [ ] 理解全局异常处理的作用
- [ ] 完成文章管理 API 练习
- [ ] 完成分页功能练习
- [ ] 完成搜索功能练习

---

## 📝 笔记

> 在此处记录学习过程中的疑问、心得

---

*创建日期：2026-03-10*
