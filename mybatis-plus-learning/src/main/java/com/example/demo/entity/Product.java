package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * ==================== 商品实体类（MyBatis-Plus 版） ====================
 *
 * 对比 mysql-learning 的 JPA 版本：
 *
 * | JPA（旧）                          | MyBatis-Plus（新）              |
 * |-----------------------------------|-------------------------------|
 * | @Entity                           | 不需要                         |
 * | @Table(name = "products")         | @TableName("products")        |
 * | @Id + @GeneratedValue             | @TableId(type = IdType.AUTO)  |
 * | @Column(name = "create_time")     | @TableField("create_time")    |
 * | @PrePersist（自动填充）              | @TableField(fill = ...)       |
 *
 * 类比前端 TypeScript：
 * interface Product {
 *   id: number;
 *   name: string;
 *   price: number;
 *   stock: number;
 *   description?: string;
 *   createTime: Date;
 * }
 */
@Data
@TableName("products")  // 对应数据库表名（JPA 用的是 @Table）
public class Product {

    /**
     * 主键，自增
     * JPA 写法：@Id + @GeneratedValue(strategy = GenerationType.IDENTITY)
     * MP 写法：@TableId(type = IdType.AUTO) —— 更简洁
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private BigDecimal price;

    private Integer stock;

    private String description;

    /**
     * 驼峰命名会自动映射到 create_time 列
     * 不需要像 JPA 那样写 @Column(name = "create_time")
     * MyBatis-Plus 默认开启了 map-underscore-to-camel-case
     */
    private Date createTime;
}
