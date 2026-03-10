/**
 * switch 语句
 * 运行：javac -encoding UTF-8 Switch.java && java Switch
 */
public class Switch {
    public static void main(String[] args) {
        // ===== 基本 switch =====
        int day = 3;

        System.out.println("=== 数字 switch ===");
        System.out.println("day = " + day);

        switch (day) {
            case 1:
                System.out.println("星期一");
                break;
            case 2:
                System.out.println("星期二");
                break;
            case 3:
                System.out.println("星期三");
                break;
            case 4:
                System.out.println("星期四");
                break;
            case 5:
                System.out.println("星期五");
                break;
            case 6:
            case 7:
                System.out.println("周末");
                break;
            default:
                System.out.println("无效的日期");
        }

        // ===== String 类型的 switch（Java 7+）=====
        System.out.println("\n=== String switch ===");
        String fruit = "apple";

        switch (fruit) {
            case "apple":
                System.out.println("苹果 - ¥5");
                break;
            case "banana":
                System.out.println("香蕉 - ¥3");
                break;
            case "orange":
                System.out.println("橙子 - ¥4");
                break;
            default:
                System.out.println("暂无此水果");
        }

        // ===== 穿透效果（不使用 break）=====
        System.out.println("\n=== 穿透效果 ===");
        int month = 3;
        String season;

        switch (month) {
            case 3:
            case 4:
            case 5:
                season = "春季";
                break;
            case 6:
            case 7:
            case 8:
                season = "夏季";
                break;
            case 9:
            case 10:
            case 11:
                season = "秋季";
                break;
            case 12:
            case 1:
            case 2:
                season = "冬季";
                break;
            default:
                season = "未知";
        }
        System.out.println(month + "月是" + season);

        // ===== 练习 =====
        // 1. 输入 1-7，输出对应的英文星期（Monday, Tuesday...）
        // 2. 输入成绩等级（A/B/C/D/F），输出对应分数段
    }
}
