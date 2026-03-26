package com.example.demo.controller;

import com.example.demo.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Redis 基础操作 Controller
 */
@RestController
@RequestMapping("/redis")
public class RedisController {

    @Autowired
    private RedisService redisService;

    @PostMapping("/set")
    public String set(@RequestParam String key, @RequestParam String value) {
        redisService.set(key, value);
        return "设置成功";
    }

    @GetMapping("/get")
    public Object get(@RequestParam String key) {
        return redisService.get(key);
    }

    @DeleteMapping("/delete")
    public String delete(@RequestParam String key) {
        redisService.delete(key);
        return "删除成功";
    }

    @PostMapping("/setex")
    public String setex(@RequestParam String key, @RequestParam String value, @RequestParam long seconds) {
        redisService.setWithExpire(key, value, seconds);
        return "设置成功，" + seconds + "秒后过期";
    }
}
