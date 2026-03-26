package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.demo.dto.CreateOrderRequest;
import com.example.demo.entity.Order;
import com.example.demo.entity.OrderItem;
import com.example.demo.entity.Product;
import com.example.demo.mapper.OrderItemMapper;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * ==================== 订单服务实现（MyBatis-Plus 版） ====================
 *
 * 对比 mysql-learning 的 JdbcTemplate 版本，主要区别：
 *
 * 1. 单表操作不用写 SQL 了
 *    旧：jdbcTemplate.query("SELECT * FROM products ORDER BY id", rowMapper)
 *    新：productMapper.selectList(null)  ← 一行搞定
 *
 * 2. 不需要手写 RowMapper 映射了
 *    旧：new RowMapper<Product>() { 一大堆映射代码 }
 *    新：MyBatis-Plus 自动根据字段名映射
 *
 * 3. 条件查询用 Wrapper（链式调用）
 *    旧：jdbcTemplate.queryForObject("SELECT ... WHERE id = ?", rowMapper, id)
 *    新：productMapper.selectById(id)  ← 直接用
 *    或：productMapper.selectList(new QueryWrapper<Product>().eq("stock", 0))
 *
 * 4. 多表 JOIN 还是写 SQL，但放在 Mapper 里（关注点分离）
 *
 * 类比前端：
 * JdbcTemplate = 每个接口手写 fetch + 手动解析 response
 * MyBatis-Plus = 用 axios 实例 + 封装好的 API 模块
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    // ==================== 商品相关 ====================

    /**
     * 查询所有商品
     *
     * 旧（JdbcTemplate）：
     *   String sql = "SELECT id, name, price, stock, description, create_time FROM products ORDER BY id";
     *   return jdbcTemplate.query(sql, productRowMapper);
     *
     * 新（MyBatis-Plus）：
     *   return productMapper.selectList(null);  ← 就一行！
     *
     * selectList(null) 的 null 表示没有查询条件 = SELECT * FROM products
     */
    @Override
    public List<Product> getAllProducts() {
        // null = 无条件，查全部
        return productMapper.selectList(null);
    }

    /**
     * 根据 ID 查询商品
     *
     * 旧：jdbcTemplate.queryForObject("SELECT ... WHERE id = ?", rowMapper, id)
     * 新：productMapper.selectById(id)
     */
    @Override
    public Product getProductById(Long id) {
        return productMapper.selectById(id);
    }

    /**
     * 创建商品
     *
     * 旧：jdbcTemplate.update("INSERT INTO products (...) VALUES (?, ?, ?, ?, ?)", ...);
     *     Long id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
     *
     * 新：productMapper.insert(product);  ← 自动插入，自动回填 ID！
     */
    @Override
    public Product createProduct(Product product) {
        if (product.getStock() == null) {
            product.setStock(0);
        }
        product.setCreateTime(new Date());

        // insert 后，MyBatis-Plus 自动把生成的 ID 设置回 product 对象
        // 不需要再 SELECT LAST_INSERT_ID() 了
        productMapper.insert(product);
        return product;
    }

    // ==================== 订单查询（多表 JOIN） ====================

    /**
     * 查询用户的订单
     * 多表 JOIN 查询 → 调用 Mapper 里的自定义 SQL
     * SQL 不在 Service 里了，而是在 OrderMapper 的 @Select 注解里
     */
    @Override
    public List<Map<String, Object>> getUserOrders(Long userId) {
        return orderMapper.getUserOrders(userId);
    }

    @Override
    public Map<String, Object> getOrderDetail(Long orderId) {
        // 查订单基本信息
        Map<String, Object> order = orderMapper.getOrderWithUser(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        // 查订单明细
        List<Map<String, Object>> items = orderItemMapper.getItemsByOrderId(orderId);
        order.put("items", items);
        return order;
    }

    @Override
    public List<Map<String, Object>> getAllOrders() {
        return orderMapper.getAllOrdersWithUser();
    }

    @Override
    public List<Map<String, Object>> getOrderStats() {
        return orderMapper.getOrderStats();
    }

    @Override
    public List<Map<String, Object>> getHotProducts() {
        return orderItemMapper.getHotProducts();
    }

    // ==================== 下单（事务） ====================

    /**
     * 创建订单
     *
     * 对比 JdbcTemplate 版本：
     *
     * 旧：手写 4 条 SQL（查商品、插订单、插明细、扣库存）
     * 新：单表操作用 Mapper 方法，复杂条件用 Wrapper
     *
     * 重点看 扣库存 的写法对比：
     * 旧：jdbcTemplate.update("UPDATE products SET stock = stock - ? WHERE id = ?", quantity, productId)
     * 新：UpdateWrapper + setSql("stock = stock - #{quantity}")
     */
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
        List<OrderItem> orderItems = new ArrayList<>();

        for (CreateOrderRequest.OrderItemDTO item : items) {
            // selectById 直接返回实体对象（JdbcTemplate 要手写 SQL + RowMapper）
            Product product = productMapper.selectById(item.getProductId());
            if (product == null) {
                throw new IllegalArgumentException("商品不存在，ID: " + item.getProductId());
            }
            if (product.getStock() < item.getQuantity()) {
                throw new IllegalArgumentException(product.getName() + " 库存不足，剩余: " + product.getStock());
            }

            BigDecimal price = product.getPrice();
            totalAmount = totalAmount.add(price.multiply(new BigDecimal(item.getQuantity())));

            // 构建明细对象
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(item.getProductId());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(price);
            orderItem.setCreateTime(new Date());
            orderItems.add(orderItem);
        }

        // 2. 创建订单对象 & 插入
        Order order = new Order();
        order.setUserId(userId);
        order.setOrderNo("ORD" + System.currentTimeMillis());
        order.setTotalAmount(totalAmount);
        order.setStatus(0);
        order.setCreateTime(new Date());

        // insert 自动回填 ID（不需要 SELECT LAST_INSERT_ID()）
        orderMapper.insert(order);

        // 3. 插入明细 & 扣库存
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrderId(order.getId());
            orderItemMapper.insert(orderItem);

            /**
             * 扣库存 —— 用 UpdateWrapper
             *
             * 旧（JdbcTemplate）：
             * jdbcTemplate.update("UPDATE products SET stock = stock - ? WHERE id = ?",
             *     quantity, productId);
             *
             * 新（MyBatis-Plus UpdateWrapper）：
             * 链式调用，类似前端的：
             * query.where('id', productId).set('stock', 'stock - quantity')
             */
            UpdateWrapper<Product> updateWrapper = new UpdateWrapper<>();
            updateWrapper
                .eq("id", orderItem.getProductId())                       // WHERE id = ?
                .setSql("stock = stock - " + orderItem.getQuantity());    // SET stock = stock - ?
            productMapper.update(null, updateWrapper);
        }

        // 4. 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("orderId", order.getId());
        result.put("orderNo", order.getOrderNo());
        result.put("totalAmount", totalAmount);
        result.put("itemCount", orderItems.size());
        return result;
    }

    // ==================== 新增方法（对应 Mapper 的自定义查询） ====================

    @Override
    public List<Product> searchProducts(String keyword) {
        return productMapper.searchByName(keyword);
    }

    @Override
    public List<Product> findProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return productMapper.findByPriceRange(minPrice, maxPrice);
    }

    @Override
    public List<Product> findLowStockProducts(Integer threshold) {
        return productMapper.findLowStock(threshold);
    }

    @Override
    public Map<String, Object> getProductStats() {
        return productMapper.getProductStats();
    }

    @Override
    public List<Map<String, Object>> findPopularAbovePrice(BigDecimal minPrice) {
        return productMapper.findPopularAbovePrice(minPrice);
    }

    @Override
    public List<Map<String, Object>> findOrdersByStatus(Integer status) {
        return orderMapper.findByStatus(status);
    }

    @Override
    public List<Map<String, Object>> findOrdersByDateRange(String startDate, String endDate) {
        return orderMapper.findByDateRange(startDate, endDate);
    }

    @Override
    public boolean updateOrderStatus(Long orderId, Integer oldStatus, Integer newStatus) {
        return orderMapper.updateStatus(orderId, oldStatus, newStatus) > 0;
    }

    @Override
    public List<Map<String, Object>> findTopOrders(Integer limit) {
        return orderMapper.findTopByAmount(limit);
    }

    @Override
    public List<Map<String, Object>> getDailyStats(Integer days) {
        return orderMapper.getDailyStats(days);
    }

    @Override
    public List<Map<String, Object>> getProductBuyHistory(Long productId) {
        return orderItemMapper.getProductBuyHistory(productId);
    }

    @Override
    public List<Map<String, Object>> getUserBoughtProducts(Long userId) {
        return orderItemMapper.getUserBoughtProducts(userId);
    }

    @Override
    public Map<String, Object> getOrderSummary(Long orderId) {
        return orderItemMapper.getOrderSummary(orderId);
    }

    @Override
    public List<Map<String, Object>> findNeverBoughtProducts() {
        return orderItemMapper.findNeverBoughtProducts();
    }
}
