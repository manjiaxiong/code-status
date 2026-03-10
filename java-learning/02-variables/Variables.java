/**
 * 变量与数据类型
 * 运行：javac -encoding UTF-8 Variables.java && java Variables
 */
public class Variables {
    public static void main(String[] args) {
        // ===== 基本数据类型 =====

        // int - 整数类型
        int age = 25;
        System.out.println("年龄：" + age);

        // double - 小数类型
        double salary = 5000.50;
        System.out.println("工资：" + salary);

        // float - 单精度浮点（需要加 f 后缀）
        float pi = 3.14f;
        System.out.println("圆周率：" + pi);

        // long - 长整数（需要加 l 或 L 后缀）
        long population = 1400000000L;
        System.out.println("人口：" + population);

        // char - 字符（单引号）
        char grade = 'A';
        System.out.println("等级：" + grade);

        // boolean - 布尔
        boolean isWorking = true;
        System.out.println("是否工作：" + isWorking);

        // ===== 引用数据类型 =====

        // String - 字符串（双引号）
        String name = "张三";
        System.out.println("姓名：" + name);

        // ===== 类型转换 =====

        // 自动转换（小范围→大范围）
        int a = 100;
        double b = a;  // int 自动转 double
        System.out.println("自动转换：" + b);

        // 强制转换（大范围→小范围）
        double c = 3.99;
        int d = (int) c;  // 需要强制转换，会丢失小数部分
        System.out.println("强制转换：" + d);  // 输出 3

        // ===== 练习 =====
        // 1. 创建一个变量存储你的身高（小数）
        // 2. 创建一个变量存储你的城市名称
        // 3. 打印这些信息
    }
}
