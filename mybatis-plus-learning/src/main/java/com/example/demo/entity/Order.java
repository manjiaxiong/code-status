package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * ==================== 订单实体类（MyBatis-Plus 版） ====================
 *
 * ★★★ 和 mysql-learning 的区别 ★★★
 *
 * mysql-learning（JPA 版）：
 *   @Entity                                          ← MP 不需要
 *   @Table(name = "orders")                          ← MP 用 @TableName
 *   @Id @GeneratedValue(strategy=GenerationType.IDENTITY)  ← MP 用 @TableId
 *   @Column(name = "user_id", nullable = false)      ← MP 自动驼峰映射，不用写
 *   @Column(name = "order_no", nullable = false, unique = true, length = 50)  ← 不用写
 *   @Temporal(TemporalType.TIMESTAMP)                ← 不用写
 *   @PrePersist protected void onCreate() { ... }    ← MP 不用这个
 *
 * mybatis-plus 版：
 *   @TableName("orders")                             ← 指定表名
 *   @TableId(type = IdType.AUTO)                     ← 指定主键自增
 *   其他字段什么注解都不用加！驼峰自动映射
 *
 * 总结：JPA 实体类注解多（要告诉框架表结构），MP 实体类注解少（只管映射）
 * 因为 JPA 能自动建表（ddl-auto），所以需要更多元数据
 * MP 不管建表，只管映射，所以注解少
 */
@Data
@TableName("orders")
public class Order {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String orderNo;

    private BigDecimal totalAmount;

    /** 0待付款 1已付款 2已发货 3已完成 */
    private Integer status;

    private Date createTime;
}
