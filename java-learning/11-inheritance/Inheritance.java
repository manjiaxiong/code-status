/**
 * 继承
 * 运行：javac -encoding UTF-8 Inheritance.java && java Inheritance
 */

// 父类（基类）
class Animal {
    protected String name;
    protected int age;

    public Animal(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void eat() {
        System.out.println(name + "在吃东西");
    }

    public void sleep() {
        System.out.println(name + "在睡觉");
    }

    public void makeSound() {
        System.out.println("动物发出声音");
    }

    public String getInfo() {
        return "Animal{name='" + name + "', age=" + age + "}";
    }
}

// 子类（派生类）
class Dog extends Animal {
    private String breed;

    public Dog(String name, int age, String breed) {
        super(name, age);  // 调用父类构造器
        this.breed = breed;
    }

    // 重写父类方法
    @Override
    public void makeSound() {
        System.out.println(name + "汪汪汪");
    }

    // 子类特有方法
    public void bark() {
        System.out.println(name + "大声叫");
    }

    @Override
    public String getInfo() {
        return "Dog{name='" + name + "', age=" + age + ", breed='" + breed + "'}";
    }
}

// 另一个子类
class Cat extends Animal {
    private String color;

    public Cat(String name, int age, String color) {
        super(name, age);
        this.color = color;
    }

    @Override
    public void makeSound() {
        System.out.println(name + "喵喵喵");
    }

    public void scratch() {
        System.out.println(name + "在抓挠");
    }
}

public class Inheritance {
    public static void main(String[] args) {
        // ===== 创建子类对象 =====
        System.out.println("=== 创建子类对象 ===");
        Dog dog = new Dog("旺财", 3, "金毛");
        Cat cat = new Cat("咪咪", 2, "白色");

        // ===== 调用继承的方法 =====
        System.out.println("\n=== 继承的方法 ===");
        dog.eat();       // 继承自 Animal
        dog.sleep();     // 继承自 Animal
        dog.makeSound(); // 重写的方法

        cat.eat();
        cat.sleep();
        cat.makeSound();

        // ===== 调用子类特有方法 =====
        System.out.println("\n=== 子类特有方法 ===");
        dog.bark();
        cat.scratch();

        // ===== 获取信息 =====
        System.out.println("\n=== 对象信息 ===");
        System.out.println(dog.getInfo());
        System.out.println(cat.getInfo());

        // ===== 多态 =====
        System.out.println("\n=== 多态 ===");
        Animal a1 = new Dog("小黑", 2, "拉布拉多");
        Animal a2 = new Cat("小白", 1, "灰色");

        a1.makeSound();  // 实际调用 Dog 的 makeSound
        a2.makeSound();  // 实际调用 Cat 的 makeSound

        // ===== instanceof 类型判断 =====
        System.out.println("\n=== instanceof ===");
        if (a1 instanceof Dog) {
            System.out.println("a1 是 Dog 类型");
            Dog d = (Dog) a1;  // 向下转型
            d.bark();
        }

        // ===== 练习 =====
        // 1. 创建一个 Person 父类，Student 和 Teacher 两个子类
        // 2. 创建一个 Shape 父类，Circle 和 Rectangle 两个子类
    }
}
