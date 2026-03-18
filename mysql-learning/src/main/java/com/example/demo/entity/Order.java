package com.example.demo.entity;

import lombok.Data;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * ==================== 订单实体类 ====================
 *
 * 对应数据库表：orders
 *
 * 类比前端 TypeScript：
 * interface Order {
 *   id: number;
 *   userId: number;
 *   orderNo: string;
 *   totalAmount: number;
 *   status: number;  // 0待付款 1已付款 2已发货 3已完成
 *   createTime: Date;
 * }
 */
@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户ID（外键，关联 users 表）
     * 这里只存 ID，不做 JPA 关联映射，保持简单
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * 订单号（唯一）
     */
    @Column(name = "order_no", nullable = false, unique = true, length = 50)
    private String orderNo;

    /**
     * 订单总金额
     */
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    /**
     * 订单状态：0待付款 1已付款 2已发货 3已完成
     */
    @Column(nullable = false)
    private Integer status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time", updatable = false)
    private Date createTime;

    @PrePersist
    protected void onCreate() {
        createTime = new Date();
        if (status == null) {
            status = 0;  // 默认待付款
        }
    }
}
