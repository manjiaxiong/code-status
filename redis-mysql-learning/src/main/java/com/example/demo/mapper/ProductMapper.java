package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 商品 Mapper
 * 继承 BaseMapper 自动获得单表 CRUD
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {

    /**
     * 扣减库存（MySQL 层面，防超卖用 stock >= quantity 条件）
     * 对应 SQL：UPDATE products SET stock = stock - #{quantity} WHERE id = #{id} AND stock >= #{quantity}
     *
     * 返回值：受影响的行数，0 表示库存不足
     */
    @Update("UPDATE products SET stock = stock - #{quantity} WHERE id = #{id} AND stock >= #{quantity}")
    int deductStock(@Param("id") Long id, @Param("quantity") int quantity);
}
