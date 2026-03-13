package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * ==================== 参数接收练习 Controller ====================
 *
 * 用途：学习如何接收前端传递的参数
 *
 * 三种常用方式：
 * 1. @RequestParam - 查询参数 (?name=xxx&age=25)
 * 2. @PathVariable - 路径参数 (/users/123)
 * 3. @RequestBody  - 请求体 JSON (POST/PUT 时)
 *
 * 访问：http://localhost:8080/api/params
 */
@RestController
@RequestMapping("/api/params")
public class ParamsController {

    /**
     * ==================== 1. @RequestParam - 查询参数 ====================
     *
     * 访问方式：
     * http://localhost:8080/api/params/query?name=张三&age=25
     *
     * 前端对比（axios）：
     * axios.get('/api/params/query', {
     *     params: { name: '张三', age: 25 }
     * });
     *
     * 前端对比（fetch）：
     * fetch('/api/params/query?name=张三&age=25')
     *
     * 测试结果：
     * {
     *     "name": "张三",
     *     "age": 25,
     *     "message": "接收成功"
     * }
     */
    @GetMapping("/query")
    public Map<String, Object> queryParam(
        @RequestParam String name,           // 必填参数
        @RequestParam(required = false) String email,  // 可选参数
        @RequestParam(defaultValue = "18") int age     // 有默认值
    ) {
        Map<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("email", email);  // 没传就是 null
        result.put("age", age);
        result.put("message", "接收成功");
        return result;
    }

    /**
     * ==================== 2. @PathVariable - 路径参数 ====================
     *
     * 访问方式：
     * http://localhost:8080/api/params/user/123
     * http://localhost:8080/api/params/order/456
     *
     * 前端对比（axios）：
     * axios.get(`/api/params/user/${userId}`)
     *
     * 前端对比（Next.js）：
     * params.id  // 在 [id].tsx 中
     *
     * 测试结果：
     * {
     *     "id": 123,
     *     "type": "user",
     *     "message": "获取 ID=123 的信息"
     * }
     */
    @GetMapping("/{type}/{id}")
    public Map<String, Object> pathParam(
        @PathVariable String type,    // 匹配 /user 或 /order
        @PathVariable Integer id      // 匹配数字
    ) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("type", type);
        result.put("message", "获取 ID=" + id + "的" + type + "信息");
        return result;
    }

    /**
     * ==================== 3. 混合使用 ====================
     *
     * 访问方式：
     * http://localhost:8080/api/params/user/123/detail?full=true
     *
     * 测试结果：
     * {
     *     "id": 123,
     *     "full": true,
     *     "message": "获取详细信息"
     * }
     */
    @GetMapping("/{type}/{id}/detail")
    public Map<String, Object> mixParam(
        @PathVariable String type,
        @PathVariable Integer id,
        @RequestParam(defaultValue = "false") boolean full
    ) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("type", type);
        result.put("full", full);
        result.put("message", full ? "获取详细信息" : "获取基本信息");
        return result;
    }

    /**
     * ==================== 4. @RequestBody - POST JSON ====================
     *
     * 访问方式：用 Postman 或前端发送 POST 请求
     *
     * 前端对比（axios）：
     * axios.post('/api/params/create', {
     *     name: '张三',
     *     age: 25,
     *     email: 'zhangsan@example.com'
     * });
     *
     * 测试命令（curl）：
     * curl -X POST http://localhost:8080/api/params/create \
     *   -H "Content-Type: application/json" \
     *   -d '{"name":"张三","age":25,"email":"zhangsan@example.com"}'
     */
    @PostMapping("/create")
    public Map<String, Object> create(
        @RequestBody Map<String, Object> body
    ) {
        // body 就是前端传的 JSON 对象
        Map<String, Object> result = new HashMap<>();
        result.put("received", body);
        result.put("message", "创建成功");
        result.put("timestamp", System.currentTimeMillis());
        return result;
    }

    /**
     * ==================== 5. 接收表单数据 ====================
     *
     * 前端对比（axios）：
     * axios.post('/api/params/login', {
     *     username: 'admin',
     *     password: '123456'
     * });
     *
     * 测试命令（curl）：
     * curl -X POST http://localhost:8080/api/params/login \
     *   -H "Content-Type: application/json" \
     *   -d '{"username":"admin","password":"123456"}'
     */
    @PostMapping("/login")
    public Map<String, Object> login(
        @RequestBody Map<String, String> loginData
    ) {
        String username = loginData.get("username");
        String password = loginData.get("password");

        Map<String, Object> result = new HashMap<>();

        // 简单验证（实际项目要查数据库）
        if ("admin".equals(username) && "123456".equals(password)) {
            result.put("success", true);
            result.put("token", "mock-jwt-token-12345");
            result.put("message", "登录成功");
        } else {
            result.put("success", false);
            result.put("message", "用户名或密码错误");
        }

        return result;
    }
}