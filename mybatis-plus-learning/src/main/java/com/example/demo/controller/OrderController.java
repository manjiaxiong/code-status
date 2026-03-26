package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.dto.CreateOrderRequest;
import com.example.demo.entity.Product;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * ==================== 订单模块 Controller ====================
 *
 * ★★★ 和 mysql-learning 的区别 ★★★
 *
 * 上半部分接口和 mysql-learning 完全一样（体现三层架构的好处）
 * 下半部分是新增的接口，展示 MyBatis-Plus 自定义查询的能力
 *
 * 新增接口一览：
 * GET /api/products/search?keyword=手机     → 模糊搜索
 * GET /api/products/price-range?min=50&max=200 → 价格区间
 * GET /api/products/low-stock?threshold=10  → 库存预警
 * GET /api/products/stats                   → 商品统计
 * GET /api/products/popular?minPrice=100    → 高价值热销
 * GET /api/products/{id}/history            → 商品购买记录
 * GET /api/products/never-bought            → 从未售出商品
 * GET /api/orders/status/{status}           → 按状态查订单
 * GET /api/orders/date-range?start=xx&end=xx → 按日期查订单
 * PUT /api/orders/{id}/status               → 改订单状态
 * GET /api/orders/top?limit=5               → 大额订单
 * GET /api/orders/daily-stats?days=7        → 每日统计
 * GET /api/orders/{id}/summary              → 订单汇总
 * GET /api/orders/user/{userId}/products    → 用户买过的商品
 */
@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // ==================== 商品相关（和 mysql-learning 一样） ====================

    @GetMapping("/products")
    public Result<List<Product>> productList() {
        return Result.success(orderService.getAllProducts());
    }

    @GetMapping("/products/{id}")
    public Result<Product> getProduct(@PathVariable Long id) {
        Product product = orderService.getProductById(id);
        if (product == null) {
            return Result.error(404, "商品不存在");
        }
        return Result.success(product);
    }

    @PostMapping("/products")
    public Result<Product> createProduct(@RequestBody Product product) {
        if (product.getName() == null || product.getPrice() == null) {
            return Result.error(400, "商品名称和价格不能为空");
        }
        return Result.success("创建成功", orderService.createProduct(product));
    }

    // ==================== 订单查询（和 mysql-learning 一样） ====================

    @GetMapping("/orders/user/{userId}")
    public Result<List<Map<String, Object>>> getUserOrders(@PathVariable Long userId) {
        return Result.success(orderService.getUserOrders(userId));
    }

    /**
     * 不用写 try-catch 了！
     * 如果 Service 抛出 RuntimeException，GlobalExceptionHandler 会自动捕获
     * 并返回 {"code": 500, "message": "操作失败：订单不存在"}
     */
    @GetMapping("/orders/{orderId}/detail")
    public Result<Map<String, Object>> getOrderDetail(@PathVariable Long orderId) {
        return Result.success(orderService.getOrderDetail(orderId));
    }

    @GetMapping("/orders")
    public Result<List<Map<String, Object>>> allOrders() {
        return Result.success(orderService.getAllOrders());
    }

    // ==================== 统计查询（和 mysql-learning 一样） ====================

    @GetMapping("/orders/stats")
    public Result<List<Map<String, Object>>> orderStats() {
        return Result.success(orderService.getOrderStats());
    }

    @GetMapping("/products/hot")
    public Result<List<Map<String, Object>>> hotProducts() {
        return Result.success(orderService.getHotProducts());
    }

    // ==================== 下单（和 mysql-learning 一样） ====================

    /**
     * 不用写 try-catch 了！
     * Service 抛出 IllegalArgumentException → GlobalExceptionHandler 返回 400
     * Service 抛出其他异常 → GlobalExceptionHandler 返回 500
     */
    @PostMapping("/orders")
    public Result<Map<String, Object>> createOrder(@RequestBody CreateOrderRequest request) {
        Map<String, Object> result = orderService.createOrder(request);
        return Result.success("下单成功", result);
    }

    // ====================================================================
    // ==================== 以下是新增接口（mysql-learning 没有） ====================
    // ====================================================================

    // ==================== 商品高级查询 ====================

    /**
     * 模糊搜索商品
     * GET /api/products/search?keyword=手机
     *
     * @RequestParam 获取 URL 上的查询参数（?key=value）
     * 类比前端：const { keyword } = useSearchParams()
     *
     * 对比 @PathVariable：
     * @PathVariable  → /products/123     → 从路径里取
     * @RequestParam  → /products?id=123  → 从查询参数取
     */
    @GetMapping("/products/search")
    public Result<List<Product>> searchProducts(@RequestParam String keyword) {
        return Result.success(orderService.searchProducts(keyword));
    }

    /**
     * 按价格区间查询
     * GET /api/products/price-range?min=50&max=200
     */
    @GetMapping("/products/price-range")
    public Result<List<Product>> priceRange(@RequestParam BigDecimal min,
                                            @RequestParam BigDecimal max) {
        return Result.success(orderService.findProductsByPriceRange(min, max));
    }

    /**
     * 库存预警（库存低于阈值的商品）
     * GET /api/products/low-stock?threshold=10
     *
     * defaultValue = "10"：如果不传参数，默认阈值为 10
     * 类比前端：const threshold = searchParams.get('threshold') ?? 10
     */
    @GetMapping("/products/low-stock")
    public Result<List<Product>> lowStock(
            @RequestParam(defaultValue = "10") Integer threshold) {
        return Result.success(orderService.findLowStockProducts(threshold));
    }

    /**
     * 商品统计信息（总数、均价、总库存等）
     * GET /api/products/stats
     */
    @GetMapping("/products/stats")
    public Result<Map<String, Object>> productStats() {
        return Result.success(orderService.getProductStats());
    }

    /**
     * 高价值热销商品（价格 >= minPrice，按销量排序）
     * GET /api/products/popular?minPrice=100
     */
    @GetMapping("/products/popular")
    public Result<List<Map<String, Object>>> popularProducts(
            @RequestParam(defaultValue = "0") BigDecimal minPrice) {
        return Result.success(orderService.findPopularAbovePrice(minPrice));
    }

    /**
     * 商品购买记录（谁买了这个商品）
     * GET /api/products/3/history
     */
    @GetMapping("/products/{productId}/history")
    public Result<List<Map<String, Object>>> productHistory(@PathVariable Long productId) {
        return Result.success(orderService.getProductBuyHistory(productId));
    }

    /**
     * 从未被购买的商品
     * GET /api/products/never-bought
     */
    @GetMapping("/products/never-bought")
    public Result<List<Map<String, Object>>> neverBought() {
        return Result.success(orderService.findNeverBoughtProducts());
    }

    // ==================== 订单高级查询 ====================

    /**
     * 按状态查询订单
     * GET /api/orders/status/0  → 查待付款订单
     * GET /api/orders/status/1  → 查已付款订单
     */
    @GetMapping("/orders/status/{status}")
    public Result<List<Map<String, Object>>> ordersByStatus(@PathVariable Integer status) {
        return Result.success(orderService.findOrdersByStatus(status));
    }

    /**
     * 按日期范围查询
     * GET /api/orders/date-range?start=2024-01-01&end=2024-12-31
     */
    @GetMapping("/orders/date-range")
    public Result<List<Map<String, Object>>> ordersByDateRange(
            @RequestParam String start,
            @RequestParam String end) {
        return Result.success(orderService.findOrdersByDateRange(start, end));
    }

    /**
     * 修改订单状态
     * PUT /api/orders/5/status
     * Body: { "oldStatus": 0, "newStatus": 1 }
     *
     * 用 PUT 而不是 POST，因为是「修改」操作
     * RESTful 规范：POST=创建，PUT=修改，DELETE=删除
     */
    @PutMapping("/orders/{orderId}/status")
    public Result<String> updateStatus(@PathVariable Long orderId,
                                        @RequestBody Map<String, Integer> body) {
        Integer oldStatus = body.get("oldStatus");
        Integer newStatus = body.get("newStatus");
        boolean success = orderService.updateOrderStatus(orderId, oldStatus, newStatus);
        if (success) {
            return Result.success("状态更新成功", null);
        }
        return Result.error(400, "状态更新失败（订单不存在或当前状态不匹配）");
    }

    /**
     * 金额最高的 N 个订单
     * GET /api/orders/top?limit=5
     */
    @GetMapping("/orders/top")
    public Result<List<Map<String, Object>>> topOrders(
            @RequestParam(defaultValue = "5") Integer limit) {
        return Result.success(orderService.findTopOrders(limit));
    }

    /**
     * 每日订单统计（最近 N 天）
     * GET /api/orders/daily-stats?days=7
     */
    @GetMapping("/orders/daily-stats")
    public Result<List<Map<String, Object>>> dailyStats(
            @RequestParam(defaultValue = "7") Integer days) {
        return Result.success(orderService.getDailyStats(days));
    }

    /**
     * 订单汇总（商品种类数、总件数、总金额）
     * GET /api/orders/5/summary
     */
    @GetMapping("/orders/{orderId}/summary")
    public Result<Map<String, Object>> orderSummary(@PathVariable Long orderId) {
        return Result.success(orderService.getOrderSummary(orderId));
    }

    /**
     * 用户买过的所有商品
     * GET /api/orders/user/1/products
     */
    @GetMapping("/orders/user/{userId}/products")
    public Result<List<Map<String, Object>>> userBoughtProducts(@PathVariable Long userId) {
        return Result.success(orderService.getUserBoughtProducts(userId));
    }
}
