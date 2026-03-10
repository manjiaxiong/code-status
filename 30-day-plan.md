# 30 天 Java 全栈速成计划

> 👨‍💻 专为**精通前端**的开发者定制
> 每天 3-4 小时，21 天完成核心技能学习

---

## 📊 你的优势

| 已掌握技能 | 可跳过内容 | 节省时间 |
|-----------|-----------|----------|
| HTTP 协议 | HTTP 基础讲解 | -2 天 |
| REST API | RESTful 概念 | -1 天 |
| JSON | 数据格式讲解 | -1 天 |
| Node.js/Express | 路由、中间件概念 | -2 天 |
| 前端框架 | 前后端联调 | -3 天 |

**实际学习时间：约 14-18 天**

---

## 📅 第 1 周：Spring Boot 核心（7 天）

### Day 1：第一个 Spring Boot 应用 ✅

**内容：**
- [x] 项目结构讲解
- [x] @RestController、@GetMapping
- [x] 运行并测试接口

**产出：**
- 能运行的 Spring Boot 项目
- 3 个测试接口

**练习：** 添加 `/api/about` 接口

---

### Day 2：请求参数处理

**内容：**
- `@PathVariable` - 路径参数（`/user/{id}`）
- `@RequestParam` - 查询参数（`?keyword=xxx`）
- `@RequestBody` - 请求体 JSON

**类比前端：**
```javascript
// 路径参数
app.get('/user/:id', (req, res) => {
    const id = req.params.id;
});

// 查询参数
app.get('/search', (req, res) => {
    const keyword = req.query.keyword;
});

// 请求体
app.post('/user', (req, res) => {
    const user = req.body;
});
```

**产出：** 完善用户管理 API

---

### Day 3：JPA 操作数据库

**内容：**
- `@Entity` - 实体类（类似 TypeScript interface）
- `JpaRepository` - 数据访问层
- 基础 CRUD：save、find、delete

**类比前端（TypeORM）：**
```typescript
// 实体类
@Entity()
class User {
    @PrimaryColumn()
    id: number;

    @Column()
    name: string;
}

// Repository
const users = await userRepository.find();
await userRepository.save(user);
```

**产出：** 能操作数据库的 Repository

---

### Day 4：MySQL 数据库

**内容：**
- 安装 MySQL（或 Docker）
- 创建数据库和表
- 配置数据源

**SQL 基础：**
```sql
-- 创建数据库
CREATE DATABASE demo;

-- 创建表
CREATE TABLE user (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50),
    email VARCHAR(100)
);

-- 增删改查
INSERT INTO user (name, email) VALUES ('张三', 'zhangsan@example.com');
SELECT * FROM user;
UPDATE user SET email = 'new@example.com' WHERE id = 1;
DELETE FROM user WHERE id = 1;
```

**产出：** 配置好数据库连接

---

### Day 5-6：用户管理系统（实战）

**完整 API 列表：**

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/users | 获取用户列表 |
| GET | /api/users/{id} | 获取用户详情 |
| POST | /api/users | 创建用户 |
| PUT | /api/users/{id} | 更新用户 |
| DELETE | /api/users/{id} | 删除用户 |

**产出：** 完整的 CRUD 接口

---

### Day 7：JWT 认证

**内容：**
- JWT 原理（3 部分组成）
- 生成 Token
- 验证 Token
- 登录接口

**产出：** 登录/注册接口

---

## 📅 第 2 周：项目实战（7 天）

### 项目选择：AI 对话管理系统

**为什么选这个？**
1. 符合公司前后端合并方向
2. 展示 AI 集成能力
3. 技术栈完整（CRUD + 认证 + 外部 API）

**功能列表：**
- 用户登录/注册
- 对话历史管理
- 调用 Claude API
- 前端界面（React）

---

### Day 8-9：数据库设计

**表结构：**
```sql
-- 用户表
CREATE TABLE user (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE,
    password VARCHAR(255),
    created_at DATETIME
);

-- 对话表
CREATE TABLE conversation (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    title VARCHAR(100),
    created_at DATETIME,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

-- 消息表
CREATE TABLE message (
    id INT PRIMARY KEY AUTO_INCREMENT,
    conversation_id INT,
    role VARCHAR(20),  -- 'user' or 'assistant'
    content TEXT,
    created_at DATETIME,
    FOREIGN KEY (conversation_id) REFERENCES conversation(id)
);
```

---

### Day 10-12：后端 API 开发

**API 列表：**

| 模块 | 接口 |
|------|------|
| 认证 | POST /api/login, POST /api/register |
| 对话 | GET/POST/DELETE /api/conversations |
| 消息 | GET/POST /api/conversations/{id}/messages |
| AI | POST /api/chat（转发 Claude API） |

---

### Day 13-14：前端开发

**用你的 React 技能快速搭建：**
- 登录页面
- 对话列表
- 聊天界面
- API 调用封装

---

## 📅 第 3 周：部署 + AI（7 天）

### Day 15-16：项目打包部署

**内容：**
- Maven 打包：`mvn clean package`
- 服务器部署（本地虚拟机或云服务器）
- Nginx 反向代理

---

### Day 17-19：AI 集成

**内容：**
- 申请 Claude API Key
- 调用 Claude API
- 实现流式响应

---

### Day 20-21：简历 + 面试

**产出：**
- 项目作品集
- GitHub 仓库
- 简历优化

---

## ✅ 打卡表

| 周次 | Day 1 | Day 2 | Day 3 | Day 4 | Day 5 | Day 6 | Day 7 |
|------|-------|-------|-------|-------|-------|-------|-------|
| 第 1 周 | ✅ | ⬜ | ⬜ | ⬜ | ⬜ | ⬜ | ⬜ |
| 第 2 周 | ⬜ | ⬜ | ⬜ | ⬜ | ⬜ | ⬜ | ⬜ |
| 第 3 周 | ⬜ | ⬜ | ⬜ | ⬜ | ⬜ | ⬜ | ⬜ |

---

## 🛠️ 开发工具

| 用途 | 工具 |
|------|------|
| IDE | IntelliJ IDEA |
| 数据库 | MySQL Workbench / DBeaver |
| API 测试 | Postman / Apifox |
| 版本控制 | Git |

---

**坚持就是胜利！21 天后你就是全栈工程师！** 🚀
