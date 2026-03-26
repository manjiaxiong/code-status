package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
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
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    // ==================== 原有方法 ====================

    /**
     * 查询用户的所有订单（JOIN users 表拿 username）
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

    // ==================== 新增自定义方法 ====================

    /**
     * 【按订单状态查询】
     *
     * 实际场景：后台管理 → 待发货列表、已完成列表
     * SQL知识点：WHERE + JOIN + 状态码
     *
     * status 含义：0待付款 1已付款 2已发货 3已完成
     *
     * 类比前端：
     * orders.filter(o => o.status === status)
     */
    @Select("SELECT " +
            "    o.id AS order_id, o.order_no, o.total_amount, o.status, o.create_time, " +
            "    u.username " +
            "FROM orders o " +
            "INNER JOIN users u ON o.user_id = u.id " +
            "WHERE o.status = #{status} " +
            "ORDER BY o.create_time DESC")
    List<Map<String, Object>> findByStatus(@Param("status") Integer status);

    /**
     * 【按日期范围查询订单】
     *
     * 实际场景：报表 → "查看本月订单" / "查看上周订单"
     * SQL知识点：BETWEEN + 日期比较
     *
     * 参数用 String 类型，格式 "2024-01-01"
     *
     * 类比前端：
     * orders.filter(o => o.createTime >= startDate && o.createTime <= endDate)
     */
    @Select("SELECT " +
            "    o.id AS order_id, o.order_no, o.total_amount, o.status, o.create_time, " +
            "    u.username " +
            "FROM orders o " +
            "INNER JOIN users u ON o.user_id = u.id " +
            "WHERE o.create_time BETWEEN #{startDate} AND #{endDate} " +
            "ORDER BY o.create_time DESC")
    List<Map<String, Object>> findByDateRange(@Param("startDate") String startDate,
                                               @Param("endDate") String endDate);

    /**
     * 【修改订单状态】
     *
     * 实际场景：卖家发货、买家确认收货
     * SQL知识点：UPDATE + WHERE 多条件
     *
     * 返回值：受影响行数（0=失败，1=成功）
     *
     * 注意：加了 AND status = #{oldStatus} 条件
     * 防止重复操作（比如已发货的订单不能再"发货"）
     * 这叫「乐观锁」思想：只有当前状态符合预期才允许修改
     *
     * 类比前端：
     * if (order.status === oldStatus) {
     *     order.status = newStatus;  // 才允许改
     * }
     */
    @Update("UPDATE orders SET status = #{newStatus} " +
            "WHERE id = #{orderId} AND status = #{oldStatus}")
    int updateStatus(@Param("orderId") Long orderId,
                     @Param("oldStatus") Integer oldStatus,
                     @Param("newStatus") Integer newStatus);

    /**
     * 【查询金额最高的 N 个订单】
     *
     * 实际场景：后台首页 → "大额订单提醒"
     * SQL知识点：ORDER BY + LIMIT + JOIN
     *
     * 类比前端：
     * orders.sort((a,b) => b.totalAmount - a.totalAmount).slice(0, limit)
     */
    @Select("SELECT " +
            "    o.id AS order_id, o.order_no, o.total_amount, o.status, o.create_time, " +
            "    u.username " +
            "FROM orders o " +
            "INNER JOIN users u ON o.user_id = u.id " +
            "ORDER BY o.total_amount DESC " +
            "LIMIT #{limit}")
    List<Map<String, Object>> findTopByAmount(@Param("limit") Integer limit);

    /**
     * 【按日期统计每日订单数和总额】
     *
     * 实际场景：报表 → 折线图（每天的订单数和收入）
     * SQL知识点：DATE() 函数 + GROUP BY 日期
     *
     * DATE(create_time) 从 datetime 中提取日期部分
     * 比如 "2024-03-19 14:30:00" → "2024-03-19"
     *
     * 类比前端：
     * // 按日期分组统计
     * const dailyStats = orders.reduce((acc, order) => {
     *     const date = order.createTime.split(' ')[0];
     *     acc[date] = acc[date] || { count: 0, total: 0 };
     *     acc[date].count++;
     *     acc[date].total += order.totalAmount;
     *     return acc;
     * }, {});
     */
    @Select("SELECT " +
            "    DATE(create_time) AS order_date, " +
            "    COUNT(*) AS order_count, " +
            "    SUM(total_amount) AS daily_total " +
            "FROM orders " +
            "GROUP BY DATE(create_time) " +
            "ORDER BY order_date DESC " +
            "LIMIT #{days}")
    List<Map<String, Object>> getDailyStats(@Param("days") Integer days);
}
