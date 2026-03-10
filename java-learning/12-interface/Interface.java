/**
 * 接口（Interface）
 * 运行：javac -encoding UTF-8 Interface.java && java Interface
 *
 * 接口特点：
 * 1. 接口中的方法默认是 public abstract
 * 2. 接口中的变量默认是 public static final
 * 3. 一个类可以实现多个接口
 * 4. 接口可以继承其他接口
 */

// 定义第一个接口 - 飞行动作
interface Flyable {
    // 常量（必须初始化）
    int MAX_HEIGHT = 1000;

    // 抽象方法（没有方法体）
    void fly();

    // 默认方法（Java 8+，有方法体）
    default void land() {
        System.out.println("安全降落");
    }
}

// 定义第二个接口 - 游泳动作
interface Swimmable {
    void swim();

    default void floatOnWater() {
        System.out.println("漂浮在水上");
    }
}

// 定义第三个接口 - 发声动作
interface Soundable {
    void makeSound();
}

// 实现一个类，实现多个接口
class Bird implements Flyable, Soundable {
    private String name;

    public Bird(String name) {
        this.name = name;
    }

    // 实现 Flyable 接口的 fly 方法
    @Override
    public void fly() {
        System.out.println(name + "在天空飞翔");
    }

    // 实现 Soundable 接口的 makeSound 方法
    @Override
    public void makeSound() {
        System.out.println(name + "叽叽喳喳");
    }

    // 重写默认方法（可选）
    @Override
    public void land() {
        System.out.println(name + "降落在树枝上");
    }
}

// 实现另一个类
class Fish implements Swimmable, Soundable {
    private String name;

    public Fish(String name) {
        this.name = name;
    }

    @Override
    public void swim() {
        System.out.println(name + "在水中游动");
    }

    @Override
    public void makeSound() {
        System.out.println(name + "吐泡泡");
    }
}

// 鸭子既能飞又能游泳又能叫
class Duck implements Flyable, Swimmable, Soundable {
    private String name;

    public Duck(String name) {
        this.name = name;
    }

    @Override
    public void fly() {
        System.out.println(name + "展翅高飞");
    }

    @Override
    public void swim() {
        System.out.println(name + "在水面游泳");
    }

    @Override
    public void makeSound() {
        System.out.println(name + "嘎嘎嘎");
    }
}

public class Interface {
    public static void main(String[] args) {
        // ===== 创建鸟对象 =====
        System.out.println("=== 鸟类 ===");
        Bird bird = new Bird("麻雀");
        bird.fly();         // 实现 Flyable 接口
        bird.makeSound();   // 实现 Soundable 接口
        bird.land();        // 使用默认方法

        // ===== 创建鱼对象 =====
        System.out.println("\n=== 鱼类 ===");
        Fish fish = new Fish("金鱼");
        fish.swim();        // 实现 Swimmable 接口
        fish.makeSound();   // 实现 Soundable 接口
        fish.floatOnWater();// 使用默认方法

        // ===== 创建鸭子对象（实现三个接口）=====
        System.out.println("\n=== 鸭子（实现三个接口）===");
        Duck duck = new Duck("唐老鸭");
        duck.fly();         // Flyable
        duck.swim();        // Swimmable
        duck.makeSound();   // Soundable

        // ===== 接口引用指向实现类对象 =====
        System.out.println("\n=== 接口多态 ===");
        Flyable flyable = new Bird("老鹰");
        flyable.fly();      // 调用 Bird 的 fly 方法

        Swimmable swimmable = new Duck("野鸭");
        swimmable.swim();   // 调用 Duck 的 swim 方法

        // ===== 访问接口常量 =====
        System.out.println("\n=== 接口常量 ===");
        System.out.println("飞行最大高度：" + Flyable.MAX_HEIGHT);

        // ===== 练习 =====
        // 1. 创建一个 USB 接口，定义 connect() 方法
        //    创建 Mouse 和 Keyboard 类实现该接口
        // 2. 创建一个 Payment 接口，定义 pay() 方法
        //    创建 Alipay 和 WechatPay 类实现该接口
    }
}
