package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.dto.CreateOrderRequest;
import com.example.demo.entity.Product;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * ==================== 订单模块 Controller ====================
 *
 * ★★★ 和 mysql-learning 的区别 ★★★
 *
 * 答案：几乎没有区别！！
 *
 * 唯一的小区别：
 * mysql-learning 的 getProduct 用 try-catch（因为 JdbcTemplate 查不到会抛异常）
 * 这里改成了 null 判断（因为 MyBatis-Plus 的 selectById 查不到返回 null）
 *
 * 为什么 Controller 可以几乎不变？
 * 因为三层架构的设计：Controller → Service接口 → Service实现
 * Controller 只依赖 Service 接口，不关心底层是 JdbcTemplate 还是 MyBatis-Plus
 *
 * 类比前端：
 * 你的 React 组件调用 useGetProducts() hook
 * hook 内部是用 fetch 还是 axios，组件根本不知道也不关心
 * 这就是「面向接口编程」的好处
 */
@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // ==================== 商品相关 ====================

    @GetMapping("/products")
    public Result<List<Product>> productList() {
        return Result.success(orderService.getAllProducts());
    }

    @GetMapping("/products/{id}")
    public Result<Product> getProduct(@PathVariable Long id) {
        Product product = orderService.getProductById(id);
        if (product == null) {
            return Result.error(404, "商品不存在");
        }
        return Result.success(product);
    }

    @PostMapping("/products")
    public Result<Product> createProduct(@RequestBody Product product) {
        if (product.getName() == null || product.getPrice() == null) {
            return Result.error(400, "商品名称和价格不能为空");
        }
        return Result.success("创建成功", orderService.createProduct(product));
    }

    // ==================== 订单查询 ====================

    @GetMapping("/orders/user/{userId}")
    public Result<List<Map<String, Object>>> getUserOrders(@PathVariable Long userId) {
        return Result.success(orderService.getUserOrders(userId));
    }

    @GetMapping("/orders/{orderId}/detail")
    public Result<Map<String, Object>> getOrderDetail(@PathVariable Long orderId) {
        try {
            return Result.success(orderService.getOrderDetail(orderId));
        } catch (Exception e) {
            return Result.error(404, "订单不存在");
        }
    }

    @GetMapping("/orders")
    public Result<List<Map<String, Object>>> allOrders() {
        return Result.success(orderService.getAllOrders());
    }

    // ==================== 统计查询 ====================

    @GetMapping("/orders/stats")
    public Result<List<Map<String, Object>>> orderStats() {
        return Result.success(orderService.getOrderStats());
    }

    @GetMapping("/products/hot")
    public Result<List<Map<String, Object>>> hotProducts() {
        return Result.success(orderService.getHotProducts());
    }

    // ==================== 下单 ====================

    @PostMapping("/orders")
    public Result<Map<String, Object>> createOrder(@RequestBody CreateOrderRequest request) {
        try {
            Map<String, Object> result = orderService.createOrder(request);
            return Result.success("下单成功", result);
        } catch (IllegalArgumentException e) {
            return Result.error(400, e.getMessage());
        }
    }
}
