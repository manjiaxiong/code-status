package com.example.demo.controller;

import com.example.demo.service.SetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * Set 操作 Controller
 *
 * ========== 实战场景 1：点赞 ==========
 * 设计思路：
 *   key = "like:文章ID"
 *   member = "用户ID"
 * 用 Set 天然去重，一个用户只能点一次赞
 *
 * ========== 实战场景 2：共同关注 ==========
 * 设计思路：
 *   key = "follow:用户ID"
 *   member = "被关注的用户ID"
 * 用 SINTER 求交集 = 共同关注的人
 * 用 SDIFF 求差集 = 你关注了但他没关注的人（可推荐给他）
 *
 * 前端类比：
 *   点赞 = new Set() 存点赞用户，set.has(userId) 判断是否已点赞
 *   共同关注 = 两个 Set 取交集
 */
@RestController
@RequestMapping("/redis/set")
public class SetController {

    @Autowired
    private SetService setService;

    // ========== 基础操作 ==========

    /** SADD：添加成员 */
    @PostMapping("/add")
    public String add(@RequestParam String key, @RequestParam String value) {
        setService.add(key, value);
        return "添加成功";
    }

    /** SREM：移除成员 */
    @PostMapping("/remove")
    public String remove(@RequestParam String key, @RequestParam String value) {
        setService.remove(key, value);
        return "移除成功";
    }

    /** SMEMBERS：获取所有成员 */
    @GetMapping("/members")
    public Set<Object> members(@RequestParam String key) {
        return setService.members(key);
    }

    /** SISMEMBER：判断是否存在 */
    @GetMapping("/isMember")
    public Boolean isMember(@RequestParam String key, @RequestParam String value) {
        return setService.isMember(key, value);
    }

    // ========== 点赞场景 ==========

    /**
     * 点赞
     * 示例：POST /redis/set/like?articleId=100&userId=user1
     */
    @PostMapping("/like")
    public String like(@RequestParam String articleId, @RequestParam String userId) {
        String key = "like:" + articleId;
        // 如果已经点赞，则取消（Toggle 效果）
        if (Boolean.TRUE.equals(setService.isMember(key, userId))) {
            setService.remove(key, userId);
            return "已取消点赞，当前点赞数: " + setService.size(key);
        }
        setService.add(key, userId);
        return "点赞成功，当前点赞数: " + setService.size(key);
    }

    /**
     * 查看文章点赞列表和数量
     * 示例：GET /redis/set/like/list?articleId=100
     */
    @GetMapping("/like/list")
    public Object likeList(@RequestParam String articleId) {
        String key = "like:" + articleId;
        return "点赞用户: " + setService.members(key) + "，总数: " + setService.size(key);
    }

    // ========== 共同关注场景 ==========

    /**
     * 关注某人
     * 示例：POST /redis/set/follow?userId=user1&targetId=user2
     */
    @PostMapping("/follow")
    public String follow(@RequestParam String userId, @RequestParam String targetId) {
        setService.add("follow:" + userId, targetId);
        return userId + " 关注了 " + targetId;
    }

    /**
     * 共同关注（交集）
     * 示例：GET /redis/set/follow/common?userId1=user1&userId2=user2
     */
    @GetMapping("/follow/common")
    public Set<Object> commonFollow(@RequestParam String userId1, @RequestParam String userId2) {
        return setService.intersect("follow:" + userId1, "follow:" + userId2);
    }

    /**
     * 我关注了但他没关注的（差集，可作为推荐）
     * 示例：GET /redis/set/follow/recommend?userId=user1&targetId=user2
     */
    @GetMapping("/follow/recommend")
    public Set<Object> recommendFollow(@RequestParam String userId, @RequestParam String targetId) {
        return setService.difference("follow:" + userId, "follow:" + targetId);
    }
}
