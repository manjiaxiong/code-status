# Java 学习指南

## 第一步：环境配置

### JDK 已安装

JDK 8 已安装在你电脑上：
- 版本：`1.8.0_432` (Temurin OpenJDK)
- 位置：`C:\Users\19579\jdk8\jdk8u432-b06`
- 环境变量已自动配置

### 验证安装
```bash
java -version  # 应显示 1.8.0_432
javac -version # 应显示 javac 1.8.0_432
```

### 4. 安装 Maven
下载：https://maven.apache.org/download.cgi
```bash
mvn -version  # 验证安装
```

---

## 第二步：Java 基础语法

### 1. 第一个 Java 程序

```java
// HelloWorld.java
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}
```

编译运行：
```bash
javac HelloWorld.java  # 编译
java HelloWorld        # 运行
```

### 2. 变量与数据类型

```java
public class Variables {
    public static void main(String[] args) {
        // 基本数据类型
        int age = 25;           // 整数
        double salary = 5000.5; // 小数
        char grade = 'A';       // 字符
        boolean isWorking = true; // 布尔

        // 引用类型
        String name = "张三";   // 字符串

        System.out.println(name + "今年" + age + "岁");
    }
}
```

### 3. 控制流程

```java
public class ControlFlow {
    public static void main(String[] args) {
        // if-else
        int score = 85;
        if (score >= 90) {
            System.out.println("优秀");
        } else if (score >= 60) {
            System.out.println("及格");
        } else {
            System.out.println("不及格");
        }

        // for 循环
        for (int i = 0; i < 5; i++) {
            System.out.println("第" + i + "次循环");
        }

        // while 循环
        int j = 0;
        while (j < 5) {
            System.out.println("while 第" + j + "次");
            j++;
        }
    }
}
```

### 4. 数组

```java
public class Arrays {
    public static void main(String[] args) {
        // 声明数组
        int[] numbers = {1, 2, 3, 4, 5};
        String[] names = new String[]{"张三", "李四", "王五"};

        // 遍历数组
        for (int i = 0; i < numbers.length; i++) {
            System.out.println(numbers[i]);
        }

        // 增强 for 循环
        for (String name : names) {
            System.out.println(name);
        }
    }
}
```

### 5. 方法与类

```java
public class Person {
    // 属性（字段）
    private String name;
    private int age;

    // 构造方法
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // 实例方法
    public void sayHello() {
        System.out.println("你好，我是" + name);
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    // 静态方法
    public static void main(String[] args) {
        Person p = new Person("张三", 25);
        p.sayHello();
    }
}
```

### 6. 接口与抽象类

```java
// 接口 - 定义行为规范
interface Animal {
    void eat();      // 默认是 public abstract
    void sleep();
}

// 抽象类 - 部分实现
abstract class Mammal {
    protected String name;

    public void breathe() {
        System.out.println("呼吸空气");
    }

    public abstract void makeSound(); // 抽象方法
}

// 实现类
public class Dog extends Mammal implements Animal {
    public Dog(String name) {
        this.name = name;
    }

    @Override
    public void eat() {
        System.out.println(name + "在吃东西");
    }

    @Override
    public void sleep() {
        System.out.println(name + "在睡觉");
    }

    @Override
    public void makeSound() {
        System.out.println(name + "汪汪叫");
    }

    public static void main(String[] args) {
        Dog dog = new Dog("旺财");
        dog.eat();
        dog.sleep();
        dog.makeSound();
        dog.breathe();
    }
}
```

### 7. 泛型与集合

```java
import java.util.*;

public class Collections {
    public static void main(String[] args) {
        // List - 有序列表
        List<String> list = new ArrayList<>();
        list.add("苹果");
        list.add("香蕉");
        list.add("橙子");

        // Map - 键值对
        Map<String, Integer> map = new HashMap<>();
        map.put("张三", 25);
        map.put("李四", 30);

        // Set - 不重复集合
        Set<String> set = new HashSet<>();
        set.add("A");
        set.add("B");

        // 遍历 Map
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
```

---

## 第三步：练习任务

### 练习 1：计算器
写一个类，实现加减乘除四个方法。

### 练习 2：学生管理
创建一个 Student 类，包含姓名、年龄、成绩属性，实现：
- 添加学生
- 查找学生
- 计算平均分

### 练习 3：简易图书管理
使用 List 存储图书，实现增删改查功能。

---

## 前端对比理解

| Java 概念 | JavaScript 对应 |
|-----------|----------------|
| class | class / function |
| interface | TypeScript interface |
| List<T> | Array |
| Map<K,V> | Object / Map |
| public/private | 默认都公开 |
| static | 模块级函数 |
| 泛型 | TypeScript 泛型 |

---

## 下一步

完成基础语法学习后，我们将：
1. 学习 Spring Boot 框架
2. 创建第一个 REST API
3. 连接数据库

有任何问题随时问我！
