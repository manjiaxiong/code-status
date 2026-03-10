/**
 * if-else 条件判断
 * 运行：javac -encoding UTF-8 IfElse.java && java IfElse
 */
public class IfElse {
    public static void main(String[] args) {
        // ===== 基本 if-else =====
        int score = 85;

        System.out.println("分数：" + score);

        if (score >= 90) {
            System.out.println("优秀");
        } else if (score >= 60) {
            System.out.println("及格");
        } else {
            System.out.println("不及格");
        }

        // ===== 多个条件 =====
        int age = 18;
        boolean hasTicket = true;

        System.out.println("\n年龄：" + age + ", 有票：" + hasTicket);

        if (age >= 18 && hasTicket) {
            System.out.println("可以入场");
        } else if (age < 18) {
            System.out.println("未成年人禁止入场");
        } else {
            System.out.println("请先购票");
        }

        // ===== 三元运算符 =====
        System.out.println("\n=== 三元运算符 ===");
        int num = 10;
        String result = (num % 2 == 0) ? "偶数" : "奇数";
        System.out.println(num + " 是 " + result);

        // 比较两个数的大小
        int x = 100;
        int y = 200;
        int max = (x > y) ? x : y;
        System.out.println("较大的数是：" + max);

        // ===== 嵌套 if =====
        System.out.println("\n=== 嵌套 if ===");
        int hour = 14;
        boolean isWeekend = false;

        if (hour >= 0 && hour < 12) {
            System.out.print("上午好");
            if (!isWeekend) {
                System.out.println("，该上班了");
            } else {
                System.out.println("，休息一下吧");
            }
        } else if (hour >= 12 && hour < 18) {
            System.out.print("下午好");
            if (!isWeekend) {
                System.out.println("，继续工作");
            } else {
                System.out.println("，享受周末");
            }
        } else {
            System.out.println("晚上好，该休息了");
        }

        // ===== 练习 =====
        // 1. 判断一个年份是否是闰年
        //    条件：能被 4 整除但不能被 100 整除，或者能被 400 整除
        // 2. 根据分数输出等级（A:90+, B:80-89, C:70-79, D:60-69, F:<60）
    }
}
