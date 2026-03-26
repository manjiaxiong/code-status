package com.example.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * ==================== SQL 关键词学习 Mapper ====================
 *
 * 把所有常用 SQL 关键词整理在一起，每个关键词配一个真实业务场景
 * 方便你复习的时候一眼看懂每个关键词的作用
 *
 * 类比前端：这就像一份 Array 方法速查表
 * WHERE  = .filter()
 * SELECT = .map()
 * ORDER BY = .sort()
 * LIMIT = .slice()
 * GROUP BY = .reduce()
 */
@Mapper
public interface SqlKeywordMapper {

    // ====================================================================
    // 1. WHERE —— 筛选条件（最基础的）
    // 类比前端：array.filter(item => item.price > 100)
    // ====================================================================

    /** 查询价格大于指定值的商品 */
    @Select("SELECT * FROM products WHERE price > #{price}")
    List<Map<String, Object>> demo_WHERE(@Param("price") double price);


    // ====================================================================
    // 2. AND / OR —— 多条件组合
    // 类比前端：array.filter(item => item.price > 100 && item.stock > 0)
    //          array.filter(item => item.status === 0 || item.status === 1)
    // ====================================================================

    /** AND：价格大于 X 并且有库存 */
    @Select("SELECT * FROM products WHERE price > #{price} AND stock > 0")
    List<Map<String, Object>> demo_AND(@Param("price") double price);

    /** OR：查待付款或已付款的订单 */
    @Select("SELECT * FROM orders WHERE status = 0 OR status = 1")
    List<Map<String, Object>> demo_OR();


    // ====================================================================
    // 3. IN —— 在某个集合里
    // 类比前端：array.filter(item => [1, 2, 3].includes(item.id))
    // 注意：MyBatis 的 IN 写法比较特殊，需要用 foreach，这里用简单写法演示
    // ====================================================================

    /** 查询指定状态的订单（0待付款, 1已付款） */
    @Select("SELECT * FROM orders WHERE status IN (0, 1)")
    List<Map<String, Object>> demo_IN();


    // ====================================================================
    // 4. NOT / NOT IN —— 取反
    // 类比前端：array.filter(item => !excludeIds.includes(item.id))
    // ====================================================================

    /** 查不是"已完成"的订单 */
    @Select("SELECT * FROM orders WHERE status NOT IN (3)")
    List<Map<String, Object>> demo_NOT_IN();

    /** 查有库存的商品（stock 不等于 0） */
    @Select("SELECT * FROM products WHERE stock != 0")
    List<Map<String, Object>> demo_NOT_EQUAL();


    // ====================================================================
    // 5. LIKE —— 模糊搜索
    // 类比前端：array.filter(item => item.name.includes('手机'))
    //
    // 三种用法：
    // '%手机%'  包含"手机"     = includes('手机')
    // '手机%'   以"手机"开头   = startsWith('手机')
    // '%手机'   以"手机"结尾   = endsWith('手机')
    // ====================================================================

    /** 商品名包含关键词 */
    @Select("SELECT * FROM products WHERE name LIKE CONCAT('%', #{keyword}, '%')")
    List<Map<String, Object>> demo_LIKE(@Param("keyword") String keyword);


    // ====================================================================
    // 6. BETWEEN —— 范围查询（包含两端）
    // 类比前端：array.filter(item => item.price >= 50 && item.price <= 200)
    // ====================================================================

    /** 价格在 50~200 之间的商品 */
    @Select("SELECT * FROM products WHERE price BETWEEN #{min} AND #{max}")
    List<Map<String, Object>> demo_BETWEEN(@Param("min") double min, @Param("max") double max);


    // ====================================================================
    // 7. IS NULL / IS NOT NULL —— 判断空值
    // 类比前端：array.filter(item => item.description === null)
    //          array.filter(item => item.description !== null)
    //
    // 注意：SQL 里不能用 = null，必须用 IS NULL！
    // 错误：WHERE description = null   ← 这不会返回任何结果
    // 正确：WHERE description IS NULL
    // ====================================================================

    /** 查没有描述的商品 */
    @Select("SELECT * FROM products WHERE description IS NULL")
    List<Map<String, Object>> demo_IS_NULL();

    /** 查有描述的商品 */
    @Select("SELECT * FROM products WHERE description IS NOT NULL")
    List<Map<String, Object>> demo_IS_NOT_NULL();


    // ====================================================================
    // 8. ORDER BY —— 排序
    // ASC = 升序（小到大，默认）   DESC = 降序（大到小）
    // 类比前端：array.sort((a, b) => a.price - b.price)       // ASC
    //          array.sort((a, b) => b.price - a.price)       // DESC
    // ====================================================================

    /** 按价格从低到高 */
    @Select("SELECT * FROM products ORDER BY price ASC")
    List<Map<String, Object>> demo_ORDER_BY_ASC();

    /** 按价格从高到低 */
    @Select("SELECT * FROM products ORDER BY price DESC")
    List<Map<String, Object>> demo_ORDER_BY_DESC();

    /** 多字段排序：先按库存升序，库存相同再按价格降序 */
    @Select("SELECT * FROM products ORDER BY stock ASC, price DESC")
    List<Map<String, Object>> demo_ORDER_BY_MULTI();


    // ====================================================================
    // 9. LIMIT —— 限制返回条数
    // 类比前端：array.slice(0, 5)              // 取前5条
    //          array.slice(10, 15)            // 跳过10条，取5条（分页）
    //
    // LIMIT 5        = 取前5条
    // LIMIT 10, 5    = 跳过10条，取5条（第3页，每页5条）
    // ====================================================================

    /** 取前 N 条 */
    @Select("SELECT * FROM products ORDER BY price DESC LIMIT #{n}")
    List<Map<String, Object>> demo_LIMIT(@Param("n") int n);

    /** 分页：第 page 页，每页 size 条 */
    @Select("SELECT * FROM products LIMIT #{offset}, #{size}")
    List<Map<String, Object>> demo_LIMIT_OFFSET(@Param("offset") int offset, @Param("size") int size);


    // ====================================================================
    // 10. DISTINCT —— 去重
    // 类比前端：[...new Set(array.map(item => item.userId))]
    // ====================================================================

    /** 查有哪些用户下过单（去重） */
    @Select("SELECT DISTINCT user_id FROM orders")
    List<Map<String, Object>> demo_DISTINCT();


    // ====================================================================
    // 11. COUNT / SUM / AVG / MAX / MIN —— 聚合函数
    // 类比前端：
    // COUNT = array.length
    // SUM   = array.reduce((s, i) => s + i.price, 0)
    // AVG   = sum / array.length
    // MAX   = Math.max(...array.map(i => i.price))
    // MIN   = Math.min(...array.map(i => i.price))
    // ====================================================================

    /** 统计商品表的各种数据 */
    @Select("SELECT " +
            "  COUNT(*) AS total_count, " +
            "  SUM(price) AS total_price, " +
            "  AVG(price) AS avg_price, " +
            "  MAX(price) AS max_price, " +
            "  MIN(price) AS min_price " +
            "FROM products")
    Map<String, Object> demo_AGGREGATE();


    // ====================================================================
    // 12. GROUP BY —— 分组（配合聚合函数用）
    // 类比前端：
    // Object.groupBy(orders, order => order.status)
    // 然后对每组求 count / sum
    // ====================================================================

    /** 按订单状态分组，统计每种状态有几个订单 */
    @Select("SELECT status, COUNT(*) AS count FROM orders GROUP BY status")
    List<Map<String, Object>> demo_GROUP_BY();


    // ====================================================================
    // 13. HAVING —— 分组后的筛选条件
    //
    // WHERE  = 分组前筛选（过滤原始数据行）
    // HAVING = 分组后筛选（过滤聚合结果）
    //
    // 类比前端：
    // Object.groupBy(orders, o => o.userId)  // GROUP BY
    //   .filter(group => group.length >= 2)  // HAVING COUNT >= 2
    // ====================================================================

    /** 查下单 2 次以上的用户 */
    @Select("SELECT user_id, COUNT(*) AS order_count " +
            "FROM orders " +
            "GROUP BY user_id " +
            "HAVING COUNT(*) >= #{minCount}")
    List<Map<String, Object>> demo_HAVING(@Param("minCount") int minCount);


    // ====================================================================
    // 14. INNER JOIN —— 内连接（两表都有匹配才返回）
    // 类比前端：
    // orders.map(order => ({
    //     ...order,
    //     user: users.find(u => u.id === order.userId)  // 找不到就不要这条
    // })).filter(o => o.user)
    // ====================================================================

    /** 查订单和对应的用户名 */
    @Select("SELECT o.order_no, o.total_amount, u.username " +
            "FROM orders o " +
            "INNER JOIN users u ON o.user_id = u.id")
    List<Map<String, Object>> demo_INNER_JOIN();


    // ====================================================================
    // 15. LEFT JOIN —— 左连接（左表全保留，右表没有就填 NULL）
    // 类比前端：
    // products.map(p => ({
    //     ...p,
    //     sold: orderItems.find(oi => oi.productId === p.id)?.quantity ?? null
    // }))
    // 区别：INNER JOIN 没匹配的会丢掉，LEFT JOIN 保留（右边填 NULL）
    // ====================================================================

    /** 查所有商品及其销量（没卖过的也显示，销量为 NULL） */
    @Select("SELECT p.name, p.price, oi.quantity " +
            "FROM products p " +
            "LEFT JOIN order_items oi ON p.id = oi.product_id")
    List<Map<String, Object>> demo_LEFT_JOIN();


    // ====================================================================
    // 16. 子查询（Subquery）—— SQL 里嵌套 SQL
    // 类比前端：
    // const maxPrice = Math.max(...products.map(p => p.price))
    // const result = products.filter(p => p.price === maxPrice)
    // ====================================================================

    /** 查价格最高的商品（子查询找最大值） */
    @Select("SELECT * FROM products " +
            "WHERE price = (SELECT MAX(price) FROM products)")
    List<Map<String, Object>> demo_SUBQUERY();


    // ====================================================================
    // 17. EXISTS —— 存在性判断
    // 类比前端：array.some(item => 条件)
    //
    // 和 IN 的区别：
    // IN 先执行子查询，把结果全拿出来再比较（数据多时慢）
    // EXISTS 逐行检查是否存在匹配（大数据量时更快）
    // ====================================================================

    /** 查有订单的用户（用 EXISTS） */
    @Select("SELECT * FROM users u " +
            "WHERE EXISTS (SELECT 1 FROM orders o WHERE o.user_id = u.id)")
    List<Map<String, Object>> demo_EXISTS();


    // ====================================================================
    // 18. CASE WHEN —— 条件表达式（SQL 版的三元运算符）
    // 类比前端：
    // orders.map(o => ({
    //     ...o,
    //     statusText: o.status === 0 ? '待付款'
    //               : o.status === 1 ? '已付款'
    //               : o.status === 2 ? '已发货' : '已完成'
    // }))
    // ====================================================================

    /** 把订单状态码转成中文 */
    @Select("SELECT order_no, total_amount, " +
            "  CASE status " +
            "    WHEN 0 THEN '待付款' " +
            "    WHEN 1 THEN '已付款' " +
            "    WHEN 2 THEN '已发货' " +
            "    WHEN 3 THEN '已完成' " +
            "    ELSE '未知' " +
            "  END AS status_text " +
            "FROM orders")
    List<Map<String, Object>> demo_CASE_WHEN();


    // ====================================================================
    // 19. IFNULL / COALESCE —— 空值替换
    // 类比前端：value ?? defaultValue   或   value || defaultValue
    //
    // IFNULL(a, b)     = a ?? b  （MySQL 专用）
    // COALESCE(a, b, c) = a ?? b ?? c （SQL 标准，所有数据库通用）
    // ====================================================================

    /** 商品描述为空时显示"暂无描述" */
    @Select("SELECT name, price, IFNULL(description, '暂无描述') AS description FROM products")
    List<Map<String, Object>> demo_IFNULL();


    // ====================================================================
    // 20. DATE 函数 —— 日期处理
    // 类比前端：
    // new Date(timestamp).toISOString().split('T')[0]  // 取日期部分
    //
    // 常用日期函数：
    // DATE(datetime)          → 取日期部分 '2024-03-19'
    // YEAR/MONTH/DAY(date)    → 取年/月/日
    // DATE_FORMAT(date, fmt)  → 格式化
    // DATEDIFF(a, b)          → 两个日期相差几天
    // NOW()                   → 当前时间
    // ====================================================================

    /** 按月统计订单数 */
    @Select("SELECT " +
            "  DATE_FORMAT(create_time, '%Y-%m') AS month, " +
            "  COUNT(*) AS order_count, " +
            "  SUM(total_amount) AS monthly_total " +
            "FROM orders " +
            "GROUP BY DATE_FORMAT(create_time, '%Y-%m') " +
            "ORDER BY month DESC")
    List<Map<String, Object>> demo_DATE_FORMAT();


    // ====================================================================
    // 21. UNION —— 合并多个查询结果
    // 类比前端：[...array1, ...array2] 然后去重
    //
    // UNION     = 合并并去重
    // UNION ALL = 合并不去重（性能更好）
    // ====================================================================

    /** 合并"高价商品"和"低库存商品"（可能有重叠） */
    @Select("SELECT id, name, '高价商品' AS tag FROM products WHERE price > 500 " +
            "UNION " +
            "SELECT id, name, '低库存' AS tag FROM products WHERE stock < 10")
    List<Map<String, Object>> demo_UNION();


    // ====================================================================
    // 22. AS —— 别名（给列或表起别名）
    // 类比前端：const { name: productName } = product  // 解构重命名
    //
    // 列别名：SELECT price AS 价格
    // 表别名：FROM products p（p 就是 products 的简称）
    // ====================================================================

    /** 给查询结果起中文别名 */
    @Select("SELECT " +
            "  name AS 商品名, " +
            "  price AS 价格, " +
            "  stock AS 库存 " +
            "FROM products")
    List<Map<String, Object>> demo_AS();
}
