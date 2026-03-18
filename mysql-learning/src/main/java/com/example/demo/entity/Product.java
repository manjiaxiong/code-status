package com.example.demo.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * ==================== 商品实体类 ====================
 *
 * 对应数据库表：products
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
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 商品名称
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * 价格，用 BigDecimal 表示精确小数
     * 不用 double 是因为浮点数有精度问题（前端也一样，0.1+0.2 !== 0.3）
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /**
     * 库存
     */
    @Column(nullable = false)
    private Integer stock;

    /**
     * 商品描述
     */
    @Column(length = 500)
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time", updatable = false)
    private Date createTime;

    @PrePersist
    protected void onCreate() {
        createTime = new Date();
    }
}
