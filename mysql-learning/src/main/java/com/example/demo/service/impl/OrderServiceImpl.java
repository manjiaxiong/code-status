package com.example.demo.service.impl;

import com.example.demo.dto.CreateOrderRequest;
import com.example.demo.entity.Product;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * 订单服务实现类
 *
 * 所有业务逻辑都在这里处理：
 * - 数据校验
 * - 业务计算（金额、库存）
 * - 事务管理
 * - 数据库操作
 *
 * 类比前端：这就像把 API 请求逻辑从组件里抽到单独的 service 文件
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 商品行映射器
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

    // ==================== 商品相关 ====================

    @Override
    public List<Product> getAllProducts() {
        String sql = "SELECT id, name, price, stock, description, create_time FROM products ORDER BY id";
        return jdbcTemplate.query(sql, productRowMapper);
    }

    @Override
    public Product getProductById(Long id) {
        String sql = "SELECT id, name, price, stock, description, create_time FROM products WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, productRowMapper, id);
    }

    @Override
    public Product createProduct(Product product) {
        String sql = "INSERT INTO products (name, price, stock, description, create_time) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
            product.getName(),
            product.getPrice(),
            product.getStock() != null ? product.getStock() : 0,
            product.getDescription(),
            new Date());

        Long id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
        product.setId(id);
        return product;
    }

    // ==================== 订单查询 ====================

    @Override
    public List<Map<String, Object>> getUserOrders(Long userId) {
        String sql =
            "SELECT " +
            "    o.id AS order_id, o.order_no, o.total_amount, o.status, o.create_time, " +
            "    u.username " +
            "FROM orders o " +
            "INNER JOIN users u ON o.user_id = u.id " +
            "WHERE o.user_id = ? " +
            "ORDER BY o.create_time DESC";
        return jdbcTemplate.queryForList(sql, userId);
    }

    @Override
    public Map<String, Object> getOrderDetail(Long orderId) {
        // 查订单基本信息
        String orderSql =
            "SELECT " +
            "    o.id AS order_id, o.order_no, o.total_amount, o.status, o.create_time, " +
            "    u.username " +
            "FROM orders o " +
            "INNER JOIN users u ON o.user_id = u.id " +
            "WHERE o.id = ?";
        Map<String, Object> order = jdbcTemplate.queryForMap(orderSql, orderId);

        // 查订单明细
        String itemsSql =
            "SELECT " +
            "    oi.quantity, oi.price, " +
            "    p.name AS product_name, p.price AS current_price, " +
            "    (oi.quantity * oi.price) AS subtotal " +
            "FROM order_items oi " +
            "INNER JOIN products p ON oi.product_id = p.id " +
            "WHERE oi.order_id = ?";
        List<Map<String, Object>> items = jdbcTemplate.queryForList(itemsSql, orderId);

        order.put("items", items);
        return order;
    }

    @Override
    public List<Map<String, Object>> getAllOrders() {
        String sql =
            "SELECT " +
            "    o.id AS order_id, o.order_no, o.total_amount, o.status, o.create_time, " +
            "    u.username, " +
            "    (SELECT COUNT(*) FROM order_items oi WHERE oi.order_id = o.id) AS item_count " +
            "FROM orders o " +
            "INNER JOIN users u ON o.user_id = u.id " +
            "ORDER BY o.create_time DESC";
        return jdbcTemplate.queryForList(sql);
    }

    @Override
    public List<Map<String, Object>> getOrderStats() {
        String sql =
            "SELECT " +
            "    u.username, " +
            "    COUNT(o.id) AS order_count, " +
            "    SUM(o.total_amount) AS total_spent, " +
            "    AVG(o.total_amount) AS avg_amount, " +
            "    MAX(o.total_amount) AS max_order, " +
            "    MIN(o.total_amount) AS min_order " +
            "FROM orders o " +
            "INNER JOIN users u ON o.user_id = u.id " +
            "GROUP BY u.id, u.username " +
            "ORDER BY total_spent DESC";
        return jdbcTemplate.queryForList(sql);
    }

    @Override
    public List<Map<String, Object>> getHotProducts() {
        String sql =
            "SELECT " +
            "    p.name, p.price, " +
            "    SUM(oi.quantity) AS total_sold, " +
            "    SUM(oi.quantity * oi.price) AS total_revenue " +
            "FROM order_items oi " +
            "INNER JOIN products p ON oi.product_id = p.id " +
            "GROUP BY p.id, p.name, p.price " +
            "ORDER BY total_sold DESC " +
            "LIMIT 10";
        return jdbcTemplate.queryForList(sql);
    }

    // ==================== 下单（事务） ====================

    @Override
    @Transactional
    public Map<String, Object> createOrder(CreateOrderRequest request) {
        Long userId = request.getUserId();
        List<CreateOrderRequest.OrderItemDTO> items = request.getItems();

        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("商品不能为空");
        }

        // 1. 计算总金额 & 检查库存
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<Map<String, Object>> itemDetails = new ArrayList<>();

        for (CreateOrderRequest.OrderItemDTO item : items) {
            Map<String, Object> product;
            try {
                product = jdbcTemplate.queryForMap(
                    "SELECT id, name, price, stock FROM products WHERE id = ?", item.getProductId());
            } catch (Exception e) {
                throw new IllegalArgumentException("商品不存在，ID: " + item.getProductId());
            }

            int stock = ((Number) product.get("stock")).intValue();
            if (stock < item.getQuantity()) {
                throw new IllegalArgumentException(product.get("name") + " 库存不足，剩余: " + stock);
            }

            BigDecimal price = new BigDecimal(product.get("price").toString());
            totalAmount = totalAmount.add(price.multiply(new BigDecimal(item.getQuantity())));

            Map<String, Object> detail = new HashMap<>();
            detail.put("productId", item.getProductId());
            detail.put("quantity", item.getQuantity());
            detail.put("price", price);
            itemDetails.add(detail);
        }

        // 2. 生成订单号
        String orderNo = "ORD" + System.currentTimeMillis();

        // 3. 插入订单
        jdbcTemplate.update(
            "INSERT INTO orders (user_id, order_no, total_amount, status) VALUES (?, ?, ?, 0)",
            userId, orderNo, totalAmount);
        Long orderId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);

        // 4. 插入明细 & 扣库存
        for (Map<String, Object> detail : itemDetails) {
            Long productId = (Long) detail.get("productId");
            int quantity = (int) detail.get("quantity");
            BigDecimal price = (BigDecimal) detail.get("price");

            jdbcTemplate.update(
                "INSERT INTO order_items (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)",
                orderId, productId, quantity, price);
            jdbcTemplate.update(
                "UPDATE products SET stock = stock - ? WHERE id = ?",
                quantity, productId);
        }

        // 5. 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("orderId", orderId);
        result.put("orderNo", orderNo);
        result.put("totalAmount", totalAmount);
        result.put("itemCount", itemDetails.size());
        return result;
    }
}
