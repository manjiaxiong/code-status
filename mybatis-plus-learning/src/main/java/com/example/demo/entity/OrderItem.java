package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * ==================== 订单明细实体类（MyBatis-Plus 版） ====================
 *
 * ★★★ 和 mysql-learning 的区别 ★★★
 *
 * 和 Order 一样的道理：
 * JPA 版要写 @Entity, @Table, @Column, @Temporal, @PrePersist 等一堆注解
 * MP 版只要 @TableName + @TableId，其他字段裸写就行
 *
 * JPA 版本（mysql-learning）的代码量：67行
 * MP 版本（这个项目）的代码量：29行 —— 少了一半多！
 */
@Data
@TableName("order_items")
public class OrderItem {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long orderId;

    private Long productId;

    private Integer quantity;

    /** 下单时的价格快照 */
    private BigDecimal price;

    private Date createTime;
}
