package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Product;
import org.apache.ibatis.annotations.Mapper;

/**
 * ==================== 商品 Mapper ====================
 *
 * 这是 MyBatis-Plus 最爽的地方！
 *
 * 只需要写一个空接口，继承 BaseMapper<Product>，
 * 就自动拥有了以下方法（不用写一行 SQL）：
 *
 * | 方法                    | 等价 SQL                              |
 * |------------------------|--------------------------------------|
 * | insert(product)        | INSERT INTO products ...              |
 * | deleteById(1L)         | DELETE FROM products WHERE id = 1     |
 * | updateById(product)    | UPDATE products SET ... WHERE id = ?  |
 * | selectById(1L)         | SELECT * FROM products WHERE id = 1   |
 * | selectList(null)       | SELECT * FROM products                |
 * | selectCount(null)      | SELECT COUNT(*) FROM products         |
 * | selectBatchIds([1,2])  | SELECT * FROM products WHERE id IN(1,2)|
 *
 * 对比之前 JdbcTemplate 要手写每条 SQL：
 * jdbcTemplate.query("SELECT id, name, price, stock... FROM products ORDER BY id", rowMapper)
 *
 * 类比前端：
 * JdbcTemplate = 手写 fetch('/api/products', { method: 'GET', headers: {...} })
 * BaseMapper   = 用 axios.get('/products') —— 框架帮你处理了底层细节
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {
    // 单表 CRUD 不用写任何代码！BaseMapper 已经全部提供了
    // 如果有复杂查询（多表 JOIN），可以在这里加自定义方法
}
