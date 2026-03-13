package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * ==================== 用户管理 Controller ====================
 *
 * 作用：学习 JPA 数据库操作
 * 访问：http://localhost:18080/api/users
 *
 * 实现功能：
 * - 查询所有用户
 * - 根据 ID 查询
 * - 新增用户
 * - 更新用户
 * - 删除用户
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * ==================== 1. 查询所有用户 ====================
     *
     * 访问：GET http://localhost:18080/api/users
     *
     * 测试命令：
     * curl http://localhost:18080/api/users
     *
     * 返回：
     * {
     *     "code": 200,
     *     "message": "success",
     *     "data": [{"id":1,"username":"zhangsan"},...]
     * }
     */
    @GetMapping
    public Result<List<User>> list() {
        return Result.success(userService.findAll());
    }

    /**
     * ==================== 2. 根据 ID 查询 ====================
     *
     * 访问：GET http://localhost:18080/api/users/1
     *
     * 测试命令：
     * curl http://localhost:18080/api/users/1
     */
    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable Long id) {
        Optional<User> userOpt = userService.findById(id);
        return userOpt.map(Result::success)
                      .orElse(Result.error(404, "用户不存在"));
    }

    /**
     * ==================== 3. 新增用户 ====================
     *
     * 访问：POST http://localhost:18080/api/users
     * 请求体：{"username":"zhangsan","email":"zhangsan@example.com","age":25}
     *
     * 测试命令：
     * curl -X POST http://localhost:18080/api/users \
     *   -H "Content-Type: application/json" \
     *   -d '{"username":"zhangsan","email":"zhangsan@example.com","age":25}'
     */
    @PostMapping
    public Result<User> create(@RequestBody User user) {
        User savedUser = userService.createUser(user);
        return Result.success("创建成功", savedUser);
    }

    /**
     * ==================== 4. 更新用户 ====================
     *
     * 访问：PUT http://localhost:18080/api/users/1
     * 请求体：{"id":1,"username":"lisi","email":"lisi@example.com","age":28}
     *
     * 测试命令：
     * curl -X PUT http://localhost:18080/api/users/1 \
     *   -H "Content-Type: application/json" \
     *   -d '{"id":1,"username":"lisi","email":"lisi@example.com","age":28}'
     */
    @PutMapping("/{id}")
    public Result<User> update(
        @PathVariable Long id,
        @RequestBody User user
    ) {
        User updatedUser = userService.updateUser(id, user);
        return Result.success("更新成功", updatedUser);
    }

    /**
     * ==================== 5. 删除用户 ====================
     *
     * 访问：DELETE http://localhost:18080/api/users/1
     *
     * 测试命令：
     * curl -X DELETE http://localhost:18080/api/users/1
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return Result.success("删除成功", null);
        } else {
            return Result.error(404, "用户不存在");
        }
    }

    /**
     * ==================== 6. 测试自定义查询方法 ====================
     *
     * 访问：GET http://localhost:18080/api/users/search/keyword?name=zhang
     *
     * 测试命令：
     * curl "http://localhost:18080/api/users/search/keyword?name=zhang"
     */
    @GetMapping("/search/keyword")
    public Result<List<User>> searchByUsername(@RequestParam String name) {
        return Result.success(userService.findByUsernameContaining(name));
    }

    /**
     * ==================== 7. 根据年龄范围查询 ====================
     *
     * 访问：GET http://localhost:18080/api/users/age?min=20&max=30
     *
     * 测试命令：
     * curl "http://localhost:18080/api/users/age?min=20&max=30"
     */
    @GetMapping("/age")
    public Result<List<User>> getByAgeRange(
        @RequestParam Integer min,
        @RequestParam Integer max
    ) {
        return Result.success(userService.findByAgeBetween(min, max));
    }
}