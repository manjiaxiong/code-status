package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * ==================== 订单明细 Mapper ====================
 *
 * ★★★ 和 mysql-learning 的区别 ★★★
 *
 * mysql-learning 没有 Mapper 层！SQL 全部写在 OrderServiceImpl.java 里
 * 现在 SQL 和业务逻辑分离，各管各的
 * 类比前端：之前 API 请求写在组件里，现在抽到 api/ 目录
 */
@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {

    // ==================== 原有方法 ====================

    /**
     * 查询订单的所有商品明细（JOIN products 表）
     */
    @Select("SELECT " +
            "    oi.quantity, oi.price, " +
            "    p.name AS product_name, p.price AS current_price, " +
            "    (oi.quantity * oi.price) AS subtotal " +
            "FROM order_items oi " +
            "INNER JOIN products p ON oi.product_id = p.id " +
            "WHERE oi.order_id = #{orderId}")
    List<Map<String, Object>> getItemsByOrderId(@Param("orderId") Long orderId);

    /**
     * 热销商品排行
     */
    @Select("SELECT " +
            "    p.name, p.price, " +
            "    SUM(oi.quantity) AS total_sold, " +
            "    SUM(oi.quantity * oi.price) AS total_revenue " +
            "FROM order_items oi " +
            "INNER JOIN products p ON oi.product_id = p.id " +
            "GROUP BY p.id, p.name, p.price " +
            "ORDER BY total_sold DESC " +
            "LIMIT 10")
    List<Map<String, Object>> getHotProducts();

    // ==================== 新增自定义方法 ====================

    /**
     * 【查询某个商品被哪些订单买过】
     *
     * 实际场景：商品详情页 → "购买记录"
     * SQL知识点：多表 JOIN（3张表关联）
     *
     * order_items → orders（拿订单信息）
     * order_items → products（拿商品信息，这里不需要因为已知 productId）
     * orders → users（拿买家信息）
     *
     * 类比前端：
     * // 从订单明细反查订单和用户
     * orderItems
     *   .filter(item => item.productId === productId)
     *   .map(item => ({
     *       ...item,
     *       order: orders.find(o => o.id === item.orderId),
     *       user: users.find(u => u.id === order.userId)
     *   }))
     */
    @Select("SELECT " +
            "    oi.quantity, oi.price AS buy_price, " +
            "    o.order_no, o.create_time AS order_time, " +
            "    u.username AS buyer " +
            "FROM order_items oi " +
            "INNER JOIN orders o ON oi.order_id = o.id " +
            "INNER JOIN users u ON o.user_id = u.id " +
            "WHERE oi.product_id = #{productId} " +
            "ORDER BY o.create_time DESC")
    List<Map<String, Object>> getProductBuyHistory(@Param("productId") Long productId);

    /**
     * 【查询用户买过的所有商品（去重）】
     *
     * 实际场景："我买过的商品" 列表
     * SQL知识点：DISTINCT + 多表 JOIN
     *
     * DISTINCT 去重：同一个商品买了多次只显示一条
     *
     * 类比前端：
     * const boughtProducts = [...new Set(
     *     myOrderItems.map(item => item.productId)
     * )].map(id => products.find(p => p.id === id))
     */
    @Select("SELECT DISTINCT " +
            "    p.id, p.name, p.price, p.description, " +
            "    SUM(oi.quantity) AS total_bought " +
            "FROM order_items oi " +
            "INNER JOIN orders o ON oi.order_id = o.id " +
            "INNER JOIN products p ON oi.product_id = p.id " +
            "WHERE o.user_id = #{userId} " +
            "GROUP BY p.id, p.name, p.price, p.description " +
            "ORDER BY total_bought DESC")
    List<Map<String, Object>> getUserBoughtProducts(@Param("userId") Long userId);

    /**
     * 【查询某个订单的商品数量和总金额（汇总）】
     *
     * 实际场景：订单列表显示 "共3件商品，合计¥299"
     * SQL知识点：聚合函数 SUM / COUNT
     *
     * 注意 SUM(quantity) 和 COUNT(*) 的区别：
     * 假设一个订单有2条明细：iPhone x1, 手机壳 x3
     * COUNT(*) = 2（2条明细记录）
     * SUM(quantity) = 4（总共4件商品）
     *
     * 类比前端：
     * {
     *   itemTypes: orderItems.length,                              // 2种商品
     *   totalQuantity: orderItems.reduce((s, i) => s + i.quantity, 0), // 4件
     *   totalAmount: orderItems.reduce((s, i) => s + i.price * i.quantity, 0)
     * }
     */
    @Select("SELECT " +
            "    COUNT(*) AS item_types, " +
            "    SUM(quantity) AS total_quantity, " +
            "    SUM(quantity * price) AS total_amount " +
            "FROM order_items " +
            "WHERE order_id = #{orderId}")
    Map<String, Object> getOrderSummary(@Param("orderId") Long orderId);

    /**
     * 【查询从未被购买过的商品】
     *
     * 实际场景：运营分析 → 哪些商品上架了但没人买
     * SQL知识点：LEFT JOIN + IS NULL（反向查询）
     *
     * LEFT JOIN 的特点：
     * 左表（products）的所有记录都会保留
     * 如果右表（order_items）没有匹配的记录，则右表字段为 NULL
     * WHERE oi.id IS NULL → 过滤出"右表没有匹配的"= 没有被购买过的商品
     *
     * 这是 SQL 面试高频题！
     *
     * 类比前端：
     * const neverBought = products.filter(p =>
     *     !orderItems.some(oi => oi.productId === p.id)
     * )
     */
    @Select("SELECT p.* " +
            "FROM products p " +
            "LEFT JOIN order_items oi ON p.id = oi.product_id " +
            "WHERE oi.id IS NULL")
    List<Map<String, Object>> findNeverBoughtProducts();
}
