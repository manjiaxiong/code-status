/**
 * String 字符串常用操作
 * 运行：javac -encoding UTF-8 StringExample.java && java StringExample
 *
 * String 特点：
 * 1. 不可变（一旦创建，内容不能修改）
 * 2. 使用双引号创建
 * 3. 可以使用 + 连接字符串
 */

public class StringExample {
    public static void main(String[] args) {
        // ===== 创建字符串 =====
        System.out.println("=== 创建字符串 ===");
        String s1 = "Hello";           // 字面量方式（推荐）
        String s2 = new String("World");  // new 对象方式
        System.out.println("s1: " + s1);
        System.out.println("s2: " + s2);

        // ===== 字符串连接 =====
        System.out.println("\n=== 字符串连接 ===");
        String fullName = "Hello" + " " + "World";
        System.out.println(fullName);

        String name = "张三";
        int age = 25;
        String info = "我叫" + name + "，今年" + age + "岁";
        System.out.println(info);

        // ===== 常用方法：获取长度 =====
        System.out.println("\n=== 获取长度 ===");
        String str = "Hello World";
        System.out.println("字符串长度：" + str.length());  // 11

        // ===== 常用方法：字符访问 =====
        System.out.println("\n=== 字符访问 ===");
        char firstChar = str.charAt(0);  // 获取索引 0 的字符
        System.out.println("第一个字符：" + firstChar);  // H

        // ===== 常用方法：子字符串 =====
        System.out.println("\n=== 子字符串 ===");
        String sub1 = str.substring(0, 5);  // 从 0 到 5（不包含 5）
        System.out.println("substring(0, 5): " + sub1);  // Hello

        String sub2 = str.substring(6);  // 从 6 到末尾
        System.out.println("substring(6): " + sub2);  // World

        // ===== 常用方法：查找 =====
        System.out.println("\n=== 查找 ===");
        int index = str.indexOf("World");  // 查找子串位置
        System.out.println("'World' 的位置：" + index);  // 6

        boolean contains = str.contains("World");  // 是否包含
        System.out.println("是否包含'World'：" + contains);  // true

        boolean startsWith = str.startsWith("Hello");  // 是否开头
        System.out.println("是否以'Hello'开头：" + startsWith);  // true

        boolean endsWith = str.endsWith("World");  // 是否结尾
        System.out.println("是否以'World'结尾：" + endsWith);  // true

        // ===== 常用方法：替换 =====
        System.out.println("\n=== 替换 ===");
        String replaced = str.replace("World", "Java");
        System.out.println("替换后：" + replaced);  // Hello Java

        String original = "Hello World World";
        String replacedAll = original.replace("World", "Java");  // 替换所有
        System.out.println("替换所有：" + replacedAll);  // Hello Java Java

        // ===== 常用方法：分割 =====
        System.out.println("\n=== 分割 ===");
        String csv = "apple,banana,orange";
        String[] fruits = csv.split(",");  // 按逗号分割
        System.out.println("分割结果：");
        for (String fruit : fruits) {
            System.out.println("  " + fruit);
        }

        // ===== 常用方法：大小写转换 =====
        System.out.println("\n=== 大小写转换 ===");
        String upper = "hello";
        String lower = "WORLD";
        System.out.println(upper.toUpperCase());  // HELLO
        System.out.println(lower.toLowerCase());  // world

        // ===== 常用方法：去除空格 =====
        System.out.println("\n=== 去除空格 ===");
        String spaces = "  Hello   World  ";
        System.out.println("原字符串：'" + spaces + "'");
        System.out.println("去除首尾空格：'" + spaces.trim() + "'");

        // ===== 常用方法：比较字符串 =====
        System.out.println("\n=== 字符串比较 ===");
        String a = "Hello";
        String b = "Hello";
        String c = new String("Hello");

        System.out.println("a == b: " + (a == b));              // true（同一对象）
        System.out.println("a == c: " + (a == c));              // 可能 false（不同对象）
        System.out.println("a.equals(c): " + a.equals(c));      // true（内容相同）

        // 注意：比较字符串内容要用 equals，不要用 ==
        String pwd1 = "123456";
        String pwd2 = new String("123456");
        System.out.println("\n密码比较：");
        System.out.println("pwd1 == pwd2: " + (pwd1 == pwd2));      // false
        System.out.println("pwd1.equals(pwd2): " + pwd1.equals(pwd2));  // true

        // ===== 常用方法：判断是否为空 =====
        System.out.println("\n=== 判断空字符串 ===");
        String empty = "";
        String notEmpty = "Hello";
        System.out.println("empty 是否为空字符串：" + empty.isEmpty());  // true
        System.out.println("notEmpty 是否为空字符串：" + notEmpty.isEmpty());  // false

        // ===== StringBuilder（可变字符串）=====
        System.out.println("\n=== StringBuilder ===");
        StringBuilder sb = new StringBuilder();
        sb.append("Hello");      // 追加
        sb.append(" ");
        sb.append("World");
        System.out.println("StringBuilder: " + sb.toString());

        sb.insert(0, "Say: ");   // 插入
        System.out.println("插入后：" + sb.toString());

        sb.reverse();            // 反转
        System.out.println("反转后：" + sb.toString());

        // ===== 性能对比 =====
        System.out.println("\n=== 性能对比 ===");
        // String 拼接（慢，因为每次创建新对象）
        long start1 = System.currentTimeMillis();
        String s = "";
        for (int i = 0; i < 100000; i++) {
            s += i;
        }
        long end1 = System.currentTimeMillis();
        System.out.println("String 拼接 1000 次耗时：" + (end1 - start1) + "ms");

        // StringBuilder 拼接（快，因为修改原对象）
        long start2 = System.currentTimeMillis();
        StringBuilder sb2 = new StringBuilder();
        for (int i = 0; i < 100000; i++) {
            sb2.append(i);
        }
        long end2 = System.currentTimeMillis();
        System.out.println("StringBuilder 拼接 1000 次耗时：" + (end2 - start2) + "ms");

        // ===== 练习 =====
        // 1. 判断一个字符串是否是回文（正读反读都一样）
        // 2. 统计字符串中每个字符出现的次数
        // 3. 将字符串反转
    }
}
