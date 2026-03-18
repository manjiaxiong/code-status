package com.example.demo.entity;

import lombok.Data;
import javax.persistence.*;
import java.util.Date;

/**
 * ==================== 用户实体类 ====================
 *
 * 作用：定义数据库表结构
 * 类似前端：TypeORM 的 Entity、Prisma 的 Model
 *
 * 数据库表：
 *   CREATE TABLE users (
 *       id BIGINT PRIMARY KEY AUTO_INCREMENT,
 *       username VARCHAR(255) NOT NULL,
 *       email VARCHAR(255),
 *       age INT,
 *       create_time DATETIME,
 *       update_time DATETIME
 *   );
 */
@Data  // Lombok 注解：自动生成 Getter、Setter、toString、equals、hashCode
@Entity  // 标记为实体类，JPA 会管理
@Table(name = "users")  // 指定表名（不写默认用类名小写）
public class User {

    /**
     * 主键 ID
     * @Id：标记为主键
     * @GeneratedValue：自动生成策略
     * GenerationType.IDENTITY：数据库自增（MySQL AUTO_INCREMENT）
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户名
     * @Column：配置列属性
     * nullable = false：不允许为空
     * unique = true：唯一约束
     * length = 50：字段长度
     */
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    /**
     * 邮箱
     */
    @Column(length = 100)
    private String email;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 密码（用于登录认证）
     */
    @Column(nullable = false, length = 255)
    private String password;

    /**
     * 昵称
     */
    @Column(length = 50)
    private String nickname;

    /**
     * 创建时间
     * @Temporal：指定日期类型
     * TemporalType.TIMESTAMP：日期 + 时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time", updatable = false)  // updatable=false 表示更新时不修改
    private Date createTime;

    /**
     * 更新时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 保存前自动设置创建时间
     * @PrePersist：实体保存前自动调用
     */
    @PrePersist
    protected void onCreate() {
        createTime = new Date();
        updateTime = new Date();
    }

    /**
     * 更新前自动设置更新时间
     * @PreUpdate：实体更新前自动调用
     */
    @PreUpdate
    protected void onUpdate() {
        updateTime = new Date();
    }
}