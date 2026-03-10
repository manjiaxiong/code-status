# IntelliJ IDEA 新手入门指南

> 🎯 目标：用 IDEA 运行你的第一个 Spring Boot 项目

---

## 📥 第一步：下载 IDEA

### 下载地址
https://www.jetbrains.com/idea/download/

### 版本选择

| 版本 | 价格 | 推荐 |
|------|------|------|
| **Community（社区版）** | 免费 | ✅ 初学者够用 |
| **Ultimate（旗舰版）** | 付费（学生免费） | 功能更全 |

**建议：** 先装社区版，不够用再换旗舰版

### 安装步骤（Windows）

1. 下载 `IJCA-win.exe` 安装包
2. 双击运行
3. 一路 Next（默认安装）
4. 选择安装位置（建议默认 `C:\Program Files\JetBrains\`）
5. 勾选：
   - ✅ 创建桌面快捷方式
   - ✅ 添加"Open Folder as Project"
6. 完成安装

---

## 🚀 第二步：打开项目

### 方法 1：直接打开文件夹

1. 找到项目文件夹：`D:\code\code\status\springboot-demo`
2. 右键 → **Open Folder as Project**（或用 IDEA 打开）
3. IDEA 会识别这是一个 Maven 项目

### 方法 2：从 IDEA 启动

1. 打开 IDEA
2. 点击 **Open**（或 File → Open）
3. 选择 `D:\code\code\status\springboot-demo` 文件夹
4. 点击 **OK**

### 首次打开会做什么？

IDEA 会：
1. 识别 `pom.xml`（Maven 项目标识）
2. 自动下载依赖（第一次需要几分钟）
3. 建立项目索引

**等待右下角进度条完成即可**

---

## ▶️ 第三步：运行 Spring Boot

### 找到启动类

1. 在左侧项目视图中展开：
   ```
   springboot-demo
   └── src
       └── main
           └── java
               └── com.example.demo
                   └── DemoApplication.java  ← 这个！
   ```

2. 点击打开 `DemoApplication.java`

### 运行项目

**方法 1：绿色运行按钮（最简单）**

1. 找到 `main` 方法那一行
2. 左侧有一个绿色三角形 ▶
3. 点击 → 选择 **Run 'DemoApplication.main()'**

**方法 2：右键运行**

1. 在 `DemoApplication.java` 上右键
2. 选择 **Run 'DemoApplication.main()'**

**方法 3：快捷键**

1. 打开 `DemoApplication.java`
2. 按 `Shift + F10`

### 运行成功标志

控制台出现：
```
========================================
   🚀 Spring Boot 启动成功!
   访问地址：http://localhost:8080
========================================
```

---

## 🌐 第四步：测试接口

打开浏览器访问：

### 测试 1：Hello 接口
```
http://localhost:8080/hello
```
**预期输出：** `Hello, Spring Boot!`

### 测试 2：用户信息接口
```
http://localhost:8080/api/user
```
**预期输出：**
```json
{
  "id": 1,
  "name": "张三",
  "age": 25,
  "email": "zhangsan@example.com"
}
```

### 测试 3：服务器时间
```
http://localhost:8080/api/server-time
```
**预期输出：** JSON 格式的时间戳

---

## 🛠️ 第五步：常用操作

### 修改代码后重启

**方式 1：手动重启**
1. 点击控制台红色方块 ⬛（停止）
2. 再次点击绿色三角形 ▶（运行）

**方式 2：热部署（推荐）**
- 修改代码后按 `Ctrl + F9`（重新编译）
- Spring Boot DevTools 会自动重启

### 查看控制台日志

- 底部 **Run** 窗口显示所有日志
- 可以筛选 ERROR、WARN 等信息

### 调试代码（Debug）

1. 在代码行号左侧点击，添加红点（断点）
2. 点击绿色虫子图标 🐛（Debug 模式运行）
3. 访问接口，程序会在断点处暂停
4. 可以查看变量值、单步执行

---

## ⌨️ 常用快捷键（Windows）

| 功能 | 快捷键 |
|------|--------|
| 运行 | `Shift + F10` |
| 调试 | `Shift + F9` |
| 停止 | `Ctrl + F2` |
| 重新编译 | `Ctrl + F9` |
| 查找类 | `Ctrl + N` |
| 查找文件 | `Ctrl + Shift + N` |
| 全局搜索 | `Double Shift`（按两次 Shift） |
| 格式化代码 | `Ctrl + Alt + L` |
| 自动导入 | `Alt + Enter` |
| 复制行 | `Ctrl + D` |
| 删除行 | `Ctrl + Y` |
| 注释代码 | `Ctrl + /` |

---

## 🆘 常见问题

### 1. 提示"JDK not found"

**原因：** IDEA 没找到 JDK

**解决：**
1. File → Project Structure → SDKs
2. 点击 `+` → Add JDK
3. 选择你的 JDK 路径：`C:\Users\19579\jdk8\jdk8u432-b06`
4. 点击 OK

### 2. Maven 依赖下载失败

**原因：** 网络问题

**解决：配置阿里云镜像**

在 `pom.xml` 中添加：
```xml
<repositories>
    <repository>
        <id>aliyun</id>
        <url>https://maven.aliyun.com/repository/public</url>
    </repository>
</repositories>
```

然后右键 `pom.xml` → Maven → Reload Project

### 3. 端口被占用（Port 8080 was already in use）

**解决 1：** 修改端口
```properties
# application.properties
server.port=8081
```

**解决 2：** 关闭占用端口的程序
```bash
# 查看占用 8080 的进程
netstat -ano | findstr :8080

# 杀死进程（替换 PID）
taskkill /F /PID 12345
```

### 4. 中文乱码

**解决：**
```properties
# application.properties
spring.http.encoding.force=true
spring.http.encoding.charset=UTF-8
server.tomcat.uri-encoding=UTF-8
```

### 5. 代码提示不工作

**解决：**
1. File → Invalidate Caches
2. 勾选所有选项
3. 点击 **Invalidate and Restart**

---

## 📚 学习建议

1. **第一天：** 熟悉 IDEA 界面和基本操作
2. **第二天：** 学会调试（Debug）
3. **第三天：** 记住常用快捷键

不要一次记所有快捷键，用多了自然就记住了！

---

## ✅ 检查清单

- [ ] IDEA 安装完成
- [ ] 项目成功打开
- [ ] 能运行 Spring Boot
- [ ] 能访问测试接口
- [ ] 知道如何停止和重启

---

**搞定！你已经会用 IDEA 了！** 🎉
