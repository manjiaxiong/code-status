package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * ==================== 订单 Mapper ====================
 *
 * 单表查询：继承 BaseMapper 自动搞定
 * 多表 JOIN：用 @Select 注解写 SQL
 *
 * 这就是 MyBatis-Plus 比 JPA 灵活的地方：
 * - 简单操作：自动生成（和 JPA 一样爽）
 * - 复杂操作：手写 SQL（比 JPA 的 JPQL 直观）
 *
 * 类比前端：
 * 简单请求 → axios.get('/orders')
 * 复杂请求 → axios.get('/orders', { params: { join: true, ... } })
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    /**
     * 查询用户的所有订单（JOIN users 表拿 username）
     *
     * 对比 JdbcTemplate 版本：
     * 之前：jdbcTemplate.queryForList(sql, userId)  —— SQL 写在 Service 里
     * 现在：SQL 写在 Mapper 里（关注点分离，更清晰）
     *
     * @Select 注解 = 直接写 SQL，最直观
     */
    @Select("SELECT " +
            "    o.id AS order_id, o.order_no, o.total_amount, o.status, o.create_time, " +
            "    u.username " +
            "FROM orders o " +
            "INNER JOIN users u ON o.user_id = u.id " +
            "WHERE o.user_id = #{userId} " +
            "ORDER BY o.create_time DESC")
    List<Map<String, Object>> getUserOrders(@Param("userId") Long userId);

    /**
     * 查询所有订单（带用户名和商品数量）
     */
    @Select("SELECT " +
            "    o.id AS order_id, o.order_no, o.total_amount, o.status, o.create_time, " +
            "    u.username, " +
            "    (SELECT COUNT(*) FROM order_items oi WHERE oi.order_id = o.id) AS item_count " +
            "FROM orders o " +
            "INNER JOIN users u ON o.user_id = u.id " +
            "ORDER BY o.create_time DESC")
    List<Map<String, Object>> getAllOrdersWithUser();

    /**
     * 查询订单基本信息（含用户名）
     */
    @Select("SELECT " +
            "    o.id AS order_id, o.order_no, o.total_amount, o.status, o.create_time, " +
            "    u.username " +
            "FROM orders o " +
            "INNER JOIN users u ON o.user_id = u.id " +
            "WHERE o.id = #{orderId}")
    Map<String, Object> getOrderWithUser(@Param("orderId") Long orderId);

    /**
     * 订单统计（按用户分组）
     */
    @Select("SELECT " +
            "    u.username, " +
            "    COUNT(o.id) AS order_count, " +
            "    SUM(o.total_amount) AS total_spent, " +
            "    AVG(o.total_amount) AS avg_amount, " +
            "    MAX(o.total_amount) AS max_order, " +
            "    MIN(o.total_amount) AS min_order " +
            "FROM orders o " +
            "INNER JOIN users u ON o.user_id = u.id " +
            "GROUP BY u.id, u.username " +
            "ORDER BY total_spent DESC")
    List<Map<String, Object>> getOrderStats();
}
