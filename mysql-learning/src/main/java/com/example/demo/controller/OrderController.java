package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * ==================== 订单模块 - 多表关联实战 ====================
 *
 * 这个 Controller 演示真实业务中最常用的 SQL 操作：
 * - JOIN：多表关联查询
 * - GROUP BY：分组统计
 * - 事务：下单时保证数据一致性
 *
 * 涉及的表：
 * users（用户） → orders（订单） → order_items（订单明细） ← products（商品）
 *
 * 类比前端：
 * 就像你调接口拿到嵌套的 JSON 数据，后端要通过 JOIN 把多张表"拼"成一个结果
 */
@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 商品行映射器：把数据库查询结果的每一行映射成 Product 对象
     *
     * 类比前端：
     * 相当于 response.data.map(row => ({
     *     id: row.id,
     *     name: row.name,
     *     price: row.price,
     *     ...
     * }))
     */
    private RowMapper<Product> productRowMapper = new RowMapper<Product>() {
        @Override
        public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
            Product product = new Product();
            product.setId(rs.getLong("id"));
            product.setName(rs.getString("name"));
            product.setPrice(rs.getBigDecimal("price"));
            product.setStock(rs.getInt("stock"));
            product.setDescription(rs.getString("description"));
            product.setCreateTime(rs.getTimestamp("create_time"));
            return product;
        }
    };

    // =====================================================
    // ==================== 商品相关 ====================
    // =====================================================

    /**
     * 查询所有商品
     *
     * 访问：GET http://localhost:18080/api/products
     *
     * SQL 知识点：基础 SELECT，最简单的查询
     *
     * 注意返回值是 Result<List<Product>>，不是 Map
     * 这样前端拿到的 JSON 字段是确定的，不会多也不会少
     */
    @GetMapping("/products")
    public Result<List<Product>> productList() {
        String sql = "SELECT id, name, price, stock, description, create_time FROM products ORDER BY id";
        List<Product> products = jdbcTemplate.query(sql, productRowMapper);
        return Result.success(products);
    }

    /**
     * 根据 ID 查询单个商品
     *
     * 访问：GET http://localhost:18080/api/products/1
     */
    @GetMapping("/products/{id}")
    public Result<Product> getProduct(@PathVariable Long id) {
        String sql = "SELECT id, name, price, stock, description, create_time FROM products WHERE id = ?";
        try {
            Product product = jdbcTemplate.queryForObject(sql, productRowMapper, id);
            return Result.success(product);
        } catch (Exception e) {
            return Result.error(404, "商品不存在");
        }
    }

    /**
     * 新增商品
     *
     * 访问：POST http://localhost:18080/api/products
     * 请求体：{"name":"小米手机","price":2999.00,"stock":200,"description":"小米14"}
     *
     * 参数用 Product 实体类接收，字段就是确定的
     */
    @PostMapping("/products")
    public Result<Product> createProduct(@RequestBody Product product) {
        if (product.getName() == null || product.getPrice() == null) {
            return Result.error(400, "商品名称和价格不能为空");
        }

        String sql = "INSERT INTO products (name, price, stock, description, create_time) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
            product.getName(),
            product.getPrice(),
            product.getStock() != null ? product.getStock() : 0,
            product.getDescription(),
            new Date());

        Long id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
        product.setId(id);
        return Result.success("创建成功", product);
    }

    // =====================================================
    // ==================== 订单查询 ====================
    // =====================================================

    /**
     * 查询某个用户的所有订单
     *
     * 访问：GET http://localhost:18080/api/orders/user/1
     *
     * SQL 知识点：INNER JOIN（内连接）
     *
     * INNER JOIN 的作用：把两张表通过某个字段"连"起来
     * 这里通过 orders.user_id = users.id 把订单和用户连起来
     *
     * 类比前端：
     * 相当于你有两个数组 users[] 和 orders[]
     * 你要根据 order.userId === user.id 把它们合并
     * JavaScript 写法：orders.map(o => ({ ...o, username: users.find(u => u.id === o.userId).username }))
     * SQL 一行搞定：SELECT ... FROM orders JOIN users ON ...
     *
     * 注意：这里返回 Map 是因为 JOIN 查询的结果跨了两张表（orders + users），
     * 没有一个现成的实体类能完全对应。实际项目中会定义一个 VO（View Object）来接收。
     */
    @GetMapping("/orders/user/{userId}")
    public Result<List<Map<String, Object>>> getUserOrders(@PathVariable Long userId) {
        String sql =
            "SELECT " +
            "    o.id AS order_id, " +          // AS 起别名，避免多张表字段名冲突
            "    o.order_no, " +
            "    o.total_amount, " +
            "    o.status, " +
            "    o.create_time, " +
            "    u.username " +                  // 从 users 表拿用户名
            "FROM orders o " +                   // o 是 orders 的别名，写起来短
            "INNER JOIN users u ON o.user_id = u.id " +  // 关联条件
            "WHERE o.user_id = ? " +             // 筛选某个用户
            "ORDER BY o.create_time DESC";       // 最新的排前面

        List<Map<String, Object>> orders = jdbcTemplate.queryForList(sql, userId);
        return Result.success(orders);
    }

    /**
     * 查询订单详情（包含订单信息 + 商品明细）
     *
     * 访问：GET http://localhost:18080/api/orders/1/detail
     *
     * SQL 知识点：多表 JOIN（三张表关联）
     *
     * 这是最实用的查询：一个订单里买了哪些商品，每个商品多少钱
     *
     * 类比前端：
     * 最终要拼成这样的数据：
     * {
     *   orderNo: "ORD20260318001",
     *   username: "张三",
     *   items: [
     *     { productName: "iPhone 15", quantity: 1, price: 5999 },
     *     { productName: "AirPods Pro", quantity: 1, price: 1799 }
     *   ]
     * }
     * 在数据库里，这些数据分散在 3 张表，需要 JOIN 拼起来
     */
    @GetMapping("/orders/{orderId}/detail")
    public Result<Map<String, Object>> getOrderDetail(@PathVariable Long orderId) {
        // 第一步：查订单基本信息（orders JOIN users）
        String orderSql =
            "SELECT " +
            "    o.id AS order_id, " +
            "    o.order_no, " +
            "    o.total_amount, " +
            "    o.status, " +
            "    o.create_time, " +
            "    u.username " +
            "FROM orders o " +
            "INNER JOIN users u ON o.user_id = u.id " +
            "WHERE o.id = ?";

        Map<String, Object> order;
        try {
            order = jdbcTemplate.queryForMap(orderSql, orderId);
        } catch (Exception e) {
            return Result.error(404, "订单不存在");
        }

        // 第二步：查订单明细（order_items JOIN products）
        String itemsSql =
            "SELECT " +
            "    oi.quantity, " +                // 购买数量
            "    oi.price, " +                   // 下单时的价格
            "    p.name AS product_name, " +     // 商品名（从 products 表拿）
            "    p.price AS current_price, " +   // 商品现价（可能和下单时不同）
            "    (oi.quantity * oi.price) AS subtotal " +  // 小计 = 数量 × 单价
            "FROM order_items oi " +
            "INNER JOIN products p ON oi.product_id = p.id " +
            "WHERE oi.order_id = ?";

        List<Map<String, Object>> items = jdbcTemplate.queryForList(itemsSql, orderId);

        // 第三步：组装结果（类似前端里把数据 merge 到一个对象）
        order.put("items", items);

        return Result.success(order);
    }

    /**
     * 查询所有订单（带用户名 + 商品数量）
     *
     * 访问：GET http://localhost:18080/api/orders
     *
     * SQL 知识点：JOIN + 子查询
     */
    @GetMapping("/orders")
    public Result<List<Map<String, Object>>> allOrders() {
        String sql =
            "SELECT " +
            "    o.id AS order_id, " +
            "    o.order_no, " +
            "    o.total_amount, " +
            "    o.status, " +
            "    o.create_time, " +
            "    u.username, " +
            "    (SELECT COUNT(*) FROM order_items oi WHERE oi.order_id = o.id) AS item_count " +  // 子查询：统计每个订单有几个商品
            "FROM orders o " +
            "INNER JOIN users u ON o.user_id = u.id " +
            "ORDER BY o.create_time DESC";

        List<Map<String, Object>> orders = jdbcTemplate.queryForList(sql);
        return Result.success(orders);
    }

    // =====================================================
    // ==================== 统计查询 ====================
    // =====================================================

    /**
     * 订单统计：每个用户的订单数和总消费
     *
     * 访问：GET http://localhost:18080/api/orders/stats
     *
     * SQL 知识点：GROUP BY（分组） + 聚合函数（COUNT、SUM）
     *
     * GROUP BY 的作用：按某个字段分组，然后对每组做统计
     *
     * 类比前端：
     * 相当于你有一个订单数组，要用 reduce 按用户分组统计：
     * orders.reduce((acc, order) => {
     *   acc[order.userId] = acc[order.userId] || { count: 0, total: 0 };
     *   acc[order.userId].count++;
     *   acc[order.userId].total += order.amount;
     *   return acc;
     * }, {})
     *
     * SQL 一句话搞定 ↓
     */
    @GetMapping("/orders/stats")
    public Result<List<Map<String, Object>>> orderStats() {
        String sql =
            "SELECT " +
            "    u.username, " +
            "    COUNT(o.id) AS order_count, " +       // 订单数
            "    SUM(o.total_amount) AS total_spent, " + // 总消费
            "    AVG(o.total_amount) AS avg_amount, " +  // 平均每单金额
            "    MAX(o.total_amount) AS max_order, " +   // 最大一单
            "    MIN(o.total_amount) AS min_order " +    // 最小一单
            "FROM orders o " +
            "INNER JOIN users u ON o.user_id = u.id " +
            "GROUP BY u.id, u.username " +               // 按用户分组
            "ORDER BY total_spent DESC";                 // 消费最多的排前面

        List<Map<String, Object>> stats = jdbcTemplate.queryForList(sql);
        return Result.success(stats);
    }

    /**
     * 热销商品排行
     *
     * 访问：GET http://localhost:18080/api/products/hot
     *
     * SQL 知识点：GROUP BY + ORDER BY + LIMIT
     *
     * 类比前端：
     * 统计每个商品被买了多少次，然后排序取前 N 个
     */
    @GetMapping("/products/hot")
    public Result<List<Map<String, Object>>> hotProducts() {
        String sql =
            "SELECT " +
            "    p.name, " +
            "    p.price, " +
            "    SUM(oi.quantity) AS total_sold, " +           // 总销量
            "    SUM(oi.quantity * oi.price) AS total_revenue " + // 总销售额
            "FROM order_items oi " +
            "INNER JOIN products p ON oi.product_id = p.id " +
            "GROUP BY p.id, p.name, p.price " +
            "ORDER BY total_sold DESC " +                      // 销量最高的排前面
            "LIMIT 10";                                        // 只取前10个

        List<Map<String, Object>> hot = jdbcTemplate.queryForList(sql);
        return Result.success(hot);
    }

    // =====================================================
    // ==================== 下单（事务） ====================
    // =====================================================

    /**
     * 下单接口
     *
     * 访问：POST http://localhost:18080/api/orders
     * 请求体：
     * {
     *   "userId": 1,
     *   "items": [
     *     {"productId": 1, "quantity": 2},
     *     {"productId": 3, "quantity": 1}
     *   ]
     * }
     *
     * SQL 知识点：事务（@Transactional）
     *
     * 事务的作用：下单涉及多步操作（建订单 → 加明细 → 扣库存），
     * 必须要么全部成功，要么全部回滚，不能出现"订单建了但库存没扣"的情况
     *
     * 类比前端：
     * 想象你做一个表单提交，要同时调 3 个接口：
     * - 如果第 2 个失败了，第 1 个的数据也要撤回
     * - 数据库事务就是自动帮你处理这种"要么全成功要么全撤回"的逻辑
     */
    @PostMapping("/orders")
    @Transactional  // 这个注解让方法里的所有数据库操作成为一个事务
    public Result<Map<String, Object>> createOrder(@RequestBody Map<String, Object> request) {
        // 1. 解析参数
        Long userId = Long.valueOf(request.get("userId").toString());
        List<Map<String, Object>> items = (List<Map<String, Object>>) request.get("items");

        if (items == null || items.isEmpty()) {
            return Result.error(400, "商品不能为空");
        }

        // 2. 计算总金额 & 检查库存
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<Map<String, Object>> itemDetails = new ArrayList<>();

        for (Map<String, Object> item : items) {
            Long productId = Long.valueOf(item.get("productId").toString());
            int quantity = Integer.parseInt(item.get("quantity").toString());

            // 查商品信息
            Map<String, Object> product;
            try {
                product = jdbcTemplate.queryForMap(
                    "SELECT id, name, price, stock FROM products WHERE id = ?", productId);
            } catch (Exception e) {
                return Result.error(404, "商品不存在，ID: " + productId);
            }

            // 检查库存
            int stock = ((Number) product.get("stock")).intValue();
            if (stock < quantity) {
                return Result.error(400, product.get("name") + " 库存不足，剩余: " + stock);
            }

            BigDecimal price = new BigDecimal(product.get("price").toString());
            totalAmount = totalAmount.add(price.multiply(new BigDecimal(quantity)));

            Map<String, Object> detail = new HashMap<>();
            detail.put("productId", productId);
            detail.put("quantity", quantity);
            detail.put("price", price);
            itemDetails.add(detail);
        }

        // 3. 生成订单号（时间戳 + 随机数，实际项目会用更复杂的方案）
        String orderNo = "ORD" + System.currentTimeMillis();

        // 4. 插入订单
        jdbcTemplate.update(
            "INSERT INTO orders (user_id, order_no, total_amount, status) VALUES (?, ?, ?, 0)",
            userId, orderNo, totalAmount);

        // 获取刚插入的订单 ID
        Long orderId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);

        // 5. 插入订单明细 & 扣减库存
        for (Map<String, Object> detail : itemDetails) {
            Long productId = (Long) detail.get("productId");
            int quantity = (int) detail.get("quantity");
            BigDecimal price = (BigDecimal) detail.get("price");

            // 插入明细
            jdbcTemplate.update(
                "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)",
                orderId, productId, quantity, price);

            // 扣减库存
            jdbcTemplate.update(
                "UPDATE products SET stock = stock - ? WHERE id = ?",
                quantity, productId);
        }

        // 6. 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("orderId", orderId);
        result.put("orderNo", orderNo);
        result.put("totalAmount", totalAmount);
        result.put("itemCount", itemDetails.size());

        return Result.success("下单成功", result);
    }
}
