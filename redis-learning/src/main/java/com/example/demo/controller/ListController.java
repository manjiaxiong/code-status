package com.example.demo.controller;

import com.example.demo.service.ListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * List 操作 Controller
 *
 * ========== 实战场景 1：消息队列 ==========
 * 生产者从左边 LPUSH 放入消息，消费者从右边 RPOP 取出消息
 * 先进先出（FIFO），就像排队一样
 *
 * ========== 实战场景 2：最新消息列表 ==========
 * 每次有新消息 LPUSH 到头部
 * 用 LRANGE 0 9 取最新 10 条
 * 用 LTRIM 0 99 只保留最新 100 条，防止无限增长
 *
 * 前端类比：
 *   消息队列 = 任务队列 / Promise.all 的串行执行
 *   最新消息 = 聊天记录列表，最新的在最上面
 */
@RestController
@RequestMapping("/redis/list")
public class ListController {

    @Autowired
    private ListService listService;

    // ========== 基础操作 ==========

    /** LPUSH：从左边推入 */
    @PostMapping("/lpush")
    public String leftPush(@RequestParam String key, @RequestParam String value) {
        Long size = listService.leftPush(key, value);
        return "推入成功，当前长度: " + size;
    }

    /** RPUSH：从右边推入 */
    @PostMapping("/rpush")
    public String rightPush(@RequestParam String key, @RequestParam String value) {
        Long size = listService.rightPush(key, value);
        return "推入成功，当前长度: " + size;
    }

    /** LPOP：从左边弹出 */
    @PostMapping("/lpop")
    public Object leftPop(@RequestParam String key) {
        return listService.leftPop(key);
    }

    /** RPOP：从右边弹出 */
    @PostMapping("/rpop")
    public Object rightPop(@RequestParam String key) {
        return listService.rightPop(key);
    }

    /** LRANGE：获取范围内的元素 */
    @GetMapping("/range")
    public List<Object> range(@RequestParam String key,
                              @RequestParam(defaultValue = "0") long start,
                              @RequestParam(defaultValue = "-1") long end) {
        return listService.range(key, start, end);
    }

    // ========== 消息队列场景 ==========

    /**
     * 发送消息（生产者）
     * 示例：POST /redis/list/mq/send?queue=order-queue&message=新订单001
     */
    @PostMapping("/mq/send")
    public String mqSend(@RequestParam String queue, @RequestParam String message) {
        listService.leftPush("mq:" + queue, message);
        return "消息已发送: " + message;
    }

    /**
     * 消费消息（消费者）
     * 示例：POST /redis/list/mq/consume?queue=order-queue
     */
    @PostMapping("/mq/consume")
    public Object mqConsume(@RequestParam String queue) {
        Object message = listService.rightPop("mq:" + queue);
        if (message == null) {
            return "队列为空，无消息可消费";
        }
        return "消费到消息: " + message;
    }

    // ========== 最新消息场景 ==========

    /**
     * 发布新消息
     * 示例：POST /redis/list/news/publish?channel=tech&message=Java21发布了
     */
    @PostMapping("/news/publish")
    public String newsPublish(@RequestParam String channel, @RequestParam String message) {
        String key = "news:" + channel;
        listService.leftPush(key, message);
        // 只保留最新 100 条
        listService.trim(key, 0, 99);
        return "消息已发布";
    }

    /**
     * 获取最新 N 条消息
     * 示例：GET /redis/list/news/latest?channel=tech&count=10
     */
    @GetMapping("/news/latest")
    public List<Object> newsLatest(@RequestParam String channel,
                                   @RequestParam(defaultValue = "10") int count) {
        return listService.range("news:" + channel, 0, count - 1);
    }
}
