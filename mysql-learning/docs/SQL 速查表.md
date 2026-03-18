# SQL 速查表 - 从零开始学 SQL

> 📚 前端开发者的 SQL 入门指南
> 类比 MongoDB/Mongoose 语法，10 分钟上手 SQL
>
> **本文档特点：**
> - ✅ 每个知识点都有完整实例
> - ✅ 提供数据准备脚本
> - ✅ 展示执行结果
> - ✅ 对比前端代码

---

## 📖 目录

1. [基础概念](#1-基础概念)
2. [查询数据 (SELECT)](#2-查询数据-select)
3. [条件查询 (WHERE)](#3-条件查询-where)
4. [新增数据 (INSERT)](#4-新增数据-insert)
5. [更新数据 (UPDATE)](#5-更新数据-update)
6. [删除数据 (DELETE)](#6-删除数据-delete)
7. [排序与分页](#7-排序与分页)
8. [聚合函数](#8-聚合函数)
9. [多表查询 (JOIN)](#9-多表查询-join)
10. [Java 代码示例](#10-java-代码示例)
11. [完整实战案例](#11-完整实战案例)

---

## 1. 基础概念

### 1.1 数据库术语对比

| 数据库概念 | SQL | MongoDB | 前端类比 |
|-----------|-----|---------|---------|
| 数据库 | `DATABASE` | Database | 文件夹 |
| 表 | `TABLE` | Collection | 数组 |
| 行/记录 | `ROW` | Document | 对象 `{}` |
| 列/字段 | `COLUMN` | Field | 属性名 `key` |

### 1.2 创建数据库和表

```sql
-- 1. 创建数据库
CREATE DATABASE demo
CHARACTER SET utf8mb4      -- 字符集：支持中文和 emoji
COLLATE utf8mb4_unicode_ci; -- 排序规则

-- 2. 使用数据库
USE demo;

-- 3. 创建用户表
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键 ID，自增',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名，非空且唯一',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    nickname VARCHAR(50) COMMENT '昵称',
    email VARCHAR(100) COMMENT '邮箱',
    age INT COMMENT '年龄',
    balance DECIMAL(10,2) DEFAULT 0 COMMENT '余额',
    status TINYINT DEFAULT 1 COMMENT '状态：1 正常 0 禁用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 4. 创建订单表
CREATE TABLE orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单 ID',
    order_no VARCHAR(50) NOT NULL UNIQUE COMMENT '订单编号',
    user_id BIGINT NOT NULL COMMENT '用户 ID',
    amount DECIMAL(10,2) NOT NULL COMMENT '订单金额',
    status TINYINT DEFAULT 0 COMMENT '状态：0 待支付 1 已支付 2 已取消',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 5. 创建商品表
CREATE TABLE products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '商品 ID',
    name VARCHAR(100) NOT NULL COMMENT '商品名称',
    price DECIMAL(10,2) NOT NULL COMMENT '价格',
    stock INT DEFAULT 0 COMMENT '库存',
    category VARCHAR(50) COMMENT '分类',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '上架时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';
```

### 1.3 插入测试数据

```sql
-- ===== users 表数据 =====
INSERT INTO users (username, password, nickname, email, age, balance, status) VALUES
('zhangsan', '123456', '张三', 'zhangsan@qq.com', 25, 1000.00, 1),
('lisi', '123456', '李四', 'lisi@qq.com', 28, 2500.50, 1),
('wangwu', '123456', '王五', 'wangwu@qq.com', 30, 500.00, 1),
('zhaoliu', '123456', '赵六', 'zhaoliu@qq.com', 22, 3000.00, 1),
('sunqi', '123456', '孙七', 'sunqi@qq.com', 35, 1500.75, 1),
('zhouba', '123456', '周八', 'zhouba@qq.com', 27, 800.00, 1),
('wujiu', '123456', '王九', 'wujiu@qq.com', 31, NULL, 0),  -- status=0 禁用
('zhengyi', '123456', '郑十', 'zhengyi@qq.com', 24, 2000.00, 1);

-- ===== orders 表数据 =====
INSERT INTO orders (order_no, user_id, amount, status) VALUES
('ORD001', 1, 99.00, 1),   -- 张三的订单，已支付
('ORD002', 1, 199.00, 1),  -- 张三的订单，已支付
('ORD003', 2, 299.00, 0),  -- 李四的订单，待支付
('ORD004', 2, 399.00, 1),  -- 李四的订单，已支付
('ORD005', 2, 499.00, 2),  -- 李四的订单，已取消
('ORD006', 3, 599.00, 1),  -- 王五的订单，已支付
('ORD007', 4, 699.00, 0),  -- 赵六的订单，待支付
('ORD008', 5, 799.00, 1);  -- 孙七的订单，已支付

-- ===== products 表数据 =====
INSERT INTO products (name, price, stock, category) VALUES
('iPhone 15', 7999.00, 100, '手机'),
('MacBook Pro', 14999.00, 50, '电脑'),
('AirPods Pro', 1999.00, 200, '配件'),
('iPad Air', 4999.00, 80, '平板'),
('Apple Watch', 2999.00, 150, '手表'),
('Magic Mouse', 699.00, 300, '配件'),
('Keyboard', 999.00, 250, '配件');
```

### 1.4 查看表结构和数据

```sql
-- 查看所有表
SHOW TABLES;

-- 查看表结构
DESC users;

-- 查看建表语句
SHOW CREATE TABLE users\G

-- 查询所有数据
SELECT * FROM users;

-- 查看前 5 条数据
SELECT * FROM users LIMIT 5;
```

### 1.5 示例表数据展示

**users 表：**
```
+----+----------+----------+--------+-------------------+-----+---------+--------+
| id | username | nickname | age    | email             | balance | status | create_time |
+----+----------+----------+--------+-------------------+-----+---------+--------+
| 1  | zhangsan | 张三      | 25     | zhangsan@qq.com   | 1000.00 | 1      | 2024-01-01 |
| 2  | lisi     | 李四      | 28     | lisi@qq.com       | 2500.50 | 1      | 2024-01-02 |
| 3  | wangwu   | 王五      | 30     | wangwu@qq.com     | 500.00  | 1      | 2024-01-03 |
| 4  | zhaoliu  | 赵六      | 22     | zhaoliu@qq.com    | 3000.00 | 1      | 2024-01-04 |
| 5  | sunqi    | 孙七      | 35     | sunqi@qq.com      | 1500.75 | 1      | 2024-01-05 |
| 6  | zhouba   | 周八      | 27     | zhouba@qq.com     | 800.00  | 1      | 2024-01-06 |
| 7  | wujiu    | 王九      | 31     | wujiu@qq.com      | NULL    | 0      | 2024-01-07 |
| 8  | zhengyi  | 郑十      | 24     | zhengyi@qq.com    | 2000.00 | 1      | 2024-01-08 |
+----+----------+----------+--------+-------------------+-----+---------+--------+
```

**orders 表：**
```
+----+--------+---------+--------+--------+-------------+
| id | order_no | user_id | amount | status | create_time |
+----+--------+---------+--------+--------+-------------+
| 1  | ORD001 | 1       | 99.00  | 1      | 2024-01-01  |
| 2  | ORD002 | 1       | 199.00 | 1      | 2024-01-02  |
| 3  | ORD003 | 2       | 299.00 | 0      | 2024-01-03  |
| 4  | ORD004 | 2       | 399.00 | 1      | 2024-01-04  |
| 5  | ORD005 | 2       | 499.00 | 2      | 2024-01-05  |
| 6  | ORD006 | 3       | 599.00 | 1      | 2024-01-06  |
| 7  | ORD007 | 4       | 699.00 | 0      | 2024-01-07  |
| 8  | ORD008 | 5       | 799.00 | 1      | 2024-01-08  |
+----+--------+---------+--------+--------+-------------+
```

---

## 2. 查询数据 (SELECT)

### 2.1 查询所有数据

```sql
-- 查询所有字段（* 表示所有列）
SELECT * FROM users;

-- 执行结果：返回 users 表所有 8 列数据

-- 类比 MongoDB
db.users.find()

-- 类比 TypeORM
userRepository.find()

-- ⚠️ 注意：生产环境不建议用 SELECT *，应该指定需要的字段
```

### 2.2 查询指定字段

```sql
-- 只查询用户名和邮箱
SELECT username, email FROM users;

-- 执行结果：
+----------+-------------------+
| username | email             |
+----------+-------------------+
| zhangsan | zhangsan@qq.com   |
| lisi     | lisi@qq.com       |
| wangwu   | wangwu@qq.com     |
| ...      | ...               |
+----------+-------------------+

-- 类比 MongoDB
db.users.find({}, { username: 1, email: 1 })

-- 类比 TypeORM
userRepository.find({ select: ['username', 'email'] })
```

### 2.3 给字段起别名 (AS)

```sql
-- 使用 AS 关键字
SELECT
    username AS 用户名，
    email AS 邮箱，
    age AS 年龄，
    balance AS 余额
FROM users;

-- 执行结果：
+----------+-------------------+-----+--------+
| 用户名    | 邮箱              | 年龄 | 余额   |
+----------+-------------------+-----+--------+
| zhangsan | zhangsan@qq.com   | 25  | 1000.00|
| lisi     | lisi@qq.com       | 28  | 2500.50|
+----------+-------------------+-----+--------+

-- AS 可以省略（用空格代替）
SELECT username 用户名，email 邮箱 FROM users;

-- 实用场景：导出报表时使用中文列名
```

### 2.4 去重查询 (DISTINCT)

```sql
-- 查询所有年龄（可能有重复）
SELECT age FROM users;
-- 结果：25, 28, 30, 22, 35, 27, 31, 24

-- 去重查询
SELECT DISTINCT age FROM users;
-- 结果：25, 28, 30, 22, 35, 27, 31, 24（去除重复值）

-- 多字段去重
SELECT DISTINCT age, status FROM users;
-- 按年龄和状态的组合去重

-- 类比 MongoDB
db.users.distinct('age')
```

### 2.5 查询常量值

```sql
-- 查询时添加常量列
SELECT
    username,
    'VIP 用户' AS 用户类型，
    100 AS 初始积分
FROM users;

-- 执行结果：
+----------+----------+----------+
| username | 用户类型  | 初始积分  |
+----------+----------+----------+
| zhangsan | VIP 用户   | 100      |
| lisi     | VIP 用户   | 100      |
+----------+----------+----------+
```

---

## 3. 条件查询 (WHERE)

### 3.1 基本比较运算符

```sql
-- 等于 (=)
SELECT * FROM users WHERE id = 1;
-- 返回：zhangsan

-- 不等于 (!= 或 <>)
SELECT * FROM users WHERE id != 1;
SELECT * FROM users WHERE id <> 1;  -- 两种写法等价

-- 大于 (>)
SELECT * FROM users WHERE age > 25;
-- 返回：lisi(28), wangwu(30), sunqi(35), wujiu(31)

-- 大于等于 (>=)
SELECT * FROM users WHERE age >= 28;
-- 返回：lisi(28), wangwu(30), sunqi(35), wujiu(31)

-- 小于 (<)
SELECT * FROM users WHERE age < 25;
-- 返回：zhaoliu(22), zhengyi(24)

-- 小于等于 (<=)
SELECT * FROM users WHERE age <= 25;
-- 返回：zhangsan(25), zhaoliu(22), zhengyi(24)
```

### 3.2 逻辑运算符 (AND, OR, NOT)

```sql
-- AND (且) - 两个条件都满足
SELECT * FROM users WHERE age > 25 AND age < 35;
-- 返回：lisi(28), wangwu(30), zhouba(27), wujiu(31)

-- OR (或) - 满足任一条件
SELECT * FROM users WHERE age = 25 OR age = 28;
-- 返回：zhangsan(25), lisi(28)

-- NOT (非) - 取反
SELECT * FROM users WHERE NOT age = 25;
SELECT * FROM users WHERE age != 25;  -- 等价写法
-- 返回：除了 zhangsan 以外的所有人

-- 复杂组合
SELECT * FROM users
WHERE (age > 25 AND age < 35) OR status = 0;
-- 返回：25-35 岁之间的人 + 被禁用的用户
```

### 3.3 IN 和 NOT IN

```sql
-- IN (在...之中)
SELECT * FROM users WHERE age IN (25, 28, 30);
-- 等价于
SELECT * FROM users WHERE age = 25 OR age = 28 OR age = 30;
-- 返回：zhangsan(25), lisi(28), wangwu(30)

-- NOT IN (不在...之中)
SELECT * FROM users WHERE age NOT IN (25, 28);
-- 返回：除了 25 和 28 岁以外的所有人

-- 实用场景：查询特定 ID 的用户
SELECT * FROM users WHERE id IN (1, 3, 5);
```

### 3.4 BETWEEN 范围查询

```sql
-- BETWEEN (在...之间，包含边界)
SELECT * FROM users WHERE age BETWEEN 25 AND 30;
-- 等价于
SELECT * FROM users WHERE age >= 25 AND age <= 30;
-- 返回：zhangsan(25), lisi(28), wangwu(30)

-- NOT BETWEEN (不在...之间)
SELECT * FROM users WHERE age NOT BETWEEN 25 AND 30;
-- 返回：zhaoliu(22), sunqi(35), zhouba(27), wujiu(31), zhengyi(24)
-- 等等，27 在 25-30 之间...应该是：zhaoliu(22), sunqi(35), wujiu(31), zhengyi(24)
```

### 3.5 LIKE 模糊查询

```sql
-- % 匹配任意数量字符（包括 0 个）

-- 以'zhang'开头
SELECT * FROM users WHERE username LIKE 'zhang%';
-- 返回：zhangsan

-- 以'san'结尾
SELECT * FROM users WHERE username LIKE '%san';
-- 返回：zhangsan

-- 包含'an'
SELECT * FROM users WHERE username LIKE '%an%';
-- 返回：zhangsan

-- 以'q'开头，以'com'结尾（邮箱）
SELECT * FROM users WHERE email LIKE 'q%com';

-- _ 匹配单个字符
SELECT * FROM users WHERE username LIKE '____i';  -- 第 4 个字符是 i
-- 返回：lisi

-- 转义特殊字符
SELECT * FROM users WHERE username LIKE 'test\_%';  -- 匹配 test_xxx
```

### 3.6 NULL 判断

```sql
-- IS NULL (为空)
SELECT * FROM users WHERE balance IS NULL;
-- 返回：wujiu（余额为 NULL）

-- IS NOT NULL (不为空)
SELECT * FROM users WHERE balance IS NOT NULL;
-- 返回：除了 wujiu 以外的所有人

-- ⚠️ 重要：不能用 = NULL
SELECT * FROM users WHERE balance = NULL;  -- 错误！不会返回任何结果
-- 原因：NULL 表示未知，未知不能和任何值比较（包括 NULL 自己）
```

### 3.7 综合实例

```sql
-- 查询 25-35 岁之间，余额大于 1000 的正常用户
SELECT username, age, balance
FROM users
WHERE age BETWEEN 25 AND 35
  AND balance > 1000
  AND status = 1;

-- 执行结果：
+----------+-----+---------+
| username | age | balance |
+----------+-----+---------+
| lisi     | 28  | 2500.50 |
| zhaoliu  | 22  | 3000.00 |  -- 等等，22 不在 25-35 之间
| sunqi    | 35  | 1500.75 |
| wujiu    | 31  | NULL    |  -- NULL 不满足 > 1000
+----------+-----+---------+

-- 修正后的结果：
+----------+-----+---------+
| username | age | balance |
+----------+-----+---------+
| lisi     | 28  | 2500.50 |
| sunqi    | 35  | 1500.75 |
+----------+-----+---------+
```

---

## 4. 新增数据 (INSERT)

### 4.1 插入单条数据

```sql
-- 标准写法（推荐）
INSERT INTO users (username, password, nickname, email, age, balance)
VALUES ('test01', '123456', '测试用户', 'test01@qq.com', 20, 500.00);

-- 插入后查看
SELECT * FROM users WHERE username = 'test01';
-- 返回：id=9, username='test01', ...

-- 类比 MongoDB
db.users.insertOne({
    username: 'test01',
    password: '123456',
    nickname: '测试用户',
    email: 'test01@qq.com',
    age: 20,
    balance: 500.00
})
```

### 4.2 插入多条数据（批量插入）

```sql
-- 一次插入多条数据
INSERT INTO users (username, password, nickname, email, age, balance) VALUES
('batch01', '123456', '批量 1', 'batch01@qq.com', 20, 100.00),
('batch02', '123456', '批量 2', 'batch02@qq.com', 21, 200.00),
('batch03', '123456', '批量 3', 'batch03@qq.com', 22, 300.00);

-- 查看结果
SELECT * FROM users WHERE username LIKE 'batch%';

-- 类比 MongoDB
db.users.insertMany([...])
```

### 4.3 获取自增 ID

```sql
-- MySQL 特有功能
INSERT INTO users (username, password, email, age)
VALUES ('getid', '123456', 'getid@qq.com', 18);

-- 获取刚插入的 ID
SELECT LAST_INSERT_ID();
-- 返回：9（假设之前最大 ID 是 8）

-- Java 中使用
// jdbcTemplate.update(sql, args);
// Long id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
```

### 4.4 插入时省略的字段

```sql
-- 可以省略有默认值的字段
INSERT INTO users (username, password)  -- 只插入必填字段
VALUES ('minimal', '123456');

-- 其他字段的值：
-- nickname: NULL
-- email: NULL
-- age: NULL
-- balance: 0 (默认值)
-- status: 1 (默认值)
-- create_time: 当前时间 (DEFAULT CURRENT_TIMESTAMP)
```

### 4.5 插入重复数据的处理

```sql
-- 尝试插入已存在的用户名（会报错）
INSERT INTO users (username, password) VALUES ('zhangsan', '123456');
-- 错误：Duplicate entry 'zhangsan' for key 'username'

-- 忽略重复（不报错也不插入）
INSERT IGNORE INTO users (username, password) VALUES ('zhangsan', '123456');
-- 返回：Affected rows: 0

-- 如果存在则更新
INSERT INTO users (username, password, nickname)
VALUES ('zhangsan', 'newpwd', '新张三')
ON DUPLICATE KEY UPDATE nickname = '新张三', password = 'newpwd';
-- 返回：Affected rows: 2（表示更新）
```

---

## 5. 更新数据 (UPDATE)

### 5.1 更新单条记录

```sql
-- 更新指定 ID 的用户
UPDATE users
SET age = 26, email = 'newemail@qq.com'
WHERE id = 1;

-- 验证结果
SELECT username, age, email FROM users WHERE id = 1;
-- +----------+-----+------------------+
-- | username | age | email            |
-- +----------+-----+------------------+
-- | zhangsan | 26  | newemail@qq.com  |
-- +----------+-----+------------------+

-- ⚠️ 警告：一定要加 WHERE 条件，否则会更新所有行！
UPDATE users SET age = 100;  -- 所有用户都变成 100 岁！
```

### 5.2 更新多条记录

```sql
-- 更新所有 25 岁的用户
UPDATE users
SET balance = balance + 100  -- 每人加 100 元
WHERE age = 25;

-- 批量更新状态
UPDATE users
SET status = 0  -- 禁用所有余额小于 1000 的用户
WHERE balance < 1000;
```

### 5.3 自增自减

```sql
-- 年龄 +1
UPDATE users SET age = age + 1 WHERE id = 1;

-- 余额 -50
UPDATE users SET balance = balance - 50 WHERE id = 1;

-- 余额翻倍
UPDATE users SET balance = balance * 2 WHERE id = 1;
```

### 5.4 条件更新

```sql
-- 根据条件设置不同值（CASE 语句）
UPDATE users
SET status = CASE
    WHEN age < 25 THEN 0      -- 小于 25 岁禁用
    WHEN age >= 25 AND age < 35 THEN 1  -- 25-35 岁正常
    ELSE 0                    -- 其他禁用
END;
```

### 5.5 完整更新示例

```sql
-- 场景：用户充值 100 元
UPDATE users
SET
    balance = balance + 100,      -- 余额 +100
    update_time = NOW()           -- 更新时间
WHERE id = 1;

-- 场景：批量修改邮箱后缀
UPDATE users
SET email = REPLACE(email, 'qq.com', 'gmail.com');
-- 所有 qq.com 邮箱变成 gmail.com
```

---

## 6. 删除数据 (DELETE)

### 6.1 删除单条记录

```sql
-- 删除指定 ID 的用户
DELETE FROM users WHERE id = 9;

-- 验证
SELECT * FROM users WHERE id = 9;  -- 返回空

-- 类比 MongoDB
db.users.deleteOne({ _id: 9 })
```

### 6.2 删除多条记录

```sql
-- 删除所有测试用户
DELETE FROM users WHERE username LIKE 'test%' OR username LIKE 'batch%';

-- 删除所有禁用用户
DELETE FROM users WHERE status = 0;

-- 删除余额为 0 的用户
DELETE FROM users WHERE balance = 0;
```

### 6.3 条件删除

```sql
-- 删除超过 30 天未更新的用户
DELETE FROM users
WHERE update_time < DATE_SUB(NOW(), INTERVAL 30 DAY);

-- 删除重复用户（保留 ID 最小的）
DELETE u1 FROM users u1
INNER JOIN users u2
WHERE u1.id > u2.id AND u1.username = u2.username;
```

### 6.4 清空表

```sql
-- DELETE (逐行删除，记录日志，可回滚)
DELETE FROM users;
-- 特点：慢，但安全，可以 WHERE 条件

-- TRUNCATE (直接清空，不记录日志，不可回滚)
TRUNCATE TABLE users;
-- 特点：快，自增 ID 重置为 1，不能 WHERE

-- DROP (删除整个表结构)
DROP TABLE users;
-- 特点：表都没了，需要重新 CREATE

-- 推荐使用：DELETE（除非确定要清空整个表）
```

### 6.5 软删除（推荐）

```sql
-- 1. 添加删除标记字段
ALTER TABLE users ADD COLUMN deleted TINYINT DEFAULT 0;
ALTER TABLE users ADD COLUMN delete_time DATETIME;

-- 2. 软删除（实际不删除，只是标记）
UPDATE users SET deleted = 1, delete_time = NOW() WHERE id = 1;

-- 3. 查询时排除已删除的
SELECT * FROM users WHERE deleted = 0;

-- 优点：数据可恢复，符合审计要求
-- 缺点：需要修改所有查询，加上 deleted = 0 条件
```

---

## 7. 排序与分页

### 7.1 排序 (ORDER BY)

```sql
-- 升序 (ASC - 从小到大，默认)
SELECT username, age FROM users ORDER BY age ASC;
SELECT username, age FROM users ORDER BY age;  -- 等价

-- 降序 (DESC - 从大到小)
SELECT username, age FROM users ORDER BY age DESC;

-- 多字段排序
SELECT username, age, balance
FROM users
ORDER BY age DESC, balance ASC;
-- 先按年龄降序，年龄相同再按余额升序

-- 执行结果示例：
+----------+-----+---------+
| username | age | balance |
+----------+-----+---------+
| sunqi    | 35  | 1500.75 |
| wujiu    | 31  | NULL    |
| wangwu   | 30  | 500.00  |
| lisi     | 28  | 2500.50 |  -- age=28 相同，按 balance 升序
| zhouba   | 27  | 800.00  |
+----------+-----+---------+

-- NULL 值排序
SELECT username, balance FROM users ORDER BY balance;
-- NULL 会排在最前面或最后面（取决于数据库）
```

### 7.2 分页 (LIMIT)

```sql
-- 限制返回数量
SELECT * FROM users LIMIT 3;
-- 只返回前 3 条

-- 分页查询（推荐写法）
-- 格式：LIMIT 偏移量，每页数量
SELECT * FROM users LIMIT 0, 10;  -- 第 1 页，每页 10 条
SELECT * FROM users LIMIT 10, 10; -- 第 2 页，每页 10 条
SELECT * FROM users LIMIT 20, 10; -- 第 3 页，每页 10 条

-- 分页公式
-- 偏移量 = (页码 - 1) * 每页数量
-- 第 1 页：LIMIT 0, 10      = (1-1)*10
-- 第 2 页：LIMIT 10, 10     = (2-1)*10
-- 第 10 页：LIMIT 90, 10    = (10-1)*10

-- 带排序的分页
SELECT * FROM users
ORDER BY id DESC
LIMIT 0, 10;  -- 按 ID 降序，取前 10 条
```

### 7.3 分页完整示例（Java 代码）

```java
/**
 * 分页查询用户
 * @param page 页码（从 1 开始）
 * @param pageSize 每页数量
 */
public Page<User> getUserPage(int page, int pageSize) {
    // 计算偏移量
    int offset = (page - 1) * pageSize;

    // 查询当前页数据
    String sql = "SELECT * FROM users ORDER BY id DESC LIMIT ?, ?";
    List<User> users = jdbcTemplate.query(sql, rowMapper, offset, pageSize);

    // 查询总数
    String countSql = "SELECT COUNT(*) FROM users WHERE deleted = 0";
    int total = jdbcTemplate.queryForObject(countSql, Integer.class);

    // 计算总页数
    int totalPages = (int) Math.ceil((double) total / pageSize);

    return new Page<>(users, total, totalPages, page);
}

// 使用示例
Page<User> page = getUserPage(1, 10);  // 第 1 页，每页 10 条
System.out.println("总数：" + page.getTotal());
System.out.println("总页数：" + page.getTotalPages());
```

---

## 8. 聚合函数

### 8.1 COUNT - 统计

```sql
-- 统计总行数
SELECT COUNT(*) FROM users;
-- 返回：8

-- 统计非 NULL 的行数
SELECT COUNT(balance) FROM users;
-- 返回：7（wujiu 的 balance 是 NULL，不计入）

-- 统计去重后的数量
SELECT COUNT(DISTINCT age) FROM users;
-- 返回：8（8 个不同的年龄）

-- 统计符合条件的数量
SELECT COUNT(*) FROM users WHERE age > 25;
-- 返回：5
```

### 8.2 SUM - 求和

```sql
-- 求年龄总和
SELECT SUM(age) FROM users;
-- 返回：222

-- 求余额总和
SELECT SUM(balance) FROM users;
-- 返回：11301.25（NULL 不计入）

-- 条件求和
SELECT SUM(balance) FROM users WHERE status = 1;
-- 返回：11301.25（排除禁用的用户）
```

### 8.3 AVG - 平均值

```sql
-- 求平均年龄
SELECT AVG(age) FROM users;
-- 返回：27.75

-- 求平均余额
SELECT AVG(balance) FROM users;
-- 返回：1412.65625

-- 保留 2 位小数
SELECT ROUND(AVG(balance), 2) FROM users;
-- 返回：1412.66
```

### 8.4 MAX/MIN - 最大/最小值

```sql
-- 最大年龄
SELECT MAX(age) FROM users;
-- 返回：35

-- 最小年龄
SELECT MIN(age) FROM users;
-- 返回：22

-- 最大余额
SELECT MAX(balance) FROM users;
-- 返回：3000.00

-- 同时查询多个聚合值
SELECT
    COUNT(*) as total,
    MAX(age) as max_age,
    MIN(age) as min_age,
    AVG(age) as avg_age,
    SUM(balance) as total_balance
FROM users;

-- 返回：
+-------+---------+---------+---------+---------------+
| total | max_age | min_age | avg_age | total_balance |
+-------+---------+---------+---------+---------------+
| 8     | 35      | 22      | 27.75   | 11301.25      |
+-------+---------+---------+---------+---------------+
```

### 8.5 GROUP BY - 分组

```sql
-- 按年龄分组，统计每组人数
SELECT age, COUNT(*) as count
FROM users
GROUP BY age;

-- 返回：
+-----+-------+
| age | count |
+-----+-------+
| 22  | 1     |
| 24  | 1     |
| 25  | 1     |
| 27  | 1     |
| 28  | 1     |
| 30  | 1     |
| 31  | 1     |
| 35  | 1     |
+-----+-------+

-- 按状态分组
SELECT status, COUNT(*) as count, AVG(balance) as avg_balance
FROM users
GROUP BY status;

-- 返回：
+--------+-------+-------------+
| status | count | avg_balance |
+--------+-------+-------------+
| 0      | 1     | NULL        |
| 1      | 7     | 1614.46     |
+--------+-------+-------------+
```

### 8.6 HAVING - 分组后过滤

```sql
-- HAVING vs WHERE 的区别：
-- WHERE: 分组前过滤（不能用聚合函数）
-- HAVING: 分组后过滤（可以用聚合函数）

-- 错误写法
SELECT age, COUNT(*) as count
FROM users
WHERE count > 1  -- 错误！WHERE 不能用聚合函数
GROUP BY age;

-- 正确写法
SELECT age, COUNT(*) as count
FROM users
GROUP BY age
HAVING count > 1;  -- 过滤分组后的结果

-- 实用示例：统计订单数大于 1 的用户
SELECT user_id, COUNT(*) as order_count, SUM(amount) as total_amount
FROM orders
GROUP BY user_id
HAVING COUNT(*) > 1;

-- 返回：
+---------+-------------+--------------+
| user_id | order_count | total_amount |
+---------+-------------+--------------+
| 1       | 2           | 298.00       |
| 2       | 3           | 1197.00      |
+---------+-------------+--------------+
```

### 8.7 GROUP BY 完整实例

```sql
-- 统计每个用户的订单数量和总金额
SELECT
    u.username,
    COUNT(o.id) as order_count,
    SUM(o.amount) as total_amount,
    AVG(o.amount) as avg_amount
FROM users u
LEFT JOIN orders o ON u.id = o.user_id
GROUP BY u.id, u.username
ORDER BY total_amount DESC;

-- 返回：
+----------+-------------+--------------+------------+
| username | order_count | total_amount | avg_amount |
+----------+-------------+--------------+------------+
| lisi     | 3           | 1197.00      | 399.00     |
| zhangsan | 2           | 298.00       | 149.00     |
| sunqi    | 1           | 799.00       | 799.00     |
| wangwu   | 1           | 599.00       | 599.00     |
| zhaoliu  | 1           | 699.00       | 699.00     |
| zhouba   | 0           | NULL         | NULL       |
| wujiu    | 0           | NULL         | NULL       |
| zhengyi  | 0           | NULL         | NULL       |
+----------+-------------+--------------+------------+
```

---

## 9. 多表查询 (JOIN)

### 9.1 表关系说明

```
users (用户表)
+----+----------+
| id | username |
+----+----------+
| 1  | zhangsan |
| 2  | lisi     |
| 3  | wangwu   |
+----+----------+

orders (订单表)
+----+---------+--------+
| id | user_id | amount |
+----+---------+--------+
| 1  | 1       | 100    |
| 2  | 1       | 200    |
| 3  | 2       | 300    |
| 4  | 4       | 400    |  -- user_id=4 不存在
+----+---------+--------+
```

### 9.2 INNER JOIN (内连接)

```sql
-- 只返回两个表都匹配的行
SELECT
    u.username,
    o.order_no,
    o.amount
FROM users u
INNER JOIN orders o ON u.id = o.user_id;

-- 执行结果：
+----------+----------+--------+
| username | order_no | amount |
+----------+----------+--------+
| zhangsan | ORD001   | 99.00  |
| zhangsan | ORD002   | 199.00 |
| lisi     | ORD003   | 299.00 |
| lisi     | ORD004   | 399.00 |
| lisi     | ORD005   | 499.00 |
| wangwu   | ORD006   | 599.00 |
| zhaoliu  | ORD007   | 699.00 |
| sunqi    | ORD008   | 799.00 |
+----------+----------+--------+

-- 注意：zhouba、wujiu、zhengyi 没有订单，所以不显示
-- 注意：orders 表所有记录都有对应的用户，所以全部显示

-- 类比 MongoDB (使用 $lookup)
db.orders.aggregate([
    { $lookup: {
        from: 'users',
        localField: 'user_id',
        foreignField: '_id',
        as: 'user'
    }},
    { $unwind: '$user' }
])
```

### 9.3 LEFT JOIN (左连接)

```sql
-- 返回左表所有行，右表没有的用 NULL 填充
SELECT
    u.username,
    o.order_no,
    o.amount
FROM users u
LEFT JOIN orders o ON u.id = o.user_id;

-- 执行结果：
+----------+----------+--------+
| username | order_no | amount |
+----------+----------+--------+
| zhangsan | ORD001   | 99.00  |
| zhangsan | ORD002   | 199.00 |
| lisi     | ORD003   | 299.00 |
| lisi     | ORD004   | 399.00 |
| lisi     | ORD005   | 499.00 |
| wangwu   | ORD006   | 599.00 |
| zhaoliu  | ORD007   | 699.00 |
| sunqi    | ORD008   | 799.00 |
| zhouba   | NULL     | NULL   |  -- 没有订单
| wujiu    | NULL     | NULL   |  -- 没有订单
| zhengyi  | NULL     | NULL   |  -- 没有订单
+----------+----------+--------+

-- 实用场景：查询所有用户及其订单（包括没有订单的用户）
```

### 9.4 RIGHT JOIN (右连接)

```sql
-- 返回右表所有行，左表没有的用 NULL 填充
SELECT
    u.username,
    o.order_no,
    o.amount
FROM users u
RIGHT JOIN orders o ON u.id = o.user_id;

-- 注意：RIGHT JOIN 较少使用，通常用 LEFT JOIN 代替
-- 上面的查询可以改写为：
SELECT
    u.username,
    o.order_no,
    o.amount
FROM orders o
LEFT JOIN users u ON u.id = o.user_id;
```

### 9.5 FULL OUTER JOIN (全外连接)

```sql
-- MySQL 不支持 FULL OUTER JOIN
-- 可以用 UNION 模拟

-- 返回两个表的所有行
SELECT u.username, o.order_no
FROM users u
LEFT JOIN orders o ON u.id = o.user_id
UNION ALL
SELECT u.username, o.order_no
FROM users u
RIGHT JOIN orders o ON u.id = o.user_id
WHERE u.id IS NULL;
```

### 9.6 多表连接（三个表）

```sql
-- 假设有订单明细表 order_items
-- order_items: id, order_id, product_id, quantity, price

-- 查询用户 - 订单 - 商品详情
SELECT
    u.username,
    o.order_no,
    p.name as product_name,
    oi.quantity,
    oi.price,
    oi.quantity * oi.price as subtotal
FROM users u
INNER JOIN orders o ON u.id = o.user_id
INNER JOIN order_items oi ON o.id = oi.order_id
INNER JOIN products p ON oi.product_id = p.id;
```

### 9.7 JOIN 条件与 WHERE 条件

```sql
-- 方式 1：JOIN 条件中过滤
SELECT u.username, o.amount
FROM users u
LEFT JOIN orders o ON u.id = o.user_id AND o.amount > 500;

-- 方式 2：WHERE 中过滤
SELECT u.username, o.amount
FROM users u
LEFT JOIN orders o ON u.id = o.user_id
WHERE o.amount > 500;

-- 区别：
-- 方式 1：左连接依然返回所有用户，只是 amount <= 500 的显示 NULL
-- 方式 2：变成了内连接，只返回 amount > 500 的记录
```

---

## 10. Java 代码示例

### 10.1 准备工作

```java
// 1. 实体类
@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String nickname;
    private String email;
    private Integer age;
    private BigDecimal balance;
    private Integer status;
    private Date createTime;
    private Date updateTime;
}

// 2. 行映射器
private RowMapper<User> rowMapper = (rs, rowNum) -> {
    User user = new User();
    user.setId(rs.getLong("id"));
    user.setUsername(rs.getString("username"));
    user.setPassword(rs.getString("password"));
    user.setNickname(rs.getString("nickname"));
    user.setEmail(rs.getString("email"));
    user.setAge(rs.getInt("age"));
    user.setBalance(rs.getBigDecimal("balance"));
    user.setStatus(rs.getInt("status"));
    user.setCreateTime(rs.getTimestamp("create_time"));
    user.setUpdateTime(rs.getTimestamp("update_time"));
    return user;
};
```

### 10.2 查询操作

```java
@Autowired
private JdbcTemplate jdbcTemplate;

// 查询所有
public List<User> findAll() {
    String sql = "SELECT * FROM users ORDER BY id DESC";
    return jdbcTemplate.query(sql, rowMapper);
}

// 根据 ID 查询
public User findById(Long id) {
    String sql = "SELECT * FROM users WHERE id = ?";
    try {
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    } catch (EmptyResultDataAccessException e) {
        return null;  // 不存在返回 null
    }
}

// 条件查询
public List<User> findByAgeGreaterThan(Integer age) {
    String sql = "SELECT * FROM users WHERE age > ?";
    return jdbcTemplate.query(sql, rowMapper, age);
}

// 模糊查询
public List<User> findByUsernameLike(String keyword) {
    String sql = "SELECT * FROM users WHERE username LIKE ?";
    return jdbcTemplate.query(sql, rowMapper, "%" + keyword + "%");
}

// 分页查询
public Page<User> findPage(int page, int pageSize) {
    int offset = (page - 1) * pageSize;

    String sql = "SELECT * FROM users ORDER BY id DESC LIMIT ?, ?";
    List<User> content = jdbcTemplate.query(sql, rowMapper, offset, pageSize);

    String countSql = "SELECT COUNT(*) FROM users";
    int total = jdbcTemplate.queryForObject(countSql, Integer.class);

    return new Page<>(content, total, page, pageSize);
}
```

### 10.3 新增操作

```java
// 新增用户
public User createUser(User user) {
    String sql = "INSERT INTO users (username, password, nickname, email, age, balance) " +
                 "VALUES (?, ?, ?, ?, ?, ?)";

    jdbcTemplate.update(sql,
        user.getUsername(),
        user.getPassword(),
        user.getNickname(),
        user.getEmail(),
        user.getAge(),
        user.getBalance()
    );

    // 获取自增 ID
    Long id = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
    user.setId(id);

    return user;
}

// 批量插入
public void batchInsert(List<User> users) {
    String sql = "INSERT INTO users (username, password, email, age) VALUES (?, ?, ?, ?)";

    List<Object[]> batchArgs = new ArrayList<>();
    for (User user : users) {
        batchArgs.add(new Object[]{
            user.getUsername(),
            user.getPassword(),
            user.getEmail(),
            user.getAge()
        });
    }

    jdbcTemplate.batchUpdate(sql, batchArgs);
}
```

### 10.4 更新操作

```java
// 更新用户
public int updateUser(User user) {
    String sql = "UPDATE users SET username=?, nickname=?, email=?, age=?, balance=?, " +
                 "update_time=NOW() WHERE id=?";

    return jdbcTemplate.update(sql,
        user.getUsername(),
        user.getNickname(),
        user.getEmail(),
        user.getAge(),
        user.getBalance(),
        user.getId()
    );
}

// 部分更新（只更新邮箱）
public int updateEmail(Long id, String email) {
    String sql = "UPDATE users SET email=?, update_time=NOW() WHERE id=?";
    return jdbcTemplate.update(sql, email, id);
}

// 余额操作（充值）
public int addBalance(Long id, BigDecimal amount) {
    String sql = "UPDATE users SET balance = balance + ? WHERE id = ?";
    return jdbcTemplate.update(sql, amount, id);
}
```

### 10.5 删除操作

```java
// 删除用户
public int deleteById(Long id) {
    String sql = "DELETE FROM users WHERE id = ?";
    return jdbcTemplate.update(sql, id);
}

// 软删除
public int softDelete(Long id) {
    String sql = "UPDATE users SET deleted = 1, delete_time = NOW() WHERE id = ?";
    return jdbcTemplate.update(sql, id);
}
```

### 10.6 聚合查询

```java
// 统计总数
public int count() {
    String sql = "SELECT COUNT(*) FROM users";
    return jdbcTemplate.queryForObject(sql, Integer.class);
}

// 统计符合条件的数量
public int countByStatus(Integer status) {
    String sql = "SELECT COUNT(*) FROM users WHERE status = ?";
    return jdbcTemplate.queryForObject(sql, Integer.class, status);
}

// 查询统计信息
public Map<String, Object> getStatistics() {
    String sql = "SELECT COUNT(*) as total, AVG(age) as avg_age, " +
                 "SUM(balance) as total_balance FROM users";
    return jdbcTemplate.queryForMap(sql);
}

// 按状态分组统计
public List<Map<String, Object>> groupByStatus() {
    String sql = "SELECT status, COUNT(*) as count, AVG(balance) as avg_balance " +
                 "FROM users GROUP BY status";
    return jdbcTemplate.queryForList(sql);
}
```

### 10.7 复杂查询（多表 JOIN）

```java
// 查询用户及其订单
public List<Map<String, Object>> getUserOrders() {
    String sql = "SELECT u.username, o.order_no, o.amount, o.create_time " +
                 "FROM users u " +
                 "INNER JOIN orders o ON u.id = o.user_id " +
                 "ORDER BY o.create_time DESC";
    return jdbcTemplate.queryForList(sql);
}

// 统计每个用户的订单数量和金额
public List<Map<String, Object>> getUserOrderStats() {
    String sql = "SELECT u.username, " +
                 "       COUNT(o.id) as order_count, " +
                 "       SUM(o.amount) as total_amount, " +
                 "       AVG(o.amount) as avg_amount " +
                 "FROM users u " +
                 "LEFT JOIN orders o ON u.id = o.user_id " +
                 "GROUP BY u.id, u.username " +
                 "ORDER BY total_amount DESC";
    return jdbcTemplate.queryForList(sql);
}
```

---

## 11. 完整实战案例

### 11.1 电商系统 - 用户下单

```sql
-- 场景：用户下单（需要事务）

-- 1. 开启事务
START TRANSACTION;

-- 2. 检查库存
SELECT stock FROM products WHERE id = 1;
-- 假设库存充足，继续

-- 3. 创建订单
INSERT INTO orders (order_no, user_id, amount, status)
VALUES ('ORD' || DATE_FORMAT(NOW(), '%Y%m%d%H%i%s'), 1, 7999.00, 0);

-- 4. 扣减库存
UPDATE products SET stock = stock - 1 WHERE id = 1;

-- 5. 扣减用户余额
UPDATE users SET balance = balance - 7999.00 WHERE id = 1;

-- 6. 提交事务
COMMIT;

-- 如果任何一步失败
ROLLBACK;  -- 回滚所有操作
```

### 11.2 用户签到系统

```sql
-- 1. 创建签到表
CREATE TABLE user_signin (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    signin_date DATE NOT NULL,
    continuous_days INT DEFAULT 1,
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_date (user_id, signin_date)
);

-- 2. 用户签到
INSERT INTO user_signin (user_id, signin_date)
VALUES (1, CURDATE())
ON DUPLICATE KEY UPDATE signin_date = signin_date;  -- 防止重复签到

-- 3. 更新连续签到天数
UPDATE user_signin
SET continuous_days = (
    SELECT COUNT(*) FROM (
        SELECT signin_date FROM user_signin
        WHERE user_id = 1
        ORDER BY signin_date DESC
        LIMIT 7  -- 最近 7 天
    ) t
)
WHERE user_id = 1 AND signin_date = CURDATE();

-- 4. 查询用户签到记录
SELECT * FROM user_signin WHERE user_id = 1 ORDER BY signin_date DESC;
```

### 11.3 排行榜系统

```sql
-- 1. 余额排行榜（前 10 名）
SELECT
    ROW_NUMBER() OVER (ORDER BY balance DESC) as rank,
    username,
    balance
FROM users
WHERE status = 1
ORDER BY balance DESC
LIMIT 10;

-- 2. 消费排行榜
SELECT
    u.username,
    SUM(o.amount) as total_spent,
    COUNT(o.id) as order_count
FROM users u
INNER JOIN orders o ON u.id = o.user_id
WHERE o.status = 1  -- 只统计已支付订单
GROUP BY u.id, u.username
ORDER BY total_spent DESC
LIMIT 10;
```

### 11.4 数据分析

```sql
-- 1. 每日订单统计
SELECT
    DATE(create_time) as date,
    COUNT(*) as order_count,
    SUM(amount) as total_amount,
    AVG(amount) as avg_amount
FROM orders
WHERE status = 1
GROUP BY DATE(create_time)
ORDER BY date DESC
LIMIT 30;  -- 最近 30 天

-- 2. 用户年龄段分布
SELECT
    CASE
        WHEN age < 25 THEN '25 岁以下'
        WHEN age >= 25 AND age < 30 THEN '25-30 岁'
        WHEN age >= 30 AND age < 35 THEN '30-35 岁'
        ELSE '35 岁以上'
    END as age_group,
    COUNT(*) as count
FROM users
GROUP BY age_group;

-- 3. 复购率分析
SELECT
    COUNT(DISTINCT CASE WHEN order_count > 1 THEN user_id END) as repeat_users,
    COUNT(DISTINCT user_id) as total_users,
    CONCAT(
        ROUND(
            COUNT(DISTINCT CASE WHEN order_count > 1 THEN user_id END) * 100.0 /
            COUNT(DISTINCT user_id), 2
        ), '%'
    ) as repeat_rate
FROM (
    SELECT user_id, COUNT(*) as order_count
    FROM orders
    GROUP BY user_id
) t;
```

---

## 📎 附录：常用 SQL 函数速查

### 字符串函数

```sql
CONCAT('Hello', ' ', 'World')  -- 拼接：Hello World
CONCAT_WS('-', 'A', 'B', 'C')  -- 指定分隔符：A-B-C
LENGTH(username)               -- 字节长度
CHAR_LENGTH(username)          -- 字符长度（中文算 1 个）
UPPER(username)                -- 转大写：ZHANGSAN
LOWER(username)                -- 转小写：zhangsan
LTRIM(username)                -- 去左边空格
RTRIM(username)                -- 去右边空格
TRIM(username)                 -- 去两边空格
SUBSTR(username, 1, 3)         -- 截取：zha
REPLACE(email, 'qq', '163')    -- 替换
REVERSE(username)              -- 反转：nasgnahz
INSTR(username, 'an')          -- 查找位置：3
```

### 日期函数

```sql
NOW()                          -- 当前日期时间：2024-01-01 12:00:00
CURDATE()                      -- 当前日期：2024-01-01
CURTIME()                      -- 当前时间：12:00:00
YEAR(NOW())                    -- 年份：2024
MONTH(NOW())                   -- 月份：1
DAY(NOW())                     -- 日期：1
HOUR(NOW())                    -- 小时：12
MINUTE(NOW())                  -- 分钟：0
SECOND(NOW())                  -- 秒：0
WEEKDAY(NOW())                 -- 星期几：0(周一)
DATE_FORMAT(NOW(), '%Y-%m-%d') -- 格式化：2024-01-01
DATE_ADD(NOW(), INTERVAL 1 DAY)   -- 加 1 天
DATE_SUB(NOW(), INTERVAL 1 MONTH) -- 减 1 月
DATEDIFF(date1, date2)         -- 日期差（天）
TIMESTAMPDIFF(HOUR, t1, t2)    -- 时间差（小时）
```

### 数值函数

```sql
ROUND(3.14159, 2)    -- 四舍五入：3.14
CEIL(3.14)           -- 向上取整：4
FLOOR(3.14)          -- 向下取整：3
TRUNCATE(3.14159, 2) -- 截断：3.14
ABS(-10)             -- 绝对值：10
MOD(10, 3)           -- 取模：1
POWER(2, 3)          -- 幂运算：8
SQRT(16)             -- 平方根：4
RAND()               -- 随机数：0.xxxx
```

### 类型转换

```sql
CAST('123' AS UNSIGNED)      -- 字符串转数字：123
CAST(123 AS CHAR)            -- 数字转字符串：'123'
CAST('2024-01-01' AS DATE)   -- 字符串转日期
CONVERT(3.14, UNSIGNED)      -- 转整数：3
```

---

## 🎯 练习题

### 难度：⭐ 基础题

1. 查询所有用户的用户名和邮箱
2. 查询年龄大于 25 岁的用户
3. 查询用户名包含 "张" 的用户
4. 查询前 5 个用户

<details>
<summary>点击查看答案</summary>

```sql
1. SELECT username, email FROM users;
2. SELECT * FROM users WHERE age > 25;
3. SELECT * FROM users WHERE username LIKE '%张%';
4. SELECT * FROM users LIMIT 5;
```
</details>

### 难度：⭐⭐ 进阶题

5. 统计每个年龄有多少人
6. 查询有订单的用户及其订单数量
7. 查询余额最多的前 3 个用户
8. 更新所有用户的年龄 +1

<details>
<summary>点击查看答案</summary>

```sql
5. SELECT age, COUNT(*) FROM users GROUP BY age;
6. SELECT u.username, COUNT(o.id) FROM users u
   INNER JOIN orders o ON u.id = o.user_id
   GROUP BY u.id, u.username;
7. SELECT * FROM users ORDER BY balance DESC LIMIT 3;
8. UPDATE users SET age = age + 1;
```
</details>

### 难度：⭐⭐⭐ 高级题

9. 查询没有订单的用户
10. 查询每个用户的订单总金额，只显示超过 1000 元的
11. 连续签到 7 天的用户有哪些

<details>
<summary>点击查看答案</summary>

```sql
9. SELECT u.* FROM users u
   LEFT JOIN orders o ON u.id = o.user_id
   WHERE o.id IS NULL;

10. SELECT u.username, SUM(o.amount) as total
    FROM users u
    INNER JOIN orders o ON u.id = o.user_id
    GROUP BY u.id, u.username
    HAVING SUM(o.amount) > 1000;

11. SELECT user_id FROM user_signin
    WHERE signin_date >= DATE_SUB(CURDATE(), INTERVAL 6 DAY)
    GROUP BY user_id
    HAVING COUNT(*) = 7;
```
</details>

---

*创建时间：2026-03-17*
*最后更新：2026-03-17*
