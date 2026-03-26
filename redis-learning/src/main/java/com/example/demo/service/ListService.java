package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * List 操作服务
 *
 * Redis List 是一个双向链表，支持左右两端的推入和弹出
 * 前端类比：JavaScript 的 Array
 *   lpush = unshift()    rpush = push()
 *   lpop  = shift()      rpop  = pop()
 *
 * 适用场景：消息队列、最新消息列表、时间线
 */
@Service
public class ListService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 从左边推入
     * 对应 Redis 命令：LPUSH key value
     */
    public Long leftPush(String key, Object value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 从右边推入
     * 对应 Redis 命令：RPUSH key value
     */
    public Long rightPush(String key, Object value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 从左边弹出
     * 对应 Redis 命令：LPOP key
     */
    public Object leftPop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 从右边弹出
     * 对应 Redis 命令：RPOP key
     */
    public Object rightPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    /**
     * 获取指定范围的元素（不删除）
     * 对应 Redis 命令：LRANGE key start stop
     * start=0, stop=-1 表示获取全部
     */
    public List<Object> range(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 获取列表长度
     * 对应 Redis 命令：LLEN key
     */
    public Long size(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 裁剪列表，只保留指定范围
     * 对应 Redis 命令：LTRIM key start stop
     * 常用于：只保留最新 N 条消息
     */
    public void trim(String key, long start, long end) {
        redisTemplate.opsForList().trim(key, start, end);
    }
}
