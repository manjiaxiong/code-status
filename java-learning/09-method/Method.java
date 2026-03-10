/**
 * 方法
 * 运行：javac -encoding UTF-8 Method.java && java Method
 */
public class Method {
    public static void main(String[] args) {
        // ===== 调用无参方法 =====
        System.out.println("=== 无参方法 ===");
        sayHello();

        // ===== 调用有参方法 =====
        System.out.println("\n=== 有参方法 ===");
        sayHelloTo("张三");
        sayHelloTo("李四");

        // ===== 调用有返回值的方法 =====
        System.out.println("\n=== 有返回值的方法 ===");
        int result = add(10, 20);
        System.out.println("10 + 20 = " + result);

        System.out.println("5 + 3 = " + add(5, 3));

        // ===== 方法重载 =====
        System.out.println("\n=== 方法重载 ===");
        System.out.println("add(1, 2) = " + add(1, 2));           // int + int
        System.out.println("add(1.5, 2.5) = " + add(1.5, 2.5));   // double + double
        System.out.println("add(1, 2, 3) = " + add(1, 2, 3));     // 三个参数
        System.out.println("add(\"Hello\", \"World\") = " + add("Hello", "World"));

        // ===== 可变参数 =====
        System.out.println("\n=== 可变参数 ===");
        System.out.println("sum(1) = " + sum(1));
        System.out.println("sum(1, 2, 3) = " + sum(1, 2, 3));
        System.out.println("sum(1, 2, 3, 4, 5) = " + sum(1, 2, 3, 4, 5));

        // ===== 递归 =====
        System.out.println("\n=== 递归 ===");
        System.out.println("5! = " + factorial(5));
        System.out.println("fibonacci(10) = " + fibonacci(10));
    }

    // 无参无返回值方法
    public static void sayHello() {
        System.out.println("Hello!");
    }

    // 有参无返回值方法
    public static void sayHelloTo(String name) {
        System.out.println("你好，" + name + "!");
    }

    // 有参有返回值方法
    public static int add(int a, int b) {
        return a + b;
    }

    // ===== 方法重载（方法名相同，参数不同）=====
    public static double add(double a, double b) {
        return a + b;
    }

    public static int add(int a, int b, int c) {
        return a + b + c;
    }

    public static String add(String a, String b) {
        return a + b;
    }

    // ===== 可变参数 =====
    public static int sum(int... numbers) {
        int total = 0;
        for (int n : numbers) {
            total += n;
        }
        return total;
    }

    // ===== 递归：阶乘 =====
    public static int factorial(int n) {
        if (n == 1) {
            return 1;
        }
        return n * factorial(n - 1);
    }

    // ===== 递归：斐波那契数列 =====
    public static int fibonacci(int n) {
        if (n <= 1) {
            return n;
        }
        return fibonacci(n - 1) + fibonacci(n - 2);
    }

    // ===== 练习 =====
    // 1. 写一个方法判断一个数是否是素数
    // 2. 写一个方法找出三个数的最大值
    // 3. 写一个方法打印星号三角形
}
