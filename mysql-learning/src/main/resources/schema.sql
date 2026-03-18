-- =====================================================
-- MySQL 初始化脚本
-- =====================================================
-- 用途：项目启动时自动执行，用于初始化数据库
-- 类似前端：prisma migrate 或 typeorm migration
-- =====================================================

-- 如果不存在则创建数据库
CREATE DATABASE IF NOT EXISTS demo
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE demo;

-- =====================================================
-- 用户表
-- =====================================================
-- 注意：实际项目中表结构由 JPA 自动管理（ddl-auto=update）
-- 这里提供手动创建的 SQL 供学习参考
-- =====================================================

CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键 ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    nickname VARCHAR(50) COMMENT '昵称',
    email VARCHAR(100) COMMENT '邮箱',
    age INT COMMENT '年龄',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_username (username),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- =====================================================
-- 初始化数据
-- =====================================================
-- 插入一些测试数据，方便联调测试
-- =====================================================

-- 清空表数据（可选，谨慎使用）
-- TRUNCATE TABLE users;

-- 插入测试用户
INSERT INTO users (username, password, nickname, email, age) VALUES
('zhangsan', '123456', '张三', 'zhangsan@example.com', 25),
('lisi', '123456', '李四', 'lisi@example.com', 28),
('wangwu', '123456', '王五', 'wangwu@example.com', 30),
('zhaoliu', '123456', '赵六', 'zhaoliu@example.com', 22),
('sunqi', '123456', '孙七', 'sunqi@example.com', 35);

-- =====================================================
-- 查询验证
-- =====================================================

-- 查看所有用户
SELECT * FROM users;

-- 查看表结构
DESC users;

-- 查看建表语句
SHOW CREATE TABLE users;
