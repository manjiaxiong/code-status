package com.example.demo.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * ==================== 订单明细实体类 ====================
 *
 * 对应数据库表：order_items
 * 记录每个订单里买了哪些商品、数量、价格
 *
 * 类比前端 TypeScript：
 * interface OrderItem {
 *   id: number;
 *   orderId: number;
 *   productId: number;
 *   quantity: number;
 *   price: number;  // 下单时的价格快照
 *   createTime: Date;
 * }
 */
@Data
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 订单ID（外键，关联 orders 表）
     */
    @Column(name = "order_id", nullable = false)
    private Long orderId;

    /**
     * 商品ID（外键，关联 products 表）
     */
    @Column(name = "product_id", nullable = false)
    private Long productId;

    /**
     * 购买数量
     */
    @Column(nullable = false)
    private Integer quantity;

    /**
     * 下单时的价格（价格快照）
     * 为什么不直接用商品表的价格？因为商品可能涨价降价，
     * 订单里要记录下单那一刻的价格
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time", updatable = false)
    private Date createTime;

    @PrePersist
    protected void onCreate() {
        createTime = new Date();
    }
}
