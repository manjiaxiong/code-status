# Spring Boot 环境配置与运行指南

**适用人群：** 零基础前端同学
**目标：** 在 IDEA 中运行 Spring Boot 项目

---

## 一、环境要求

### 1.1 已满足

- ✅ JDK 1.8 (已安装在 `C:\Users\19579\jdk8\jdk8u432-b06\`)
- ✅ IntelliJ IDEA (已安装在 `D:\IntelliJ IDEA 2025.3.3\`)

### 1.2 无需额外安装

> **好消息：** IDEA 内置了 Maven，所以不用单独安装 Maven！

---

## 二、用 IDEA 打开项目

### 步骤 1：打开 IDEA

双击桌面上的 IntelliJ IDEA 图标，或者运行：
```
D:\IntelliJ IDEA 2025.3.3\bin\idea64.exe
```

### 步骤 2：打开项目

1. 点击 **Open** (打开)
2. 找到你的项目文件夹：`D:\code\code\status\springboot-demo`
3. 点击 **OK**

> 注意：是打开 `springboot-demo` 这个文件夹，不是整个 `status` 文件夹

### 步骤 3：等待项目加载

IDEA 会：
1. 检测这是一个 Maven 项目
2. 自动下载依赖包（第一次需要几分钟）
3. 建立索引

**如何判断加载完成？**
- 底部状态栏显示 "All features loaded" 或类似提示
- 右侧出现 **Maven** 工具窗口

---

## 三、运行 Spring Boot 项目

### 方法 1：点击运行按钮（最简单）

1. 在左侧项目树中找到：
   ```
   src/main/java/com.example.demo/DemoApplication.java
   ```

2. 双击打开这个文件

3. 找到 `@SpringBootApplication` 这一行，左边或右边会有一个绿色的 **▶** 图标

4. 点击 **▶** → 选择 **Run 'DemoApplication'**

5. 等待启动，看到底部控制台输出：
   ```
   Started DemoApplication in X.XXX seconds
   🚀 Spring Boot 启动成功!
   访问地址：http://localhost:8080
   ```

### 方法 2：使用 Maven 工具窗口

1. 点击右侧边栏的 **Maven**

2. 展开：`springboot-demo` → `Plugins` → `spring-boot-maven-plugin`

3. 双击 `spring-boot:run`

### 方法 3：使用 IDEA 顶部菜单

1. 点击顶部菜单 **Run** → **Run...** (或按 `Shift+F10`)

2. 选择 `DemoApplication`

---

## 四、测试接口

项目启动后，打开浏览器访问：

### 测试接口 1：Hello
```
http://localhost:8080/hello
```
返回：`Hello, Spring Boot!`

### 测试接口 2：用户信息
```
http://localhost:8080/api/user
```
返回：
```json
{
  "id": 1,
  "name": "张三",
  "age": 25,
  "email": "zhangsan@example.com"
}
```

### 测试接口 3：服务器时间
```
http://localhost:8080/api/server-time
```
返回：
```json
{
  "timestamp": 1710000000000,
  "message": "当前服务器时间"
}
```

---

## 五、常见问题

### 问题 1：端口被占用

**错误信息：** `Port 8080 was already in use`

**解决方法：**
1. 打开 `src/main/resources/application.properties`
2. 修改端口号：
   ```properties
   server.port=8081
   ```
3. 重启项目

### 问题 2：Java 版本不对

**错误信息：** `Unsupported class file major version`

**解决方法：**
1. 按 `Ctrl+Alt+Shift+S` 打开项目结构
2. 左侧选择 **Project**
3. 确保 **Project SDK** 选择的是 1.8
4. 确保 **Project language level** 选择的是 8

### 问题 3：依赖下载失败

**现象：** 底部一直显示 "Downloading..." 或者报错

**解决方法：**
1. 点击右侧 **Maven** 工具窗口
2. 点击顶部的刷新图标 🔄
3. 或者删除 `C:\Users\19579\.m2\repository` 文件夹后重新运行

---

## 六、项目结构解析（ IDEA 视角）

打开项目后，你会看到这样的结构：

```
springboot-demo
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.example.demo
│   │   │       ├── DemoApplication.java      ← 启动类（入口）
│   │   │       └── controller
│   │   │           └── HelloController.java  ← 控制器（路由）
│   │   └── resources
│   │       └── application.properties        ← 配置文件
│   └── test                                  ← 测试代码
└── pom.xml                                   ← 项目配置（类似 package.json）
```

---

## 七、下一步

项目能运行后，开始学习：

1. **Day 8 教程：** `docs/day08-springboot-intro.md`
   - 理解每个文件的作用
   - 理解 pom.xml 配置
   - 理解配置文件

2. **动手尝试：**
   - 修改 `HelloController.java` 中的文字
   - 重新运行，看效果变化
   - 添加一个新的接口

---

## 八、常用 IDEA 快捷键

| 快捷键 | 作用 |
|--------|------|
| `Shift+F10` | 运行项目 |
| `Ctrl+F5` | 重新运行 |
| `Ctrl+/` | 切换注释 |
| `Ctrl+Shift+L` | 删除当前行 |
| `Ctrl+D` | 复制当前行 |
| `Alt+Enter` | 快速修复（导入包等） |
| `Ctrl+P` | 查看方法参数 |

---

*如有问题，随时问 AI 助手！*
