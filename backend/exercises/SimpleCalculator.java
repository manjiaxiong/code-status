/**
 * 练习 1：实现一个简单计算器
 * 要求：实现加、减、乘、除四个方法
 */
public class SimpleCalculator {

    // TODO: 实现加法方法
    public int add(int a, int b) {
        return a + b;
    }

    // TODO: 实现减法方法
    public int subtract(int a, int b) {
        return a - b;
    }

    // TODO: 实现乘法方法
    public int multiply(int a, int b) {
        return a * b;
    }

    // TODO: 实现除法方法
    public double divide(int a, int b) {
        if (b == 0) {
            throw new ArithmeticException("除数不能为零");
        }
        return (double) a / b;
    }

    // 测试方法
    public static void main(String[] args) {
        SimpleCalculator calc = new SimpleCalculator();

        // 测试加法
        System.out.println("5 + 3 = aaaa " + calc.add(5, 3)); // 期望输出：8

        // 测试减法
        System.out.println("10 - 4 = " + calc.subtract(10, 4)); // 期望输出：6

        // 测试乘法
        System.out.println("6 * 7 = " + calc.multiply(6, 7)); // 期望输出：42

        // 测试除法
        System.out.println("20 / 4 = " + calc.divide(20, 4)); // 期望输出：5.0

        System.out.println("\n完成所有测试！");
    }
}
