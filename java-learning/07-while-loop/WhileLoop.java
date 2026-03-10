/**
 * while 循环
 * 运行：javac -encoding UTF-8 WhileLoop.java && java WhileLoop
 */
public class WhileLoop {
    public static void main(String[] args) {
        // ===== 基本 while 循环 =====
        System.out.println("=== while 循环 ===");
        int i = 0;
        while (i < 5) {
            System.out.println("i = " + i);
            i++;
        }

        // ===== do-while 循环（先执行后判断）=====
        System.out.println("\n=== do-while 循环 ===");
        int j = 0;
        do {
            System.out.println("j = " + j);
            j++;
        } while (j < 5);

        // do-while 至少执行一次
        int k = 10;
        do {
            System.out.println("这行会执行，虽然 k=10 不满足 k<5");
        } while (k < 5);

        // ===== while 实现猜数字 =====
        System.out.println("\n=== 猜数字游戏 ===");
        int target = 7;  // 目标数字
        int guess = 1;   // 当前猜测

        while (guess <= target) {
            System.out.println("正在猜第 " + guess + " 次...");
            if (guess == target) {
                System.out.println("猜对了！答案是 " + target);
                break;
            }
            guess++;
        }

        // ===== 斐波那契数列 =====
        System.out.println("\n=== 斐波那契数列 ===");
        int a = 1, b = 1;
        int count = 0;
        int max = 10;  // 打印前 10 个数

        System.out.print(a + " " + b + " ");
        while (count < max - 2) {
            int next = a + b;
            System.out.print(next + " ");
            a = b;
            b = next;
            count++;
        }
        System.out.println();

        // ===== 练习 =====
        // 1. 用 while 循环计算 1+2+...+100
        // 2. 用 do-while 实现：让用户输入密码，最多尝试 3 次
        // 3. 用 while 找出 100 以内所有素数
    }
}
