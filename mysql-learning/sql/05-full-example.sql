-- =====================================================
-- MySQL 完整 SQL 示例
-- =====================================================
-- 用途：手动执行创建表和插入测试数据
-- 执行方式：
--   1. MySQL Workbench: 打开文件 → 执行
--   2. 命令行：mysql -u root -p demo < 05-full-example.sql
--   3. Docker: docker exec -i mysql-local mysql -uroot -p123456 demo < 05-full-example.sql
-- =====================================================

USE demo;

-- =====================================================
-- 1. 删除旧表（如果存在）
-- =====================================================
DROP TABLE IF EXISTS users;

-- =====================================================
-- 2. 创建用户表
-- =====================================================
CREATE TABLE users (
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
-- 3. 插入测试数据
-- =====================================================
INSERT INTO users (username, password, nickname, email, age) VALUES
('zhangsan', '123456', '张三', 'zhangsan@example.com', 25),
('lisi', '123456', '李四', 'lisi@example.com', 28),
('wangwu', '123456', '王五', 'wangwu@example.com', 30),
('zhaoliu', '123456', '赵六', 'zhaoliu@example.com', 22),
('sunqi', '123456', '孙七', 'sunqi@example.com', 35),
('zhouba', '123456', '周八', 'zhouba@example.com', 27),
('wujiu', '123456', '王九', 'wujiu@example.com', 31),
('zhengyi', '123456', '郑十', 'zhengyi@example.com', 24);

-- =====================================================
-- 4. 验证数据
-- =====================================================
SELECT '=== 所有用户 ===' as '';
SELECT * FROM users ORDER BY id;

SELECT '' as '';
SELECT '=== 年龄大于 25 的用户 ===' as '';
SELECT username, nickname, age FROM users WHERE age > 25;

SELECT '' as '';
SELECT '=== 用户名包含"张"的用户 ===' as '';
SELECT username, nickname, email FROM users WHERE username LIKE '%张%';

SELECT '' as '';
SELECT '=== 用户总数 ===' as '';
SELECT COUNT(*) as total FROM users;

SELECT '' as '';
SELECT '=== 平均年龄 ===' as '';
SELECT AVG(age) as avg_age FROM users;

-- =====================================================
-- 5. CRUD 操作示例
-- =====================================================

-- 新增
-- INSERT INTO users (username, password, nickname, email, age) VALUES ('test', '123456', '测试用户', 'test@example.com', 20);

-- 更新
-- UPDATE users SET age = 26, nickname = '小张' WHERE username = 'zhangsan';

-- 删除
-- DELETE FROM users WHERE username = 'test';

-- 查询单个
-- SELECT * FROM users WHERE id = 1;

-- =====================================================
-- 6. 查看表结构
-- =====================================================
DESC users;
SHOW CREATE TABLE users\G
