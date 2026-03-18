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
 * 重构后的 Controller 只负责：
 * 1. 接收请求参数
 * 2. 调用 Service 处理业务
 * 3. 返回响应结果
 *
 * 所有业务逻辑都在 OrderServiceImpl 里
 *
 * 类比前端：
 * Controller = React 组件（只管展示和交互）
 * Service = hooks / utils（处理业务逻辑）
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
        try {
            return Result.success(orderService.getProductById(id));
        } catch (Exception e) {
            return Result.error(404, "商品不存在");
        }
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
        System.out.println("下单参数：" + request);
        try {
            Map<String, Object> result = orderService.createOrder(request);
            return Result.success("下单成功", result);
        } catch (IllegalArgumentException e) {
            return Result.error(400, e.getMessage());
        }
    }
}
