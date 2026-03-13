package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * ==================== 用户管理 Controller ====================
 *
 * v1-crud 版本：Controller 直接调用 Repository
 * 适合：简单 CRUD，快速开发
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    /**
     * 查询所有用户
     */
    @GetMapping
    public Result<List<User>> list() {
        return Result.success(userRepository.findAll());
    }

    /**
     * 根据 ID 查询用户
     */
    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        return userOpt.map(Result::success)
                      .orElse(Result.error(404, "用户不存在"));
    }

    /**
     * 新增用户
     */
    @PostMapping
    public Result<User> create(@RequestBody User user) {
        // 简单验证：用户名不能为空
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为空");
        }

        // 验证用户名是否已存在
        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null) {
            throw new IllegalArgumentException("用户名已存在");
        }

        User savedUser = userRepository.save(user);
        return Result.success("创建成功", savedUser);
    }

    /**
     * 更新用户
     */
    @PutMapping("/{id}")
    public Result<User> update(
        @PathVariable Long id,
        @RequestBody User user
    ) {
        Optional<User> userOpt = userRepository.findById(id);

        if (userOpt.isPresent()) {
            User existingUser = userOpt.get();
            existingUser.setUsername(user.getUsername());
            existingUser.setEmail(user.getEmail());
            existingUser.setAge(user.getAge());

            userRepository.save(existingUser);
            return Result.success("更新成功", existingUser);
        } else {
            return Result.error(404, "用户不存在");
        }
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return Result.success("删除成功", null);
        } else {
            return Result.error(404, "用户不存在");
        }
    }

    /**
     * 模糊查询用户名
     */
    @GetMapping("/search/keyword")
    public Result<List<User>> searchByUsername(@RequestParam String name) {
        return Result.success(userRepository.findByUsernameContaining(name));
    }

    /**
     * 按年龄范围查询
     */
    @GetMapping("/age")
    public Result<List<User>> getByAgeRange(
        @RequestParam Integer min,
        @RequestParam Integer max
    ) {
        return Result.success(userRepository.findByAgeBetween(min, max));
    }
}
