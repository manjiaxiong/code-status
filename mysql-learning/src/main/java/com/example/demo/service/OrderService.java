package com.example.demo.service;

import com.example.demo.dto.CreateOrderRequest;
import com.example.demo.entity.Product;

import java.util.List;
import java.util.Map;

/**
 * 订单服务接口
 */
public interface OrderService {

    /**
     * 查询所有商品
     */
    List<Product> getAllProducts();

    /**
     * 根据ID查询商品
     */
    Product getProductById(Long id);

    /**
     * 创建商品
     */
    Product createProduct(Product product);

    /**
     * 查询用户的所有订单
     */
    List<Map<String, Object>> getUserOrders(Long userId);

    /**
     * 查询订单详情
     */
    Map<String, Object> getOrderDetail(Long orderId);

    /**
     * 查询所有订单
     */
    List<Map<String, Object>> getAllOrders();

    /**
     * 订单统计
     */
    List<Map<String, Object>> getOrderStats();

    /**
     * 热销商品排行
     */
    List<Map<String, Object>> getHotProducts();

    /**
     * 创建订单（核心业务逻辑）
     */
    Map<String, Object> createOrder(CreateOrderRequest request);
}
