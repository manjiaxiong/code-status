/**
 * for 循环
 * 运行：javac -encoding UTF-8 ForLoop.java && java ForLoop
 */
public class ForLoop {
    public static void main(String[] args) {
        // ===== 基本 for 循环 =====
        System.out.println("=== 基本 for 循环 ===");
        for (int i = 0; i < 5; i++) {
            System.out.println("第 " + i + " 次循环");
        }

        // ===== 计算 1 到 100 的和 =====
        System.out.println("\n=== 计算 1+2+...+100 ===");
        int sum = 0;
        for (int i = 1; i <= 100; i++) {
            sum += i;
        }
        System.out.println("结果：" + sum);

        // ===== 计算阶乘 5! =====
        System.out.println("\n=== 计算 5! ===");
        int factorial = 1;
        for (int i = 1; i <= 5; i++) {
            factorial *= i;
            System.out.println(i + "! = " + factorial);
        }

        // ===== 打印九九乘法表 =====
        System.out.println("\n=== 九九乘法表 ===");
        for (int i = 1; i <= 9; i++) {
            for (int j = 1; j <= i; j++) {
                System.out.print(j + "×" + i + "=" + (i * j) + "  ");
            }
            System.out.println();
        }

        // ===== 增强 for 循环（遍历数组）=====
        System.out.println("\n=== 增强 for 循环 ===");
        int[] numbers = {1, 2, 3, 4, 5};

        System.out.println("遍历数组：");
        for (int num : numbers) {
            System.out.println(num);
        }

        String[] fruits = {"苹果", "香蕉", "橙子"};
        System.out.println("\n遍历字符串数组：");
        for (String fruit : fruits) {
            System.out.println(fruit);
        }

        // ===== break 和 continue =====
        System.out.println("\n=== break ===");
        for (int i = 0; i < 10; i++) {
            if (i == 5) {
                break;  // 跳出整个循环
            }
            System.out.println("i = " + i);
        }

        System.out.println("\n=== continue ===");
        for (int i = 0; i < 5; i++) {
            if (i == 2) {
                continue;  // 跳过本次循环
            }
            System.out.println("i = " + i);
        }

        // ===== 练习 =====
        // 1. 计算 1 到 100 之间所有偶数的和
        // 2. 打印 100-999 之间的所有水仙花数（各位数字立方和等于自身）
        // 3. 打印一个金字塔图案
    }
}
