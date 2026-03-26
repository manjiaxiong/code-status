package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 商品服务 —— Redis + MySQL 整合的核心
 *
 * ===== 缓存策略总结 =====
 *
 * 1. Cache Aside（旁路缓存）—— 本项目用的就是这个，最常用
 *    读：先查 Redis → 没有则查 MySQL → 写入 Redis
 *    写：先更新 MySQL → 再删除 Redis 缓存
 *
 * 2. 为什么是"删除缓存"而不是"更新缓存"？
 *    因为并发场景下，两个请求同时更新，可能导致缓存和数据库不一致
 *    删除更安全：下次读的时候自然会从 MySQL 重新加载
 *
 * 前端类比：
 *    Cache Aside = React Query 的 invalidateQueries()
 *    更新数据后不是手动 setQueryData，而是 invalidate 让它重新 fetch
 */
@Service
public class ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // ===== Redis Key 前缀 =====
    private static final String PRODUCT_KEY = "product:";           // 单个商品缓存
    private static final String PRODUCT_LIST_KEY = "product:list";  // 商品列表缓存
    private static final String HOT_RANK_KEY = "rank:hot";          // 热销排行榜
    private static final String STOCK_KEY = "stock:";               // 库存缓存

    // 缓存过期时间（秒）
    private static final long CACHE_TTL = 300;  // 5 分钟

    // ========================================================
    //  场景 1：查询缓存（Cache Aside 模式）
    // ========================================================

    /**
     * 查询单个商品（带缓存）
     *
     * 流程：
     * 1. 先查 Redis
     * 2. Redis 有 → 直接返回（缓存命中 cache hit）
     * 3. Redis 没有 → 查 MySQL → 写入 Redis → 返回（缓存未命中 cache miss）
     */
    public Product getById(Long id) {
        String key = PRODUCT_KEY + id;

        // 1. 查 Redis
        Object cached = redisTemplate.opsForValue().get(key);
        if (cached != null) {
            System.out.println(">>> 缓存命中！从 Redis 返回商品 " + id);
            return (Product) cached;
        }

        // 2. 缓存未命中，查 MySQL
        System.out.println(">>> 缓存未命中，从 MySQL 查询商品 " + id);
        Product product = productMapper.selectById(id);

        // 3. 写入 Redis（设置过期时间，防止缓存永不过期占内存）
        if (product != null) {
            redisTemplate.opsForValue().set(key, product, CACHE_TTL, TimeUnit.SECONDS);
        }

        return product;
    }

    /**
     * 查询商品列表（带缓存）
     */
    @SuppressWarnings("unchecked")
    public List<Product> getAll() {
        // 1. 查 Redis
        Object cached = redisTemplate.opsForValue().get(PRODUCT_LIST_KEY);
        if (cached != null) {
            System.out.println(">>> 缓存命中！从 Redis 返回商品列表");
            return (List<Product>) cached;
        }

        // 2. 查 MySQL
        System.out.println(">>> 缓存未命中，从 MySQL 查询商品列表");
        List<Product> products = productMapper.selectList(null);

        // 3. 写入 Redis
        if (products != null && !products.isEmpty()) {
            redisTemplate.opsForValue().set(PRODUCT_LIST_KEY, products, CACHE_TTL, TimeUnit.SECONDS);
        }

        return products;
    }

    // ========================================================
    //  场景 2：缓存一致性（写操作后清除缓存）
    // ========================================================

    /**
     * 新增商品
     * 新增后清除列表缓存，下次查列表会重新从 MySQL 加载
     */
    public Product create(Product product) {
        productMapper.insert(product);
        // 清除列表缓存
        redisTemplate.delete(PRODUCT_LIST_KEY);
        System.out.println(">>> 新增商品，已清除列表缓存");
        return product;
    }

    /**
     * 更新商品
     * 先更新 MySQL，再删除 Redis 缓存（Cache Aside 模式）
     */
    public Product update(Long id, Product product) {
        product.setId(id);
        productMapper.updateById(product);

        // 删除该商品的缓存 + 列表缓存
        redisTemplate.delete(PRODUCT_KEY + id);
        redisTemplate.delete(PRODUCT_LIST_KEY);
        System.out.println(">>> 更新商品 " + id + "，已清除相关缓存");

        return productMapper.selectById(id);
    }

    /**
     * 删除商品
     */
    public void delete(Long id) {
        productMapper.deleteById(id);

        // 删除缓存
        redisTemplate.delete(PRODUCT_KEY + id);
        redisTemplate.delete(PRODUCT_LIST_KEY);
        System.out.println(">>> 删除商品 " + id + "，已清除相关缓存");
    }

    // ========================================================
    //  场景 3：购买商品（Redis 预扣库存 + 热销排行榜）
    // ========================================================

    /**
     * 购买商品
     *
     * 流程：
     * 1. Redis DECR 预扣库存（原子操作，高并发安全）
     * 2. MySQL 扣减库存（真正落库）
     * 3. Redis ZSet ZINCRBY 更新热销排行榜
     *
     * 为什么先扣 Redis？
     *   MySQL 扣库存需要行锁，并发高时很慢
     *   Redis 是单线程 + 内存操作，DECR 是原子的，不会超卖
     *   类比前端：先用 optimistic update 立刻更新 UI，再等后端确认
     */
    public Map<String, Object> buy(Long productId, int quantity) {
        String stockKey = STOCK_KEY + productId;

        // 0. 初始化 Redis 库存（如果 Redis 里没有，从 MySQL 加载）
        if (!Boolean.TRUE.equals(redisTemplate.hasKey(stockKey))) {
            Product product = productMapper.selectById(productId);
            if (product == null) {
                throw new IllegalArgumentException("商品不存在");
            }
            redisTemplate.opsForValue().set(stockKey, product.getStock());
        }

        // 1. Redis 预扣库存
        Long remainStock = redisTemplate.opsForValue().decrement(stockKey, quantity);
        if (remainStock == null || remainStock < 0) {
            // 库存不足，回滚 Redis
            redisTemplate.opsForValue().increment(stockKey, quantity);
            throw new IllegalArgumentException("库存不足！剩余库存: " + (remainStock + quantity));
        }
        System.out.println(">>> Redis 预扣库存成功，剩余: " + remainStock);

        // 2. MySQL 扣减库存
        int rows = productMapper.deductStock(productId, quantity);
        if (rows == 0) {
            // MySQL 扣减失败，回滚 Redis
            redisTemplate.opsForValue().increment(stockKey, quantity);
            throw new RuntimeException("MySQL 库存扣减失败，已回滚 Redis");
        }
        System.out.println(">>> MySQL 库存扣减成功");

        // 3. 更新热销排行榜（ZSet，分数 = 累计销量）
        Product product = productMapper.selectById(productId);
        redisTemplate.opsForZSet().incrementScore(HOT_RANK_KEY, product.getName(), quantity);
        System.out.println(">>> 热销排行榜已更新：" + product.getName() + " +" + quantity);

        // 4. 清除该商品的缓存（库存变了）
        redisTemplate.delete(PRODUCT_KEY + productId);
        redisTemplate.delete(PRODUCT_LIST_KEY);

        // 返回结果
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("商品", product.getName());
        result.put("购买数量", quantity);
        result.put("剩余库存", remainStock);
        return result;
    }

    // ========================================================
    //  场景 4：热销排行榜（直接读 Redis，不查 MySQL）
    // ========================================================

    /**
     * 获取热销排行榜 Top N
     * 数据全部来自 Redis ZSet，不查 MySQL，速度极快
     */
    public List<Map<String, Object>> getHotRank(int topN) {
        Set<ZSetOperations.TypedTuple<Object>> tuples =
                redisTemplate.opsForZSet().reverseRangeWithScores(HOT_RANK_KEY, 0, topN - 1);

        List<Map<String, Object>> result = new ArrayList<>();
        if (tuples == null) return result;

        int ranking = 1;
        for (ZSetOperations.TypedTuple<Object> tuple : tuples) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("排名", ranking++);
            item.put("商品", tuple.getValue());
            item.put("销量", tuple.getScore().intValue());
            result.add(item);
        }
        return result;
    }
}
