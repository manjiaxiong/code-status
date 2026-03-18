package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.entity.User;
import com.example.demo.jwt.JwtAuthService;
import com.example.demo.jwt.LoginRequest;
import com.example.demo.jwt.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * ==================== JWT 认证控制器 ====================
 *
 * 作用：用户登录、注册、令牌管理
 * 类似前端：POST /api/auth/login, POST /api/auth/register
 *
 * 接口列表：
 * 1. POST /api/auth/login      - 用户登录
 * 2. POST /api/auth/register   - 用户注册
 * 3. GET  /api/auth/me         - 获取当前用户信息
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin  // 允许跨域（前端开发时需要）
public class AuthController {

    @Autowired
    private JwtAuthService jwtAuthService;

    /**
     * ==================== 1. 用户登录 ====================
     *
     * 访问：POST http://localhost:18080/api/auth/login
     * 请求体：{"username":"zhangsan","password":"123456"}
     *
     * 测试命令：
     * curl -X POST http://localhost:18080/api/auth/login \
     *   -H "Content-Type: application/json" \
     *   -d '{"username":"zhangsan","password":"123456"}'
     *
     * 返回：
     * {
     *     "code": 200,
     *     "message": "登录成功",
     *     "data": {
     *         "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
     *         "tokenType": "Bearer",
     *         "username": "zhangsan",
     *         "userId": 1,
     *         "expiresIn": 604800
     *     }
     * }
     *
     * 前端使用方法：
     * const res = await fetch('/api/auth/login', {
     *     method: 'POST',
     *     headers: { 'Content-Type': 'application/json' },
     *     body: JSON.stringify({ username, password })
     * });
     * const data = await res.json();
     * localStorage.setItem('token', data.data.token);
     */
    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = jwtAuthService.login(
            request.getUsername(),
            request.getPassword()
        );
        return Result.success("登录成功", response);
    }

    /**
     * ==================== 2. 用户注册 ====================
     *
     * 访问：POST http://localhost:18080/api/auth/register
     * 请求体：{"username":"lisi","password":"123456","nickname":"小李"}
     *
     * 测试命令：
     * curl -X POST http://localhost:18080/api/auth/register \
     *   -H "Content-Type: application/json" \
     *   -d '{"username":"lisi","password":"123456","nickname":"小李"}'
     *
     * 返回：
     * {
     *     "code": 200,
     *     "message": "注册成功",
     *     "data": {
     *         "id": 2,
     *         "username": "lisi",
     *         "nickname": "小李",
     *         ...
     *     }
     * }
     */
    @PostMapping("/register")
    public Result<User> register(@RequestBody RegisterRequest request) {
        User user = jwtAuthService.register(
            request.getUsername(),
            request.getPassword(),
            request.getNickname()
        );
        return Result.success("注册成功", user);
    }

    /**
     * ==================== 3. 获取当前用户信息 ====================
     *
     * 访问：GET http://localhost:18080/api/auth/me
     * 请求头：Authorization: Bearer <token>
     *
     * 测试命令：
     * curl http://localhost:18080/api/auth/me \
     *   -H "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
     *
     * 返回：
     * {
     *     "code": 200,
     *     "message": "success",
     *     "data": {
     *         "id": 1,
     *         "username": "zhangsan",
     *         "nickname": "张三",
     *         "email": "zhangsan@example.com"
     *     }
     * }
     */
    @GetMapping("/me")
    public Result<User> getCurrentUser(@RequestHeader("Authorization") String authorization) {
        // 提取 Token（去掉 "Bearer " 前缀）
        String token = authorization.replace("Bearer ", "");
        Long userId = jwtAuthService.getUserIdFromToken(token);
        User user = jwtAuthService.getCurrentUser(userId);
        return Result.success(user);
    }
}
