package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 商品 Controller
 *
 * ===== 接口总览 =====
 * GET    /api/products          查商品列表（走缓存）
 * GET    /api/products/{id}     查单个商品（走缓存）
 * POST   /api/products          新增商品（清缓存）
 * PUT    /api/products/{id}     更新商品（清缓存）
 * DELETE /api/products/{id}     删除商品（清缓存）
 * POST   /api/products/{id}/buy 购买商品（Redis 预扣库存 + 排行榜）
 * GET    /api/products/hot      热销排行榜（纯 Redis）
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    /** 查商品列表（第一次查 MySQL，后续查 Redis） */
    @GetMapping
    public Result<List<Product>> list() {
        return Result.success(productService.getAll());
    }

    /** 查单个商品（带缓存） */
    @GetMapping("/{id}")
    public Result<Product> getById(@PathVariable Long id) {
        Product product = productService.getById(id);
        if (product == null) {
            return Result.error(404, "商品不存在");
        }
        return Result.success(product);
    }

    /** 新增商品 */
    @PostMapping
    public Result<Product> create(@RequestBody Product product) {
        return Result.success("创建成功", productService.create(product));
    }

    /** 更新商品 */
    @PutMapping("/{id}")
    public Result<Product> update(@PathVariable Long id, @RequestBody Product product) {
        return Result.success("更新成功", productService.update(id, product));
    }

    /** 删除商品 */
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        productService.delete(id);
        return Result.success("删除成功", null);
    }

    /**
     * 购买商品
     * 示例：POST /api/products/1/buy?quantity=2
     */
    @PostMapping("/{id}/buy")
    public Result<Map<String, Object>> buy(@PathVariable Long id,
                                            @RequestParam(defaultValue = "1") int quantity) {
        return Result.success("购买成功", productService.buy(id, quantity));
    }

    /**
     * 热销排行榜（纯 Redis，不查 MySQL）
     * 示例：GET /api/products/hot?topN=10
     */
    @GetMapping("/hot")
    public Result<List<Map<String, Object>>> hotRank(@RequestParam(defaultValue = "10") int topN) {
        return Result.success(productService.getHotRank(topN));
    }
}
