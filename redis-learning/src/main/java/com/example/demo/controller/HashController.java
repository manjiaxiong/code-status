package com.example.demo.controller;

import com.example.demo.service.HashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Hash 操作 Controller
 *
 * ========== 实战场景：购物车 ==========
 * 设计思路：
 *   key = "cart:用户ID"      （每个用户一个购物车）
 *   field = "商品ID"          （购物车里的每件商品）
 *   value = "数量"            （该商品的购买数量）
 *
 * 前端类比：
 *   相当于一个 Map<userId, Map<productId, quantity>>
 *   或者 { "cart:1": { "iphone": 2, "airpods": 1 } }
 *
 * 为什么用 Hash 而不是 String？
 *   String：要存整个购物车的 JSON，改一个商品要读出来 → 改 → 写回去
 *   Hash：直接操作某个字段，加商品、改数量、删商品都是一步到位
 *   就像前端用 setState({ ...old, [productId]: qty }) vs 直接 map.set(productId, qty)
 */
@RestController
@RequestMapping("/redis/hash")
public class HashController {

    @Autowired
    private HashService hashService;

    // ========== 基础操作 ==========

    /** HSET：设置字段 */
    @PostMapping("/put")
    public String put(@RequestParam String key, @RequestParam String field, @RequestParam String value) {
        hashService.put(key, field, value);
        return "设置成功";
    }

    /** HGET：获取字段 */
    @GetMapping("/get")
    public Object get(@RequestParam String key, @RequestParam String field) {
        return hashService.get(key, field);
    }

    /** HGETALL：获取所有字段 */
    @GetMapping("/getAll")
    public Map<Object, Object> getAll(@RequestParam String key) {
        return hashService.getAll(key);
    }

    /** HDEL：删除字段 */
    @DeleteMapping("/delete")
    public String delete(@RequestParam String key, @RequestParam String field) {
        hashService.delete(key, field);
        return "删除成功";
    }

    // ========== 购物车场景 ==========

    /**
     * 添加商品到购物车
     * 示例：POST /redis/hash/cart/add?userId=1&productId=iphone&quantity=2
     */
    @PostMapping("/cart/add")
    public String cartAdd(@RequestParam String userId,
                          @RequestParam String productId,
                          @RequestParam int quantity) {
        String cartKey = "cart:" + userId;
        hashService.put(cartKey, productId, String.valueOf(quantity));
        return "已添加 " + productId + " x" + quantity + " 到购物车";
    }

    /**
     * 修改购物车商品数量（+1 / -1）
     * 示例：POST /redis/hash/cart/increment?userId=1&productId=iphone&delta=1
     */
    @PostMapping("/cart/increment")
    public String cartIncrement(@RequestParam String userId,
                                @RequestParam String productId,
                                @RequestParam long delta) {
        String cartKey = "cart:" + userId;
        Long newQty = hashService.increment(cartKey, productId, delta);
        return productId + " 数量变更为 " + newQty;
    }

    /**
     * 查看购物车
     * 示例：GET /redis/hash/cart?userId=1
     */
    @GetMapping("/cart")
    public Map<Object, Object> cartList(@RequestParam String userId) {
        return hashService.getAll("cart:" + userId);
    }

    /**
     * 从购物车删除商品
     * 示例：DELETE /redis/hash/cart/remove?userId=1&productId=iphone
     */
    @DeleteMapping("/cart/remove")
    public String cartRemove(@RequestParam String userId, @RequestParam String productId) {
        hashService.delete("cart:" + userId, productId);
        return "已从购物车移除 " + productId;
    }
}
