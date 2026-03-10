/**
 * Map 集合
 * 运行：javac -encoding UTF-8 MapExample.java && java MapExample
 *
 * Map 特点：
 * 1. 存储键值对（key-value）
 * 2. key 不可重复，value 可重复
 * 3. 通过 key 获取 value
 *
 * 常用实现类：HashMap, TreeMap, LinkedHashMap
 */
import java.util.HashMap;        // 哈希表实现
import java.util.Map;            // Map 接口
import java.util.Set;            // Set 接口

public class MapExample {
    public static void main(String[] args) {
        // ===== 创建 Map =====
        System.out.println("=== 创建 Map ===");
        // key 是 String，value 是 Integer
        Map<String, Integer> map = new HashMap<>();

        // ===== 添加键值对 =====
        System.out.println("\n=== 添加键值对 ===");
        map.put("张三", 25);      // 添加 key-value
        map.put("李四", 30);
        map.put("王五", 28);
        System.out.println("添加后：" + map);

        // ===== 获取值 =====
        System.out.println("\n=== 获取值 ===");
        Integer age = map.get("张三");
        System.out.println("张三的年龄：" + age);

        Integer liAge = map.get("李四");
        System.out.println("李四的年龄：" + liAge);

        // 获取不存在的 key，返回 null
        Integer zhaoAge = map.get("赵六");
        System.out.println("赵六的年龄：" + zhaoAge);  // null

        // ===== 修改值 =====
        System.out.println("\n=== 修改值 ===");
        map.put("张三", 26);  // key 已存在，更新 value
        System.out.println("修改后张三的年龄：" + map.get("张三"));

        // ===== 删除键值对 =====
        System.out.println("\n=== 删除键值对 ===");
        map.remove("王五");   // 删除 key 为"王五"的键值对
        System.out.println("删除后：" + map);

        // ===== 获取大小 =====
        System.out.println("\n=== 获取大小 ===");
        int size = map.size();
        System.out.println("Map 大小：" + size);

        // ===== 判断是否包含 =====
        System.out.println("\n=== 判断 ===");
        boolean hasKey = map.containsKey("张三");
        System.out.println("是否包含 key'张三'：" + hasKey);

        boolean hasValue = map.containsValue(25);
        System.out.println("是否包含 value 25：" + hasValue);

        // ===== 遍历 Map（方式 1：entrySet）=====
        System.out.println("\n=== 遍历 Map（方式 1：entrySet）===");
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }

        // ===== 遍历 Map（方式 2：keySet）=====
        System.out.println("\n=== 遍历 Map（方式 2：keySet）===");
        Set<String> keys = map.keySet();
        for (String key : keys) {
            System.out.println(key + " = " + map.get(key));
        }

        // ===== 获取所有 key 和所有 value =====
        System.out.println("\n=== 获取所有 key 和 value ===");
        System.out.println("所有 key：" + map.keySet());

        // 注意：HashMap 不保证顺序
        System.out.println("所有 value：" + map.values());

        // ===== 其他常用方法 =====
        System.out.println("\n=== 其他方法 ===");
        System.out.println("是否为空：" + map.isEmpty());

        map.clear();  // 清空
        System.out.println("清空后大小：" + map.size());

        // ===== 实际应用：统计词频 =====
        System.out.println("\n=== 词频统计 ===");
        String text = "apple banana apple orange banana apple";
        String[] words = text.split(" ");

        Map<String, Integer> wordCount = new HashMap<>();
        for (String word : words) {
            // 如果 key 不存在，先放入 0，然后 +1
            // 如果 key 已存在，获取 value 然后 +1
            wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
        }
        System.out.println("原文：" + text);
        System.out.println("词频统计：" + wordCount);

        // ===== 实际应用：学生成绩表 =====
        System.out.println("\n=== 学生成绩表 ===");
        Map<String, Double> scores = new HashMap<>();
        scores.put("语文", 85.4);
        scores.put("数学", 92.09);
        scores.put("英语", 78.0258);

        System.out.println("成绩单：");
        for (Map.Entry<String, Double> entry : scores.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + "分");
        }

        // 计算平均分
        double total = 0;
        for (Double score : scores.values()) {
            total += score;
        }
        // double average = total / scores.size();
        // System.out.println("平均分：" + average);
        double average = total / scores.size();
        System.out.println("平均分1：" + average);
        System.out.println("平均分2：" + String.format("%.3f", average) + "," + average);


        // ===== 练习 =====
        // 1. 用 Map 存储 3 个朋友的电话号码（姓名->电话）
        // 2. 统计一句话中每个字符出现的次数
        // 3. 用 Map 实现一个简单的字典（英文单词->中文意思）
    }
}
