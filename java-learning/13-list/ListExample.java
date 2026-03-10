/**
 * List 集合
 * 运行：javac -encoding UTF-8 ListExample.java && java ListExample
 *
 * List 特点：
 * 1. 有序（元素按插入顺序存储）
 * 2. 可重复
 * 3. 可通过索引访问
 *
 * 常用实现类：ArrayList, LinkedList
 */
import java.util.ArrayList;      // 动态数组
import java.util.LinkedList;     // 链表
import java.util.List;           // List 接口

public class ListExample {
    public static void main(String[] args) {
        // ===== 创建 List =====
        System.out.println("=== 创建 List ===");
        // 使用接口引用指向实现类对象（推荐）
        List<String> list = new ArrayList<>();

        // ===== 添加元素 =====
        System.out.println("\n=== 添加元素 ===");
        list.add("苹果");      // 添加到末尾
        list.add("香蕉");
        list.add("橙子");
        System.out.println("添加后：" + list);

        // 在指定位置添加
        list.add(1, "葡萄");   // 在索引 1 的位置添加
        System.out.println("在索引 1 添加葡萄后：" + list);

        // ===== 访问元素 =====
        System.out.println("\n=== 访问元素 ===");
        String first = list.get(0);           // 获取索引 0 的元素
        System.out.println("第一个元素：" + first);

        String third = list.get(2);
        System.out.println("第三个元素：" + third);

        // ===== 修改元素 =====
        System.out.println("\n=== 修改元素 ===");
        list.set(0, "青苹果");  // 修改索引 0 的元素
        System.out.println("修改后：" + list);

        // ===== 删除元素 =====
        System.out.println("\n=== 删除元素 ===");
        list.remove("橙子");      // 删除指定元素
        System.out.println("删除'橙子'后：" + list);

        list.remove(0);           // 删除指定索引的元素
        System.out.println("删除索引 0 后：" + list);

        // ===== 获取大小 =====
        System.out.println("\n=== 获取大小 ===");
        int size = list.size();
        System.out.println("List 大小：" + size);

        // ===== 判断是否包含 =====
        System.out.println("\n=== 判断 ===");
        boolean hasBanana = list.contains("香蕉");
        System.out.println("是否包含香蕉：" + hasBanana);

        boolean hasOrange = list.contains("橙子");
        System.out.println("是否包含橙子：" + hasOrange);

        // ===== 遍历 List =====
        System.out.println("\n=== 遍历 List ===");

        // 方式 1：普通 for 循环
        System.out.print("普通 for 循环：");
        for (int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i) + " ");
        }
        System.out.println();

        // 方式 2：增强 for 循环（推荐）
        System.out.print("增强 for 循环：");
        for (String fruit : list) {
            System.out.print(fruit + " ");
        }
        System.out.println();

        // ===== 其他常用方法 =====
        System.out.println("\n=== 其他方法 ===");
        System.out.println("列表是否为空：" + list.isEmpty());

        list.clear();  // 清空列表
        System.out.println("清空后大小：" + list.size());
        System.out.println("清空后是否为空：" + list.isEmpty());

        // ===== 数字 List =====
        System.out.println("\n=== 数字 List ===");
        List<Integer> numbers = new ArrayList<>();
        numbers.add(1);
        numbers.add(2);
        numbers.add(3);
        numbers.add(4);
        numbers.add(5);
        System.out.println("数字列表：" + numbers);

        // 计算总和
        int sum = 0;
        for (int num : numbers) {
            sum += num;
        }
        System.out.println("总和：" + sum);

        // ===== LinkedList =====
        System.out.println("\n=== LinkedList ===");
        List<String> linkedList = new LinkedList<>();
        linkedList.add("A");
        linkedList.add("B");
        linkedList.add("C");
        System.out.println("LinkedList:" + linkedList);

        // LinkedList 特有方法（添加在头部）
        ((LinkedList<String>) linkedList).addFirst("Head");
        ((LinkedList<String>) linkedList).addLast("Tail");
        System.out.println("添加头尾后：" + linkedList);

        // ===== 练习 =====
        // 1. 创建一个 List 存储 5 个学生的姓名
        // 2. 找出 List 中的最大值（数字 List）
        // 3. 将 List 反转（不使用 Collections.reverse）
    }
}
