# Java 基础语法学习指南

> 位置：`D:\code\code\status\java-learning\`

## 环境配置

### 永久配置环境变量（Windows）
1. 右键"此电脑" → 属性 → 高级系统设置
2. 点击"环境变量"
3. 新建系统变量：
   - 变量名：`JAVA_HOME`
   - 变量值：`C:\Users\19579\jdk8\jdk8u432-b06`
4. 找到 `Path`，编辑，新建：`%JAVA_HOME%\bin`

### 临时配置（当前终端）
```bash
export JAVA_HOME="/c/Users/19579/jdk8/jdk8u432-b06"
export PATH="$JAVA_HOME/bin:$PATH"
```

## 运行示例

### 运行单个示例
```bash
cd D:/code/code/status/java-learning/01-hello-world
javac -encoding UTF-8 HelloWorld.java
java HelloWorld
```

## 学习顺序

按数字顺序学习每个示例：

| 序号 | 目录 | 主题 | 核心内容 |
|------|------|------|----------|
| 01 | 01-hello-world | 第一个 Java 程序 | 编译、运行、println |
| 02 | 02-variables | 变量与数据类型 | 8 种基本类型、String、类型转换 |
| 03 | 03-operators | 运算符 | 算术、比较、逻辑、自增自减 |
| 04 | 04-if-else | 条件判断 | if-else、三元运算符 |
| 05 | 05-switch | switch 语句 | switch-case、break |
| 06 | 06-for-loop | for 循环 | for、增强 for、break/continue |
| 07 | 07-while-loop | while 循环 | while、do-while |
| 08 | 08-array | 数组 | 一维数组、二维数组、Arrays 工具 |
| 09 | 09-method | 方法 | 定义、重载、可变参数、递归 |
| 10 | 10-class-object | 类与对象 | 类定义、对象创建、getter/setter |
| 11 | 11-inheritance | 继承 | extends、方法重写、super、多态 |
| 12 | 12-interface | 接口 | implements、抽象方法、默认方法 |
| 13 | 13-list | List 集合 | ArrayList、LinkedList、增删改查 |
| 14 | 14-map | Map 集合 | HashMap、键值对、遍历 |
| 15 | 15-exception | 异常处理 | try-catch、finally、自定义异常 |
| 16 | 16-string | String | 常用方法、StringBuilder |

## 学习计划

### 第 1 天：基础语法
- 01-hello-world
- 02-variables
- 03-operators

### 第 2 天：流程控制
- 04-if-else
- 05-switch
- 06-for-loop
- 07-while-loop

### 第 3 天：数组与方法
- 08-array
- 09-method

### 第 4 天：面向对象（上）
- 10-class-object
- 11-inheritance
- 12-interface

### 第 5 天：面向对象（下）
- 13-list
- 14-map

### 第 6 天：异常与字符串
- 15-exception
- 16-string

### 第 7 天：综合练习
- 复习所有内容
- 完成每个文件中的练习题

## 学习建议

1. **不要只看代码** - 每个示例都要亲手运行
2. **修改代码尝试** - 改改参数，看看输出有什么变化
3. **完成练习题** - 每个文件末尾都有练习题
4. **做笔记** - 把不懂的地方记下来
5. **多问为什么** - 遇到不懂的随时问

## 常见问题

### 中文乱码
Windows 控制台可能显示中文乱码，这是编码问题，不影响功能。
解决：添加 `-Dfile.encoding=UTF-8` 参数
```bash
java -Dfile.encoding=UTF-8 HelloWorld
```

### 找不到类
确保先编译再运行：
```bash
javac -encoding UTF-8 HelloWorld.java
java HelloWorld
```

### 命令不存在
检查 JAVA_HOME 是否正确配置：
```bash
java -version
javac -version
```
