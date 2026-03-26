package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * ==================== 商品 Mapper ====================
 *
 * BaseMapper 已经提供的方法（不用写）：
 * insert / deleteById / updateById / selectById / selectList / selectCount ...
 *
 * 下面是自定义方法，展示 MyBatis-Plus 的三种自定义查询方式：
 * 1. @Select 注解写 SQL（最直观，适合简单查询）
 * 2. @Update 注解写 SQL（修改操作）
 * 3. 复杂查询可以用 XML 文件（这里暂不演示，后面再学）
 *
 * 类比前端：
 * BaseMapper 的方法 = axios 内置的 get/post/put/delete
 * 自定义方法 = 你在 api/ 目录下封装的业务接口
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {

    // ==================== 自定义查询方法 ====================

    /**
     * 【按名称模糊搜索商品】
     *
     * 实际场景：用户在搜索框输入关键词
     * SQL知识点：LIKE 模糊查询
     *
     * 注意 #{keyword} 外面要拼 %，写法是 CONCAT('%', #{keyword}, '%')
     * 不能写成 '%#{keyword}%'，因为 #{} 在引号里会被当成字符串
     *
     * 类比前端：
     * const results = products.filter(p => p.name.includes(keyword))
     */
    @Select("SELECT * FROM products WHERE name LIKE CONCAT('%', #{keyword}, '%')")
    List<Product> searchByName(@Param("keyword") String keyword);

    /**
     * 【按价格区间查询】
     *
     * 实际场景：商品筛选（价格：50-200元）
     * SQL知识点：BETWEEN ... AND ...
     *
     * 类比前端：
     * products.filter(p => p.price >= minPrice && p.price <= maxPrice)
     */
    @Select("SELECT * FROM products " +
            "WHERE price BETWEEN #{minPrice} AND #{maxPrice} " +
            "ORDER BY price ASC")
    List<Product> findByPriceRange(@Param("minPrice") BigDecimal minPrice,
                                   @Param("maxPrice") BigDecimal maxPrice);

    /**
     * 【查询库存不足的商品（预警）】
     *
     * 实际场景：后台管理 → 库存预警列表
     * SQL知识点：WHERE 条件 + ORDER BY
     *
     * 类比前端：
     * products.filter(p => p.stock < threshold).sort((a,b) => a.stock - b.stock)
     */
    @Select("SELECT * FROM products WHERE stock < #{threshold} ORDER BY stock ASC")
    List<Product> findLowStock(@Param("threshold") Integer threshold);

    /**
     * 【查询商品统计信息】
     *
     * 实际场景：后台首页的统计卡片（总商品数、平均价格、总库存等）
     * SQL知识点：聚合函数 COUNT / AVG / SUM / MAX / MIN
     *
     * 类比前端：
     * {
     *   totalCount: products.length,
     *   avgPrice: products.reduce((sum, p) => sum + p.price, 0) / products.length,
     *   totalStock: products.reduce((sum, p) => sum + p.stock, 0)
     * }
     */
    @Select("SELECT " +
            "    COUNT(*) AS total_count, " +
            "    AVG(price) AS avg_price, " +
            "    MAX(price) AS max_price, " +
            "    MIN(price) AS min_price, " +
            "    SUM(stock) AS total_stock " +
            "FROM products")
    Map<String, Object> getProductStats();

    /**
     * 【安全扣库存（带库存检查）】
     *
     * 实际场景：下单时扣库存，防止超卖
     * SQL知识点：UPDATE + WHERE 条件（stock >= quantity 才扣）
     *
     * 返回值：受影响的行数（0 = 库存不足没扣成，1 = 扣成功）
     *
     * 这比直接 UPDATE stock = stock - ? 更安全：
     * 如果库存只剩 2，有人买 5 个，这条 SQL 不会执行（WHERE stock >= 5 不满足）
     * 而不是变成 stock = -3
     *
     * 类比前端：
     * if (product.stock >= quantity) {
     *     product.stock -= quantity;  // 才执行
     * }
     */
    @Update("UPDATE products SET stock = stock - #{quantity} " +
            "WHERE id = #{productId} AND stock >= #{quantity}")
    int deductStock(@Param("productId") Long productId, @Param("quantity") Integer quantity);

    /**
     * 【批量查询指定价格以上的商品，按销量排序】
     *
     * 实际场景：首页推荐（高价值 + 卖得好的商品）
     * SQL知识点：子查询 + LEFT JOIN + GROUP BY + HAVING
     *
     * LEFT JOIN vs INNER JOIN 的区别：
     * INNER JOIN：没有卖出过的商品不显示
     * LEFT JOIN：没有卖出过的商品也显示（sold = 0）
     */
    @Select("SELECT p.*, IFNULL(SUM(oi.quantity), 0) AS total_sold " +
            "FROM products p " +
            "LEFT JOIN order_items oi ON p.id = oi.product_id " +
            "WHERE p.price >= #{minPrice} " +
            "GROUP BY p.id " +
            "ORDER BY total_sold DESC")
    List<Map<String, Object>> findPopularAbovePrice(@Param("minPrice") BigDecimal minPrice);
}
