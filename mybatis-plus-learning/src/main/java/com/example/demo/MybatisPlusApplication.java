package com.example.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ==================== 启动类 ====================
 *
 * ★★★ 和 mysql-learning 的区别 ★★★
 *
 * mysql-learning（JPA + JdbcTemplate）:
 *   @SpringBootApplication
 *   @EnableJpaRepositories(basePackages = "com.example.demo.repository")
 *                          ↑ 告诉 JPA 去 repository 包找 Repository 接口
 *
 * mybatis-plus-learning（MyBatis-Plus）:
 *   @SpringBootApplication
 *   @MapperScan("com.example.demo.mapper")
 *               ↑ 告诉 MyBatis-Plus 去 mapper 包找 Mapper 接口
 *
 * 本质上做的是同一件事：告诉框架去哪里找数据库操作的接口
 * JPA 找的是 Repository（extends JpaRepository）
 * MP  找的是 Mapper（extends BaseMapper）
 *
 * 类比前端：
 * 相当于 webpack 的 resolve.modules / alias 配置
 * 告诉打包工具去哪个目录找模块
 */
@SpringBootApplication
@MapperScan("com.example.demo.mapper")  // 扫描 Mapper 接口所在的包
public class MybatisPlusApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatisPlusApplication.class, args);
        System.out.println("========================================");
        System.out.println("   MyBatis-Plus 学习项目启动成功!");
        System.out.println("   端口：http://localhost:18081");
        System.out.println("   对比 mysql-learning (18080) 用 JdbcTemplate");
        System.out.println("   这个项目用 MyBatis-Plus，同样的功能，更少的代码");
        System.out.println("========================================");
    }
}
