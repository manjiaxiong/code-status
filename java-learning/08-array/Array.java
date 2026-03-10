/**
 * 数组
 * 运行：javac -encoding UTF-8 Array.java && java Array
 */
import java.util.Arrays;

public class Array {
    public static void main(String[] args) {
        // ===== 声明数组 =====
        System.out.println("=== 声明数组 ===");

        // 方式 1：直接赋值
        int[] arr1 = {1, 2, 3, 4, 5};

        // 方式 2：new 数组
        int[] arr2 = new int[5];  // 长度 5，默认值都是 0
        arr2[0] = 10;
        arr2[1] = 20;
        arr2[2] = 30;
        arr2[3] = 40;
        arr2[4] = 50;

        // 方式 3：new 并初始化
        int[] arr3 = new int[]{1, 2, 3, 4, 5};

        // ===== 访问数组 =====
        System.out.println("\n=== 访问数组 ===");
        System.out.println("arr1[0] = " + arr1[0]);  // 1
        System.out.println("arr1 的长度 = " + arr1.length);  // 5

        // ===== 遍历数组 =====
        System.out.println("\n=== 遍历数组 ===");
        System.out.print("标准 for 循环：");
        for (int i = 0; i < arr1.length; i++) {
            System.out.print(arr1[i] + " ");
        }

        System.out.print("\n增强 for 循环：");
        for (int num : arr1) {
            System.out.print(num + " ");
        }
        System.out.println();

        // ===== 数组常用操作 =====
        System.out.println("\n=== 数组操作 ===");
        int[] nums = {5, 2, 8, 1, 9};

        // 排序
        Arrays.sort(nums);
        System.out.println("排序后：" + Arrays.toString(nums));

        // 查找最大值
        int max = nums[0];
        for (int num : nums) {
            if (num > max) {
                max = num;
            }
        }
        System.out.println("最大值：" + max);

        // 计算总和
        int sum = 0;
        for (int num : nums) {
            sum += num;
        }
        System.out.println("总和：" + sum);
        System.out.println("平均值：" + (sum / nums.length));

        // ===== 二维数组 =====
        System.out.println("\n=== 二维数组 ===");
        int[][] matrix = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };

        System.out.println("matrix[0][0] = " + matrix[0][0]);  // 1
        System.out.println("matrix[1][2] = " + matrix[1][2]);  // 6

        // 遍历二维数组
        System.out.println("遍历二维数组：");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }

        // ===== 字符串数组 =====
        System.out.println("\n=== 字符串数组 ===");
        String[] names = {"张三", "李四", "王五"};
        for (String name : names) {
            System.out.println(name);
        }

        // ===== 练习 =====
        // 1. 找出数组中的第二大的数
        // 2. 反转数组
        // 3. 实现冒泡排序
    }
}
