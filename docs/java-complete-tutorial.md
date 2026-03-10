# Java 基础语法完整教程

> 专为前端开发者编写，用 JavaScript 对比理解

---

## 目录

1. [变量与数据类型](#1-变量与数据类型)
2. [运算符](#2-运算符)
3. [控制流程](#3-控制流程)
4. [数组](#4-数组)
5. [方法](#5-方法)
6. [类与对象](#6-类与对象)
7. [继承与多态](#7-继承与多态)
8. [接口与抽象类](#8-接口与抽象类)
9. [集合框架](#9-集合框架)
10. [异常处理](#10-异常处理)
11. [泛型](#11-泛型)
12. [常用类](#12-常用类)

---

## 1. 变量与数据类型

### 1.1 声明变量

**Java：**
```java
// 格式：数据类型 变量名 = 值;
int age = 25;
String name = "张三";
double salary = 5000.5;
boolean isWorking = true;
```

**JavaScript 对比：**
```javascript
// JS 用 let/const，类型自动推断
let age = 25;
const name = "张三";
```

### 1.2 数据类型分类

#### 基本数据类型（8 种）

| 类型 | 说明 | 字节 | 示例 |
|------|------|------|------|
| `byte` | 字节 | 1 | `byte b = 100;` |
| `short` | 短整数 | 2 | `short s = 1000;` |
| `int` | 整数 | 4 | `int i = 100000;` |
| `long` | 长整数 | 8 | `long l = 100000L;` |
| `float` | 单精度浮点 | 4 | `float f = 3.14f;` |
| `double` | 双精度浮点 | 8 | `double d = 3.14159;` |
| `char` | 字符 | 2 | `char c = 'A';` |
| `boolean` | 布尔 | - | `boolean b = true;` |

#### 引用数据类型

```java
String name = "Hello";      // 字符串
int[] arr = {1, 2, 3};      // 数组
List<String> list;          // 列表
Map<String, Object> map;    // 映射
```

### 1.3 类型转换

```java
// 自动转换（小→大）
int i = 100;
double d = i;  // 自动转成 double

// 强制转换（大→小）
double d = 3.14;
int i = (int) d;  // 结果是 3，小数部分丢失

// String 转 int
String num = "123";
int n = Integer.parseInt(num);

// int 转 String
int x = 456;
String s = String.valueOf(x);  // 或 "" + x
```

---

## 2. 运算符

### 2.1 算术运算符

```java
int a = 10, b = 3;

a + b   // 13  加
a - b   // 7   减
a * b   // 30  乘
a / b   // 3   除（整数相除得整数，不要小数部分）
a % b   // 1   取余

// 重要：整数除法
int result = 5 / 2;  // 结果是 2，不是 2.5！
double result2 = 5.0 / 2;  // 结果是 2.5
```

### 2.2 自增自减

```java
int i = 5;
i++;  // i 变成 6
i--;  // i 变成 5

// ++i 和 i++ 的区别
int a = 5;
int b = ++a;  // b = 6, a = 6（先加后用）
int c = a++;  // c = 6, a = 7（先用后加）
```

### 2.3 比较运算符

```java
a == b   // 等于
a != b   // 不等于
a > b    // 大于
a < b    // 小于
a >= b   // 大于等于
a <= b   // 小于等于
```

### 2.4 逻辑运算符

```java
true && false   // 与（且）
true || false   // 或（或）
!true           // 非（取反）

// && 和 || 的短路特性
if (str != null && str.length() > 0) {
    // 安全，str 为 null 时不会执行后面
}
```

---

## 3. 控制流程

### 3.1 if-else

```java
int score = 85;

if (score >= 90) {
    System.out.println("优秀");
} else if (score >= 60) {
    System.out.println("及格");
} else {
    System.out.println("不及格");
}

// 三元运算符
String result = score >= 60 ? "及格" : "不及格";
```

**JavaScript 对比：**
```javascript
// 语法几乎一样
if (score >= 90) {
    console.log("优秀");
} else if (score >= 60) {
    console.log("及格");
} else {
    console.log("不及格");
}
```

### 3.2 switch

```java
int day = 3;
switch (day) {
    case 1:
        System.out.println("周一");
        break;  // 重要！不写会穿透
    case 2:
        System.out.println("周二");
        break;
    case 3:
        System.out.println("周三");
        break;
    default:
        System.out.println("其他");
}

// Java 7+ 支持 String
String fruit = "apple";
switch (fruit) {
    case "apple":
        System.out.println("苹果");
        break;
    case "banana":
        System.out.println("香蕉");
        break;
}
```

### 3.3 for 循环

```java
// 标准 for 循环
for (int i = 0; i < 5; i++) {
    System.out.println("第" + i + "次");
}

// 增强 for 循环（类似 JS 的 for...of）
int[] numbers = {1, 2, 3, 4, 5};
for (int num : numbers) {
    System.out.println(num);
}
```

**JavaScript 对比：**
```javascript
// 标准 for
for (let i = 0; i < 5; i++) {
    console.log("第" + i + "次");
}

// for...of
for (let num of numbers) {
    console.log(num);
}
```

### 3.4 while 循环

```java
int i = 0;
while (i < 5) {
    System.out.println(i);
    i++;
}

// do-while：先执行一次再判断
int j = 0;
do {
    System.out.println(j);
    j++;
} while (j < 5);
```

### 3.5 break 和 continue

```java
// break：跳出整个循环
for (int i = 0; i < 10; i++) {
    if (i == 5) {
        break;  // 循环结束
    }
    System.out.println(i);  // 输出 0-4
}

// continue：跳过本次循环
for (int i = 0; i < 5; i++) {
    if (i == 2) {
        continue;  // 跳过 2
    }
    System.out.println(i);  // 输出 0,1,3,4
}
```

---

## 4. 数组

### 4.1 声明数组

```java
// 方式 1：直接赋值
int[] arr1 = {1, 2, 3, 4, 5};

// 方式 2：new 数组
int[] arr2 = new int[5];  // 长度 5，默认值都是 0
arr2[0] = 10;
arr2[1] = 20;

// 方式 3：new 并初始化
int[] arr3 = new int[]{1, 2, 3};

// 字符串数组
String[] names = {"张三", "李四", "王五"};
```

**JavaScript 对比：**
```javascript
const arr1 = [1, 2, 3, 4, 5];
const arr2 = new Array(5);  // 长度 5
```

### 4.2 访问数组

```java
int[] arr = {10, 20, 30, 40, 50};

arr[0]      // 10，第一个元素
arr.length  // 5，数组长度（注意是属性不是方法！）

// 遍历数组
for (int i = 0; i < arr.length; i++) {
    System.out.println(arr[i]);
}

// 增强 for 循环
for (int val : arr) {
    System.out.println(val);
}
```

### 4.3 二维数组

```java
// 声明二维数组
int[][] matrix = {
    {1, 2, 3},
    {4, 5, 6},
    {7, 8, 9}
};

matrix[0][0]  // 1，第一行第一列
matrix[1][2]  // 6，第二行第三列

// 遍历二维数组
for (int i = 0; i < matrix.length; i++) {
    for (int j = 0; j < matrix[i].length; j++) {
        System.out.println(matrix[i][j]);
    }
}
```

---

## 5. 方法

### 5.1 方法的定义

```java
// 格式：修饰符 返回值类型 方法名 (参数列表) { 方法体 }

// 有返回值的方法
public int add(int a, int b) {
    return a + b;
}

// 无返回值的方法（void）
public void sayHello(String name) {
    System.out.println("你好，" + name);
}

// 无参数的方法
public int getRandom() {
    return (int) (Math.random() * 100);
}
```

**JavaScript 对比：**
```javascript
// 函数定义
function add(a, b) {
    return a + b;
}

// 箭头函数
const add = (a, b) => a + b;
```

### 5.2 方法调用

```java
// 调用方法
int result = add(5, 3);  // result = 8
sayHello("张三");

// 方法重载（同名方法，参数不同）
public int add(int a, int b) {
    return a + b;
}

public double add(double a, double b) {
    return a + b;
}

public int add(int a, int b, int c) {
    return a + b + c;
}

// 根据参数自动选择哪个方法
add(1, 2);        // 调用第一个
add(1.5, 2.5);    // 调用第二个
add(1, 2, 3);     // 调用第三个
```

### 5.3 可变参数

```java
// 可以传任意个 int 参数
public int sum(int... numbers) {
    int total = 0;
    for (int n : numbers) {
        total += n;
    }
    return total;
}

sum(1);          // 1
sum(1, 2, 3);    // 6
sum(1, 2, 3, 4, 5);  // 15
```

### 5.4 递归

```java
// 计算阶乘：5! = 5 * 4 * 3 * 2 * 1
public int factorial(int n) {
    if (n == 1) {
        return 1;  // 终止条件
    }
    return n * factorial(n - 1);  // 递归调用
}

factorial(5);  // 120

// 斐波那契数列
public int fibonacci(int n) {
    if (n <= 1) return n;
    return fibonacci(n - 1) + fibonacci(n - 2);
}

fibonacci(10);  // 55
```

---

## 6. 类与对象

### 6.1 定义类

```java
public class Person {
    // 属性（成员变量/字段）
    private String name;   // 姓名
    private int age;       // 年龄

    // 构造方法（用于创建对象）
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // 方法（行为）
    public void sayHello() {
        System.out.println("你好，我是" + name);
    }

    // Getter 和 Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
```

**JavaScript 对比：**
```javascript
class Person {
    constructor(name, age) {
        this.name = name;
        this.age = age;
    }

    sayHello() {
        console.log("你好，我是" + this.name);
    }
}
```

### 6.2 创建对象

```java
// 使用 new 关键字
Person p1 = new Person("张三", 25);
Person p2 = new Person("李四", 30);

// 调用方法
p1.sayHello();  // 你好，我是张三
p2.sayHello();  // 你好，我是李四

// 使用 getter/setter
p1.setAge(26);
System.out.println(p1.getAge());  // 26
```

### 6.3 访问修饰符

| 修饰符 | 同类 | 同包 | 子类 | 其他 |
|--------|------|------|------|------|
| `private` | ✓ | ✗ | ✗ | ✗ |
| (默认) | ✓ | ✓ | ✗ | ✗ |
| `protected` | ✓ | ✓ | ✓ | ✗ |
| `public` | ✓ | ✓ | ✓ | ✓ |

```java
public class Person {
    private String name;      // 只有本类能访问
    int age;                  // 同包能访问
    protected String phone;   // 子类能访问
    public String email;      // 谁都能访问
}
```

### 6.4 static 关键字

```java
public class Person {
    private String name;
    private static int count = 0;  // 静态变量，所有对象共享

    public Person(String name) {
        this.name = name;
        count++;  // 每创建一个对象，计数加 1
    }

    // 静态方法
    public static int getCount() {
        return count;
    }

    // 非静态方法
    public void sayHello() {
        System.out.println("你好，我是" + name);
    }
}

// 使用
Person p1 = new Person("张三");
Person p2 = new Person("李四");

Person.getCount();  // 2，静态方法通过类名调用
// p1.sayHello();   // 实例方法通过对象调用
```

**JavaScript 对比：**
```javascript
class Person {
    static count = 0;

    constructor(name) {
        this.name = name;
        Person.count++;
    }

    sayHello() {
        console.log("你好，我是" + this.name);
    }

    static getCount() {
        return Person.count;
    }
}
```

### 6.5 this 关键字

```java
public class Person {
    private String name;

    public Person(String name) {
        this.name = name;  // this 指当前对象
    }

    public void compareWith(Person other) {
        if (this.name.equals(other.name)) {
            System.out.println("同名");
        }
    }
}
```

---

## 7. 继承与多态

### 7.1 继承

```java
// 父类（基类）
public class Animal {
    protected String name;

    public void eat() {
        System.out.println("吃东西");
    }
}

// 子类（派生类）
public class Dog extends Animal {
    public Dog(String name) {
        this.name = name;
    }

    // 子类可以调用父类的方法
    public void bark() {
        System.out.println("汪汪汪");
    }
}

// 使用
Dog dog = new Dog("旺财");
dog.eat();   // 继承自 Animal
dog.bark();  // 自己的方法
```

### 7.2 方法重写（Override）

```java
public class Animal {
    public void makeSound() {
        System.out.println("动物叫");
    }
}

public class Dog extends Animal {
    @Override  // 注解，表示重写父类方法
    public void makeSound() {
        System.out.println("汪汪汪");
    }
}

public class Cat extends Animal {
    @Override
    public void makeSound() {
        System.out.println("喵喵喵");
    }
}

// 使用
Animal a1 = new Dog();
Animal a2 = new Cat();

a1.makeSound();  // 汪汪汪（实际类型是 Dog）
a2.makeSound();  // 喵喵喵（实际类型是 Cat）
```

### 7.3 super 关键字

```java
public class Animal {
    protected String name;

    public Animal(String name) {
        this.name = name;
    }
}

public class Dog extends Animal {
    private String breed;

    public Dog(String name, String breed) {
        super(name);  // 调用父类构造器
        this.breed = breed;
    }
}
```

### 7.4 多态

```java
// 父类引用指向子类对象
Animal animal = new Dog("旺财");

// 可以调用的方法取决于引用类型（Animal）
animal.eat();       // 可以，Animal 有这个方法
// animal.bark();   // 编译错误，Animal 没有 bark 方法

// 类型转换
if (animal instanceof Dog) {
    Dog dog = (Dog) animal;  // 向下转型
    dog.bark();
}
```

---

## 8. 接口与抽象类

### 8.1 接口（Interface）

```java
// 接口定义
public interface Flyable {
    // 常量（默认 public static final）
    int MAX_HEIGHT = 1000;

    // 抽象方法（默认 public abstract）
    void fly();

    // Java 8+ 默认方法
    default void land() {
        System.out.println("降落");
    }

    // Java 8+ 静态方法
    static void info() {
        System.out.println("这是飞行接口");
    }
}

// 实现接口
public class Bird implements Flyable {
    @Override
    public void fly() {
        System.out.println("鸟儿飞翔");
    }
}

// 一个类可以实现多个接口
public class SuperMan implements Flyable, Swimmable {
    @Override
    public void fly() {
        System.out.println("超人飞");
    }

    @Override
    public void swim() {
        System.out.println("超人游泳");
    }
}
```

**JavaScript 对比：**
```javascript
// TypeScript 接口
interface Flyable {
    fly(): void;
    land(): void;
}
```

### 8.2 抽象类（Abstract Class）

```java
// 抽象类
public abstract class Shape {
    protected String color;

    // 构造方法
    public Shape(String color) {
        this.color = color;
    }

    // 抽象方法（没有方法体）
    public abstract double getArea();

    // 普通方法
    public void info() {
        System.out.println("颜色：" + color);
    }
}

// 继承抽象类
public class Circle extends Shape {
    private double radius;

    public Circle(String color, double radius) {
        super(color);
        this.radius = radius;
    }

    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }
}

// 使用
Shape s = new Circle("红色", 5);
s.info();         // 颜色：红色
s.getArea();      // 78.54
```

### 8.3 接口 vs 抽象类

| 特性 | 接口 | 抽象类 |
|------|------|--------|
| 方法实现 | Java 8+ 支持默认方法 | 可以 |
| 构造器 | 不可以 | 可以 |
| 继承数量 | 多继承 | 单继承 |
| 使用场景 | 定义能力 | 代码复用 |

---

## 9. 集合框架

### 9.1 List（列表）

```java
import java.util.ArrayList;
import java.util.List;

// 创建 List
List<String> list = new ArrayList<>();

// 添加元素
list.add("苹果");
list.add("香蕉");
list.add("橙子");

// 访问元素
String first = list.get(0);  // "苹果"

// 修改元素
list.set(1, "葡萄");

// 删除元素
list.remove(0);

// 获取大小
int size = list.size();

// 遍历
for (String fruit : list) {
    System.out.println(fruit);
}

// 包含判断
boolean hasApple = list.contains("苹果");

// 转数组
String[] arr = list.toArray(new String[0]);
```

**JavaScript 对比：**
```javascript
const list = ["苹果", "香蕉", "橙子"];
list.push("葡萄");
list[0];  // "苹果"
list.length;
```

### 9.2 Map（映射）

```java
import java.util.HashMap;
import java.util.Map;

// 创建 Map
Map<String, Integer> map = new HashMap<>();

// 添加键值对
map.put("张三", 25);
map.put("李四", 30);
map.put("王五", 28);

// 获取值
int age = map.get("张三");  // 25

// 删除
map.remove("李四");

// 包含判断
boolean hasKey = map.containsKey("张三");
boolean hasValue = map.containsValue(25);

// 获取大小
int size = map.size();

// 遍历方式 1：entrySet
for (Map.Entry<String, Integer> entry : map.entrySet()) {
    System.out.println(entry.getKey() + ": " + entry.getValue());
}

// 遍历方式 2：keySet
for (String key : map.keySet()) {
    System.out.println(key + ": " + map.get(key));
}
```

**JavaScript 对比：**
```javascript
const map = new Map([["张三", 25], ["李四", 30]]);
// 或普通对象
const obj = { "张三": 25, "李四": 30 };
```

### 9.3 Set（集合）

```java
import java.util.HashSet;
import java.util.Set;

// 创建 Set
Set<String> set = new HashSet<>();

// 添加元素
set.add("A");
set.add("B");
set.add("A");  // 重复，不会添加成功

// 删除元素
set.remove("A");

// 包含判断
boolean hasA = set.contains("A");

// 获取大小
int size = set.size();

// 遍历
for (String item : set) {
    System.out.println(item);
}
```

### 9.4 集合工具类

```java
import java.util.Collections;
import java.util.Arrays;

List<Integer> list = Arrays.asList(3, 1, 4, 1, 5);

// 排序
Collections.sort(list);  // [1, 1, 3, 4, 5]

// 反转
Collections.reverse(list);  // [5, 4, 3, 1, 1]

// 查找最大最小值
int max = Collections.max(list);  // 5
int min = Collections.min(list);  // 1
```

---

## 10. 异常处理

### 10.1 try-catch-finally

```java
try {
    // 可能出错的代码
    int result = 10 / 0;
} catch (ArithmeticException e) {
    // 捕获并处理异常
    System.out.println("除零错误：" + e.getMessage());
} finally {
    // 无论是否异常都会执行
    System.out.println("清理资源");
}
```

**JavaScript 对比：**
```javascript
try {
    const result = 10 / 0;
} catch (e) {
    console.log("错误：" + e.message);
} finally {
    console.log("清理资源");
}
```

### 10.2 多个 catch

```java
try {
    int[] arr = {1, 2, 3};
    int result = arr[5];  // 数组越界
} catch (ArithmeticException e) {
    System.out.println("算术异常");
} catch (ArrayIndexOutOfBoundsException e) {
    System.out.println("数组越界");
} catch (Exception e) {
    // 捕获所有其他异常
    System.out.println("其他异常");
}
```

### 10.3 throws 声明

```java
// 声明可能抛出的异常
public void readFile(String path) throws IOException {
    // 可能抛出 IOException
}

// 调用者必须处理
try {
    readFile("test.txt");
} catch (IOException e) {
    e.printStackTrace();
}
```

### 10.4 自定义异常

```java
// 继承 Exception 创建自定义异常
public class AgeException extends Exception {
    public AgeException(String message) {
        super(message);
    }
}

// 使用
public void setAge(int age) throws AgeException {
    if (age < 0 || age > 150) {
        throw new AgeException("年龄不合法：" + age);
    }
}
```

### 10.5 常见异常

| 异常 | 说明 |
|------|------|
| `NullPointerException` | 空指针异常 |
| `ArrayIndexOutOfBoundsException` | 数组越界 |
| `NumberFormatException` | 数字格式错误 |
| `ClassNotFoundException` | 类不存在 |
| `IOException` | IO 异常 |
| `SQLException` | 数据库异常 |

---

## 11. 泛型

### 11.1 泛型类

```java
// 定义泛型类
public class Box<T> {
    private T content;

    public void set(T content) {
        this.content = content;
    }

    public T get() {
        return content;
    }
}

// 使用
Box<String> stringBox = new Box<>();
stringBox.set("Hello");
String str = stringBox.get();

Box<Integer> intBox = new Box<>();
intBox.set(123);
Integer num = intBox.get();
```

### 11.2 泛型方法

```java
public class Util {
    // 泛型方法
    public static <T> T getMiddle(T[] arr) {
        return arr[arr.length / 2];
    }
}

String[] names = {"A", "B", "C"};
String middle = Util.getMiddle(names);  // "B"
```

### 11.3 泛型通配符

```java
// ? 表示任意类型
public void printList(List<?> list) {
    for (Object item : list) {
        System.out.println(item);
    }
}

// ? extends T：T 或 T 的子类
public double sum(List<? extends Number> list) {
    double total = 0;
    for (Number n : list) {
        total += n.doubleValue();
    }
    return total;
}

// ? super T：T 或 T 的父类
public void addNumbers(List<? super Integer> list) {
    list.add(1);
    list.add(2);
}
```

---

## 12. 常用类

### 12.1 String

```java
String s = "Hello World";

s.length();              // 11
s.charAt(0);             // 'H'
s.substring(0, 5);       // "Hello"
s.indexOf("World");      // 6
s.replace("World", "Java");  // "Hello Java"
s.split(" ");            // ["Hello", "World"]
s.toLowerCase();         // "hello world"
s.toUpperCase();         // "HELLO WORLD"
s.trim();                // 去除首尾空格
s.contains("World");     // true
s.startsWith("Hello");   // true
s.endsWith("World");     // true
```

### 12.2 StringBuilder（可变字符串）

```java
StringBuilder sb = new StringBuilder();

sb.append("Hello");      // 追加
sb.append(" World");
sb.insert(0, "Say: ");   // 插入
sb.reverse();            // 反转
sb.toString();           // 转 String

// 性能对比
String s = "";
for (int i = 0; i < 1000; i++) {
    s += i;  // 每次创建新对象，慢
}

StringBuilder sb = new StringBuilder();
for (int i = 0; i < 1000; i++) {
    sb.append(i);  // 修改原对象，快
}
```

### 12.3 Math

```java
Math.abs(-10);      // 10，绝对值
Math.max(3, 5);     // 5，最大值
Math.min(3, 5);     // 3，最小值
Math.round(3.7);    // 4，四舍五入
Math.floor(3.7);    // 3.0，向下取整
Math.ceil(3.2);     // 4.0，向上取整
Math.random();      // [0,1) 随机数
Math.pow(2, 3);     // 8.0，2 的 3 次方
Math.sqrt(16);      // 4.0，平方根
Math.PI;            // 3.14159...
```

### 12.4 Date 和 SimpleDateFormat

```java
import java.util.Date;
import java.text.SimpleDateFormat;

// 当前时间
Date now = new Date();

// 格式化日期
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
String formatted = sdf.format(now);  // "2024-01-15 10:30:00"

// 解析日期
Date date = sdf.parse("2024-01-15 10:30:00");
```

### 12.5 Arrays

```java
import java.util.Arrays;

int[] arr = {3, 1, 4, 1, 5};

Arrays.sort(arr);           // 排序
Arrays.toString(arr);       // "[1, 1, 3, 4, 5]"
Arrays.binarySearch(arr, 3); // 查找（数组需先排序）
Arrays.fill(arr, 0);        // 填充
Arrays.equals(arr1, arr2);  // 比较
```

### 12.6 包装类

| 基本类型 | 包装类 |
|----------|--------|
| `byte` | `Byte` |
| `int` | `Integer` |
| `double` | `Double` |
| `boolean` | `Boolean` |
| `char` | `Character` |

```java
// 装箱（基本→包装）
int i = 10;
Integer obj = Integer.valueOf(i);  // 或自动装箱：Integer obj = i;

// 拆箱（包装→基本）
int val = obj.intValue();  // 或自动拆箱：int val = obj;

// String 转基本类型
int num = Integer.parseInt("123");
double d = Double.parseDouble("3.14");
boolean b = Boolean.parseBoolean("true");

// 基本类型转 String
String s1 = String.valueOf(123);
String s2 = "" + 123;
```

---

## 附录：快速参考

### 前端→后端术语对照

| 前端术语 | Java 对应 |
|----------|-----------|
| 函数 | 方法 |
| 对象属性 | 字段/成员变量 |
| 模块 | 包（Package） |
| import | import |
| 类方法 | static 方法 |
| 实例方法 | 非 static 方法 |
| 构造函数 | 构造方法 |
| 原型链 | 继承链 |
| 回调函数 | 接口实现 |
| Promise | CompletableFuture |
| async/await | 线程池 + 回调 |

### 常用快捷键（IDEA）

- `Ctrl + N`：查找类
- `Ctrl + Shift + N`：查找文件
- `Ctrl + F`：查找
- `Ctrl + R`：替换
- `Ctrl + B`：跳转到定义
- `Alt + Insert`：生成代码（Getter/Setter 等）
- `Ctrl + Alt + L`：格式化代码
- `Ctrl + /`：注释
- `Shift + Shift`：搜索所有

---

## 练习建议

1. **第一天**：变量、运算符、控制流程
2. **第二天**：数组、方法
3. **第三天**：类与对象
4. **第四天**：继承、接口
5. **第五天**：集合框架
6. **第六天**：异常、泛型
7. **第七天**：综合练习

每个知识点都要动手写代码，不要只看！
