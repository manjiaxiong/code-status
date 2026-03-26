package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Hash 操作服务
 *
 * Redis Hash 类似于 Java 的 Map<String, Object>
 * 前端类比：一个 key 对应一个 JSON 对象
 *   比如 user:1 -> { name: "张三", age: 25 }
 *
 * 适用场景：存储对象、购物车
 */
@Service
public class HashService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 设置 Hash 中的一个字段
     * 对应 Redis 命令：HSET key field value
     */
    public void put(String key, String field, Object value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    /**
     * 获取 Hash 中的一个字段
     * 对应 Redis 命令：HGET key field
     */
    public Object get(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    /**
     * 获取 Hash 中的所有字段和值
     * 对应 Redis 命令：HGETALL key
     */
    public Map<Object, Object> getAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 删除 Hash 中的一个字段
     * 对应 Redis 命令：HDEL key field
     */
    public void delete(String key, String field) {
        redisTemplate.opsForHash().delete(key, field);
    }

    /**
     * Hash 字段值递增（购物车加减数量专用）
     * 对应 Redis 命令：HINCRBY key field increment
     */
    public Long increment(String key, String field, long delta) {
        return redisTemplate.opsForHash().increment(key, field, delta);
    }

    /**
     * 判断 Hash 中是否存在某个字段
     * 对应 Redis 命令：HEXISTS key field
     */
    public Boolean hasField(String key, String field) {
        return redisTemplate.opsForHash().hasKey(key, field);
    }
}
