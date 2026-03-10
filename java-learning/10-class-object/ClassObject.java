/**
 * 类与对象
 * 运行：javac -encoding UTF-8 ClassObject.java && java ClassObject
 */

// 定义一个类
class Person {
    // 属性（成员变量/字段）
    String name;    // 姓名
    int age;        // 年龄

    // 构造方法（用于创建对象）
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // 方法（行为）
    public void sayHello() {
        System.out.println("你好，我是" + name);
    }

    public void introduce() {
        System.out.println("我叫" + name + "，今年" + age + "岁");
    }

    // Getter 和 Setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

// 定义一个学生类
class Student {
    private String name;
    private String studentId;
    private double score;

    public Student(String name, String studentId, double score) {
        this.name = name;
        this.studentId = studentId;
        this.score = score;
    }

    public String getInfo() {
        return "学生{name='" + name + "', 学号='" + studentId + "', 分数=" + score + "}";
    }

    public String getName() {
        return name;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
// 
public class ClassObject {
    public static void main(String[] args) {
        // ===== 创建对象 =====
        System.out.println("=== 创建对象 ===");
        Person p1 = new Person("张三", 25);
        Person p2 = new Person("李四", 30);

        // ===== 调用方法 =====
        System.out.println("\n=== 调用方法 ===");
        p1.sayHello();
        p1.introduce();
        p2.sayHello();
        p2.introduce();

        // ===== 使用 Getter/Setter =====
        System.out.println("\n=== Getter/Setter ===");
        System.out.println("p1 的姓名：" + p1.getName());
        p1.setAge(26);
        System.out.println("p1 的新年龄：" + p1.getAge());

        // ===== 创建多个学生 =====
        System.out.println("\n=== 学生信息 ===");
        Student s1 = new Student("小明", "S001", 85.5);
        Student s2 = new Student("小红", "S002", 92.0);
        Student s3 = new Student("小刚", "S003", 78.5);

        System.out.println(s1.getInfo());
        System.out.println(s2.getInfo());
        System.out.println(s3.getInfo());

        // ===== 计算平均分 =====
        double total = s1.getScore() + s2.getScore() + s3.getScore();
        double average = total / 3;
        System.out.println("\n平均分：" + average);

        // ===== 练习 =====
        // 1. 创建一个 Book 类，包含书名、作者、价格
        // 2. 创建一个 Rectangle 类，包含长和宽，有计算面积和周长的方法
    }
}
