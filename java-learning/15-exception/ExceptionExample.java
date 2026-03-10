/**
 * 异常处理（Exception）
 * 运行：javac -encoding UTF-8 ExceptionExample.java && java ExceptionExample
 *
 * 异常分类：
 * 1. Error：系统错误，无法处理（如 OutOfMemoryError）
 * 2. Exception：程序错误，可以处理
 *    - 受检异常（Checked）：必须处理（如 IOException）
 *    - 非受检异常（Unchecked）：可选处理（如 NullPointerException）
 */

// 自定义异常类
class AgeException extends Exception {
    public AgeException(String message) {
        super(message);  // 调用父类构造器
    }
}

public class ExceptionExample {
    public static void main(String[] args) {
        // ===== try-catch 基础 =====
        System.out.println("=== try-catch 基础 ===");
        try {
            int result = 10 / 0;  // 这里会抛出 ArithmeticException
            System.out.println("结果是：" + result);  // 不会执行
        } catch (ArithmeticException e) {
            System.out.println("捕获到异常：除数不能为零！");
            System.out.println("异常信息：" + e.getMessage());
        }

        // ===== 多个 catch =====
        System.out.println("\n=== 多个 catch ===");
        try {
            int[] arr = {1, 2, 3};
            System.out.println(arr[5]);  // 这里会抛出 ArrayIndexOutOfBoundsException
        } catch (ArithmeticException e) {
            System.out.println("算术异常");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("数组越界异常！");
            System.out.println("异常信息：" + e.getMessage());
        } catch (Exception e) {
            // 捕获所有其他异常
            System.out.println("其他异常");
        }

        // ===== finally 块 =====
        System.out.println("\n=== finally 块 ===");
        try {
            System.out.println("try 块：可能出错的代码");
            int a = 10 / 2;  // 不会出错
            System.out.println("计算结果：" + a);
        } catch (Exception e) {
            System.out.println("捕获异常");
        } finally {
            // finally 无论是否异常都会执行
            // 通常用于释放资源（关闭文件、数据库连接等）
            System.out.println("finally 块：清理资源");
        }

        // ===== finally 在异常时也会执行 =====
        System.out.println("\n=== finally 在异常时也会执行 ===");
        try {
            System.out.println("try 块：10 / 0");
            int b = 10 / 0;  // 出错
        } catch (ArithmeticException e) {
            System.out.println("捕获到异常");
        } finally {
            System.out.println("finally 块：即使异常也会执行");
        }

        // ===== 常见异常示例 =====
        System.out.println("\n=== 常见异常 ===");

        // NullPointerException
        try {
            String str = null;
            System.out.println(str.length());  // 空指针异常
        } catch (NullPointerException e) {
            System.out.println("空指针异常：对象为 null");
        }

        // NumberFormatException
        try {
            int num = Integer.parseInt("abc");  // 数字格式异常
        } catch (NumberFormatException e) {
            System.out.println("数字格式异常：'abc' 不是有效数字");
        }

        // ===== 自定义异常 =====
        System.out.println("\n=== 自定义异常 ===");
        try {
            setAge(-5);  // 传入负数，抛出自定义异常
        } catch (AgeException e) {
            System.out.println("捕获自定义异常：" + e.getMessage());
        }

        // ===== throws 声明异常 =====
        System.out.println("\n=== throws 声明异常 ===");
        try {
            readFile("不存在的文件.txt");
        } catch (Exception e) {
            System.out.println("读取文件失败：" + e.getMessage());
        }

        // ===== throw 抛出异常 =====
        System.out.println("\n=== throw 抛出异常 ===");
        checkAge(150);  // 传入过大年龄
        checkAge(25);   // 正常年龄

        System.out.println("\n程序继续执行...");
    }

    // ===== 自定义异常方法 =====
    public static void setAge(int age) throws AgeException {
        if (age < 0 || age > 150) {
            throw new AgeException("年龄不合法：" + age);
        }
        System.out.println("年龄设置成功：" + age);
    }

    // ===== throws 声明异常 =====
    public static void readFile(String filename) throws Exception {
        // 模拟读取文件
        if (!filename.equals("存在的文件.txt")) {
            throw new Exception("文件不存在：" + filename);
        }
        System.out.println("文件读取成功");
    }

    // ===== throw 抛出异常 =====
    public static void checkAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("年龄不能为负数");
        }
        if (age > 120) {
            throw new IllegalArgumentException("年龄不能超过 120 岁");
        }
        System.out.println("年龄验证通过：" + age);
    }

    // ===== 练习 =====
    // 1. 编写一个方法，计算两个数相除，处理除零异常
    // 2. 编写一个方法，根据索引获取数组元素，处理数组越界异常
    // 3. 创建自定义异常 InsufficientFundsException（余额不足）
}
