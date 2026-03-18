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
 * 比如热销商品查询，之前是这样的：
 *
 *   // 在 OrderServiceImpl.java 里（service 层混着 SQL）
 *   public List<Map<String, Object>> getHotProducts() {
 *       String sql = "SELECT p.name, p.price, SUM(oi.quantity) AS total_sold...";
 *       return jdbcTemplate.queryForList(sql);
 *   }
 *
 * 现在改成了：
 *   // SQL 在 Mapper 里（关注点分离）
 *   @Select("SELECT p.name, p.price, SUM(oi.quantity) AS total_sold...")
 *   List<Map<String, Object>> getHotProducts();
 *
 *   // Service 里只管调用
 *   public List<Map<String, Object>> getHotProducts() {
 *       return orderItemMapper.getHotProducts();  // 一行搞定
 *   }
 *
 * 好处：SQL 和业务逻辑分离，各管各的
 * 类比前端：之前 API 请求写在组件里，现在抽到 api/ 目录
 */
@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {

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
}
