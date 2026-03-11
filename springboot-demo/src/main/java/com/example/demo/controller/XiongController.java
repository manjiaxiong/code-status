package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * ==================== 测试 Controller ====================
 * 用途：测试接口连通性
 * 访问：GET /xiong
 */
@RestController
public class XiongController {

    /**
     * GET /xiong
     * 返回测试数据
     */
    @GetMapping("/xiong")
    public Map<String, Object> getTestInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("name", "熊家满111");
        return map;
    }
}
