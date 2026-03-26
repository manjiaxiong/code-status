package com.example.demo.controller;

import com.example.demo.service.ZSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * ZSet 操作 Controller
 *
 * ========== 实战场景：排行榜 ==========
 * 设计思路：
 *   key = "rank:游戏名"    （每个排行榜一个 key）
 *   member = "玩家名"       （参与排名的人）
 *   score = 分数            （排名依据）
 *
 * 为什么用 ZSet？
 *   1. 自动按 score 排序，O(logN) 插入，不用手动 sort
 *   2. 成员去重，同一个玩家只有一条记录
 *   3. ZINCRBY 原子加分，并发安全
 *
 * 前端类比：
 *   如果用数组做排行榜：每次加分都要 find → 改分 → sort，O(NlogN)
 *   Redis ZSet：直接 ZINCRBY，自动排好序，O(logN)
 */
@RestController
@RequestMapping("/redis/zset")
public class ZSetController {

    @Autowired
    private ZSetService zSetService;

    // ========== 基础操作 ==========

    /** ZADD：添加成员 */
    @PostMapping("/add")
    public String add(@RequestParam String key, @RequestParam String value, @RequestParam double score) {
        zSetService.add(key, value, score);
        return "添加成功，" + value + " 分数: " + score;
    }

    /** ZSCORE：查询分数 */
    @GetMapping("/score")
    public Double score(@RequestParam String key, @RequestParam String value) {
        return zSetService.score(key, value);
    }

    /** ZREVRANK：查询排名（从高到低） */
    @GetMapping("/rank")
    public String rank(@RequestParam String key, @RequestParam String value) {
        Long rank = zSetService.reverseRank(key, value);
        if (rank == null) {
            return value + " 不在排行榜中";
        }
        return value + " 排名第 " + (rank + 1) + " 名";
    }

    // ========== 排行榜场景 ==========

    /**
     * 玩家加分
     * 示例：POST /redis/zset/rank/addScore?board=game1&player=张三&score=100
     */
    @PostMapping("/rank/addScore")
    public String addScore(@RequestParam String board,
                           @RequestParam String player,
                           @RequestParam double score) {
        String key = "rank:" + board;
        Double newScore = zSetService.incrementScore(key, player, score);
        Long rank = zSetService.reverseRank(key, player);
        return player + " 加了 " + score + " 分，当前总分: " + newScore + "，排名第 " + (rank + 1);
    }

    /**
     * 查看排行榜 TopN
     * 示例：GET /redis/zset/rank/top?board=game1&n=10
     */
    @GetMapping("/rank/top")
    public List<Map<String, Object>> top(@RequestParam String board,
                                          @RequestParam(defaultValue = "10") int n) {
        String key = "rank:" + board;
        Set<ZSetOperations.TypedTuple<Object>> tuples = zSetService.topN(key, 0, n - 1);
        List<Map<String, Object>> result = new ArrayList<>();
        int ranking = 1;
        for (ZSetOperations.TypedTuple<Object> tuple : tuples) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("排名", ranking++);
            item.put("玩家", tuple.getValue());
            item.put("分数", tuple.getScore());
            result.add(item);
        }
        return result;
    }

    /**
     * 查看某个玩家的排名和分数
     * 示例：GET /redis/zset/rank/player?board=game1&player=张三
     */
    @GetMapping("/rank/player")
    public Map<String, Object> playerRank(@RequestParam String board, @RequestParam String player) {
        String key = "rank:" + board;
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("玩家", player);
        result.put("分数", zSetService.score(key, player));
        Long rank = zSetService.reverseRank(key, player);
        result.put("排名", rank == null ? "未上榜" : "第 " + (rank + 1) + " 名");
        return result;
    }
}
