package com.example.demo.service;

import com.example.demo.dto.CreateOrderRequest;
import com.example.demo.entity.Product;

import java.util.List;
import java.util.Map;

/**
 * 订单服务接口（和 mysql-learning 的一模一样）
 * 接口不变，实现方式从 JdbcTemplate 换成 MyBatis-Plus
 */
public interface OrderService {

    List<Product> getAllProducts();

    Product getProductById(Long id);

    Product createProduct(Product product);

    List<Map<String, Object>> getUserOrders(Long userId);

    Map<String, Object> getOrderDetail(Long orderId);

    List<Map<String, Object>> getAllOrders();

    List<Map<String, Object>> getOrderStats();

    List<Map<String, Object>> getHotProducts();

    Map<String, Object> createOrder(CreateOrderRequest request);
}
