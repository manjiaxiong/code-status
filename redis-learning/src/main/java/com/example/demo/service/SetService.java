package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Set 操作服务
 *
 * Redis Set 是无序、不重复的集合
 * 前端类比：JavaScript 的 Set
 *   add    = set.add()
 *   remove = set.delete()
 *   has    = set.has()
 *
 * 核心能力：集合运算（交集、并集、差集）
 * 适用场景：点赞、标签、共同关注、抽奖
 */
@Service
public class SetService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 添加成员
     * 对应 Redis 命令：SADD key member
     */
    public Long add(String key, Object... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    /**
     * 移除成员
     * 对应 Redis 命令：SREM key member
     */
    public Long remove(String key, Object... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }

    /**
     * 获取所有成员
     * 对应 Redis 命令：SMEMBERS key
     */
    public Set<Object> members(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 判断成员是否存在
     * 对应 Redis 命令：SISMEMBER key member
     */
    public Boolean isMember(String key, Object value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    /**
     * 获取集合大小
     * 对应 Redis 命令：SCARD key
     */
    public Long size(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    /**
     * 交集（两个集合都有的）
     * 对应 Redis 命令：SINTER key1 key2
     */
    public Set<Object> intersect(String key1, String key2) {
        return redisTemplate.opsForSet().intersect(key1, key2);
    }

    /**
     * 并集（两个集合合在一起）
     * 对应 Redis 命令：SUNION key1 key2
     */
    public Set<Object> union(String key1, String key2) {
        return redisTemplate.opsForSet().union(key1, key2);
    }

    /**
     * 差集（key1 有但 key2 没有的）
     * 对应 Redis 命令：SDIFF key1 key2
     */
    public Set<Object> difference(String key1, String key2) {
        return redisTemplate.opsForSet().difference(key1, key2);
    }
}
