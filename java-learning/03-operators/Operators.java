/**
 * 运算符
 * 运行：javac -encoding UTF-8 Operators.java && java Operators
 */
public class Operators {
    public static void main(String[] args) {
        // ===== 算术运算符 =====
        int a = 10;
        int b = 3;

        System.out.println("=== 算术运算符 ===");
        System.out.println("a = " + a + ", b = " + b);
        System.out.println("a + b = " + (a + b));    // 13
        System.out.println("a - b = " + (a - b));    // 7
        System.out.println("a * b = " + (a * b));    // 30
        System.out.println("a / b = " + (a / b));    // 3 (整数除法！)
        System.out.println("a % b = " + (a % b));    // 1 (取余)

        // 注意：整数除法
        System.out.println("\n注意：5 / 2 = " + (5 / 2));      // 2，不是 2.5！
        System.out.println("注意：5.0 / 2 = " + (5.0 / 2));    // 2.5

        // ===== 自增自减 =====
        System.out.println("\n=== 自增自减 ===");
        int x = 5;
        System.out.println("x = " + x);
        x++;
        System.out.println("x++ 后 x = " + x);    // 6
        x--;
        System.out.println("x-- 后 x = " + x);    // 5

        // ++x 和 x++ 的区别
        int m = 5;
        int n = ++m;  // 先加后用，m=6, n=6
        System.out.println("++m: m=" + m + ", n=" + n);

        int p = 5;
        int q = p++;  // 先用后加，p=6, q=5
        System.out.println("p++: p=" + p + ", q=" + q);

        // ===== 比较运算符 =====
        System.out.println("\n=== 比较运算符 ===");
        System.out.println("10 == 3: " + (10 == 3));   // false
        System.out.println("10 != 3: " + (10 != 3));   // true
        System.out.println("10 > 3: " + (10 > 3));     // true
        System.out.println("10 < 3: " + (10 < 3));     // false
        System.out.println("10 >= 10: " + (10 >= 10)); // true
        System.out.println("10 <= 3: " + (10 <= 3));   // false

        // ===== 逻辑运算符 =====
        System.out.println("\n=== 逻辑运算符 ===");
        System.out.println("true && false: " + (true && false));  // false
        System.out.println("true || false: " + (true || false));  // true
        System.out.println("!true: " + (!true));                   // false

        // 短路特性
        System.out.println("\n=== 短路特性 ===");
        int val = 0;
        if (false && ++val > 0) {
            // 不会执行
        }
        System.out.println("val = " + val);  // 0，因为 && 左边是 false，右边不执行

        // ===== 练习 =====
        // 1. 计算圆的面积（半径 r=5，面积=π*r*r）
        // 2. 判断一个数是否同时大于 10 且小于 20
    }
}
