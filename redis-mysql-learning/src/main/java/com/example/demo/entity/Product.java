package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品实体
 * 复用 demo 库的 products 表
 *
 * 注意：实现 Serializable 接口
 * 因为要存到 Redis，需要序列化
 * 类比前端：JSON.stringify() 需要对象可以被序列化
 */
@Data
@TableName("products")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private BigDecimal price;

    private Integer stock;

    private String description;

    private Date createTime;
}
