package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * ==================== 第一个 Controller（控制器） ====================
 *
 * 什么是 Controller？
 * 控制器 = 路由处理器 = 处理 HTTP 请求的地方
 *
 * 类比前端 Express：
 * const express = require('express');
 * const router = express.Router();
 *
 * router.get('/hello', (req, res) => {
 *     res.json({ message: 'Hello' });
 * });
 *
 * 在 Spring Boot 中：
 * - @RestController = 定义这是一个控制器（返回 JSON）
 * - @GetMapping = 定义 GET 路由
 * - 方法返回值 = res.send()/res.json() 的内容
 */

/**
 * @RestController 注解的作用：
 * 1. 标记这是一个 REST 风格的控制器
 * 2. 所有方法的返回值会自动转成 JSON（不用手动 res.json()）
 *
 * 等价于：@Controller + @ResponseBody
 *
 * 类似前端：router 或 controller 文件
 */
@RestController
public class HelloController {

    /**
     * ==================== GET /hello ====================
     *
     * 访问方式：浏览器打开 http://localhost:8080/hello
     * 返回结果：Hello, Spring Boot!
     *
     * 类比前端 Express：
     * app.get('/hello', (req, res) => {
     *     res.send('Hello, Spring Boot!');
     * });
     */
    @GetMapping("/hello")  // 映射 HTTP GET 请求到这个方法
    public String hello() {
        // 直接返回字符串，Spring 会自动包装成 HTTP 响应
        // Content-Type: text/plain
        return "Hello, Spring Boot!";
    }

    /**
     * ==================== GET /api/user ====================
     *
     * 访问方式：http://localhost:8080/api/user
     * 返回结果：JSON 对象
     * {
     *     "id": 1,
     *     "name": "张三",
     *     "age": 25,
     *     "email": "zhangsan@example.com"
     * }
     *
     * 类比前端 Express：
     * app.get('/api/user', (req, res) => {
     *     res.json({
     *         id: 1,
     *         name: "张三",
     *         age: 25,
     *         email: "zhangsan@example.com"
     *     });
     * });
     */
    @GetMapping("/api/user")
    public Map<String, Object> getUser() {
        // Map 会自动转成 JSON 对象
        // key = JSON 的字段名
        // value = JSON 的字段值

        Map<String, Object> user = new HashMap<>();
        user.put("id", 1);                    // 用户 ID
        user.put("name", "张三");              // 姓名
        user.put("age", 25);                  // 年龄
        user.put("email", "zhangsan@example.com");

        return user;  // Spring 自动转成 JSON 返回
    }

    /**
     * ==================== GET /api/server-time ====================
     *
     * 访问方式：http://localhost:8080/api/server-time
     * 返回结果：
     * {
     *     "timestamp": 1710000000000,
     *     "message": "当前服务器时间"
     * }
     *
     * 用途：测试接口是否连通、获取服务器时间戳
     */
    @GetMapping("/api/server-time")
    public Map<String, Object> getServerTime() {
        Map<String, Object> result = new HashMap<>();

        // System.currentTimeMillis() 获取当前时间戳（毫秒）
        // 类似 JavaScript 的 Date.now()
        result.put("timestamp", System.currentTimeMillis());
        result.put("message", "当前服务器时间");

        return result;
    }

    /**
     * ==================== 补充说明 ====================
     *
     * 常用 HTTP 注解：
     * - @GetMapping("/xxx")   --> 处理 GET 请求
     * - @PostMapping("/xxx")  --> 处理 POST 请求
     * - @PutMapping("/xxx")   --> 处理 PUT 请求
     * - @DeleteMapping("/xxx")--> 处理 DELETE 请求
     * - @PatchMapping("/xxx") --> 处理 PATCH 请求
     *
     * 接收参数的注解：
     * - @PathVariable("id")   --> 路径参数 /user/{id}
     * - @RequestParam("name") --> 查询参数 ?name=xxx
     * - @RequestBody          --> 请求体 JSON（POST/PUT）
     *
     * 示例：
     * @GetMapping("/user/{id}")
     * public User getUser(@PathVariable Long id) { ... }
     *
     * @GetMapping("/search")
     * public List<User> search(@RequestParam String keyword) { ... }
     *
     * @PostMapping("/user")
     * public User createUser(@RequestBody User user) { ... }
     */
}
