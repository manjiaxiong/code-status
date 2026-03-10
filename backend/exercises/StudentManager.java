/**
 * 练习 2：学生管理系统
 * 要求：
 * 1. 创建 Student 类，包含姓名、年龄、成绩
 * 2. 实现添加学生、查找学生、计算平均分
 */
import java.util.ArrayList;
import java.util.List;

// 学生类
class Student {
    private String name;    // 姓名
    private int age;        // 年龄
    private double score;   // 成绩

    // 构造方法
    public Student(String name, int age, double score) {
        this.name = name;
        this.age = age;
        this.score = score;
    }

    // Getter 方法
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public double getScore() {
        return score;
    }

    // toString 方法，方便打印
    @Override
    public String toString() {
        return "学生{name='" + name + "', age=" + age + ", score=" + score + "}";
    }
}

// 学生管理类
public class StudentManager {
    private List<Student> students;

    public StudentManager() {
        this.students = new ArrayList<>();
    }

    // TODO: 实现添加学生方法
    public void addStudent(Student student) {
        students.add(student);
    }

    // TODO: 实现查找学生方法（按姓名）
    public Student findStudent(String name) {
        for (Student s : students) {
            if (s.getName().equals(name)) {
                return s;
            }
        }
        return null;
    }

    // TODO: 实现计算平均分方法
    public double getAverageScore() {
        if (students.isEmpty()) {
            return 0;
        }
        double total = 0;
        for (Student s : students) {
            total += s.getScore();
        }
        return total / students.size();
    }

    // 打印所有学生
    public void printAllStudents() {
        System.out.println("=== 学生列表 ===");
        for (Student s : students) {
            System.out.println(s);
        }
        System.out.println();
    }

    // 测试方法
    public static void main(String[] args) {
        StudentManager manager = new StudentManager();

        // 添加学生
        manager.addStudent(new Student("张三", 18, 85.5));
        manager.addStudent(new Student("李四", 19, 92.0));
        manager.addStudent(new Student("王五", 20, 78.5));

        // 打印所有学生
        manager.printAllStudents();

        // 查找学生
        Student s = manager.findStudent("李四");
        if (s != null) {
            System.out.println("找到学生：" + s);
        } else {
            System.out.println("未找到学生");
        }

        // 计算平均分
        System.out.println("平均分：" + manager.getAverageScore());
    }
}
