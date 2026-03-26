package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * ZSet（有序集合）操作服务
 *
 * Redis ZSet = Set + 每个成员有一个 score（分数）
 * 按 score 排序，天然就是排行榜
 *
 * 前端类比：
 *   类似于 Array.sort((a, b) => a.score - b.score)
 *   但 Redis 帮你自动维护排序，不需要每次手动排
 *
 * 适用场景：排行榜、热搜、延迟队列
 */
@Service
public class ZSetService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 添加成员（带分数）
     * 对应 Redis 命令：ZADD key score member
     */
    public Boolean add(String key, Object value, double score) {
        return redisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * 增加成员的分数
     * 对应 Redis 命令：ZINCRBY key increment member
     */
    public Double incrementScore(String key, Object value, double delta) {
        return redisTemplate.opsForZSet().incrementScore(key, value, delta);
    }

    /**
     * 获取成员的分数
     * 对应 Redis 命令：ZSCORE key member
     */
    public Double score(String key, Object value) {
        return redisTemplate.opsForZSet().score(key, value);
    }

    /**
     * 获取成员的排名（从低到高，0 开始）
     * 对应 Redis 命令：ZRANK key member
     */
    public Long rank(String key, Object value) {
        return redisTemplate.opsForZSet().rank(key, value);
    }

    /**
     * 获取成员的排名（从高到低，0 开始）
     * 对应 Redis 命令：ZREVRANK key member
     */
    public Long reverseRank(String key, Object value) {
        return redisTemplate.opsForZSet().reverseRank(key, value);
    }

    /**
     * 获取 TopN（分数从高到低）
     * 对应 Redis 命令：ZREVRANGE key start stop WITHSCORES
     */
    public Set<ZSetOperations.TypedTuple<Object>> topN(String key, long start, long end) {
        return redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
    }

    /**
     * 移除成员
     * 对应 Redis 命令：ZREM key member
     */
    public Long remove(String key, Object... values) {
        return redisTemplate.opsForZSet().remove(key, values);
    }

    /**
     * 获取集合大小
     * 对应 Redis 命令：ZCARD key
     */
    public Long size(String key) {
        return redisTemplate.opsForZSet().zCard(key);
    }
}
