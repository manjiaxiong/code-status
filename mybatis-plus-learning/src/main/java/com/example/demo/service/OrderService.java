package com.example.demo.service;

import com.example.demo.dto.CreateOrderRequest;
import com.example.demo.entity.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 订单服务接口
 *
 * ★★★ 和 mysql-learning 的区别 ★★★
 * 前面的方法和 mysql-learning 一模一样（接口不变，实现变了）
 * 后面新增了很多方法，展示 MyBatis-Plus 自定义查询的能力
 */
public interface OrderService {

    // ==================== 原有方法（和 mysql-learning 一样） ====================

    List<Product> getAllProducts();

    Product getProductById(Long id);

    Product createProduct(Product product);

    List<Map<String, Object>> getUserOrders(Long userId);

    Map<String, Object> getOrderDetail(Long orderId);

    List<Map<String, Object>> getAllOrders();

    List<Map<String, Object>> getOrderStats();

    List<Map<String, Object>> getHotProducts();

    Map<String, Object> createOrder(CreateOrderRequest request);

    // ==================== 新增方法（MyBatis-Plus 自定义查询） ====================

    /** 按名称模糊搜索商品 */
    List<Product> searchProducts(String keyword);

    /** 按价格区间查询商品 */
    List<Product> findProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);

    /** 查询库存不足的商品 */
    List<Product> findLowStockProducts(Integer threshold);

    /** 商品统计信息 */
    Map<String, Object> getProductStats();

    /** 高价值热销商品 */
    List<Map<String, Object>> findPopularAbovePrice(BigDecimal minPrice);

    /** 按状态查询订单 */
    List<Map<String, Object>> findOrdersByStatus(Integer status);

    /** 按日期范围查询订单 */
    List<Map<String, Object>> findOrdersByDateRange(String startDate, String endDate);

    /** 修改订单状态 */
    boolean updateOrderStatus(Long orderId, Integer oldStatus, Integer newStatus);

    /** 金额最高的 N 个订单 */
    List<Map<String, Object>> findTopOrders(Integer limit);

    /** 每日订单统计 */
    List<Map<String, Object>> getDailyStats(Integer days);

    /** 商品购买记录 */
    List<Map<String, Object>> getProductBuyHistory(Long productId);

    /** 用户买过的所有商品 */
    List<Map<String, Object>> getUserBoughtProducts(Long userId);

    /** 订单汇总（商品数量+总金额） */
    Map<String, Object> getOrderSummary(Long orderId);

    /** 从未被购买过的商品 */
    List<Map<String, Object>> findNeverBoughtProducts();
}
