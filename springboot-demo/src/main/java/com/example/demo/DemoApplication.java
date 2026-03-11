package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ==================== Spring Boot 启动类 ====================
 *
 * 这是整个项目的入口，类似前端项目的 index.js 或 main.js
 *
 * 运行方式：
 * 1. IDEA：右键 → Run 'DemoApplication'
 * 2. 命令行：mvn spring-boot:run
 * 3. 打包后：java -jar target/springboot-demo.jar
 *
 * 启动后访问：http://localhost:8080
 */

/**
 * @SpringBootApplication 是一个组合注解，包含：
 * - @Configuration：表示这是一个配置类
 * - @EnableAutoConfiguration：启用自动配置（Spring Boot 的核心魔法）
 * - @ComponentScan：自动扫描当前包及子包下的组件（Controller、Service 等）
 *
 * 类比前端：
 * app.listen(8080, () => console.log('Server running on port 8080'))
 */
// 排除数据库自动配置（暂时不使用数据库）
@SpringBootApplication(exclude = {
    org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class
})


public class DemoApplication {
    /**
     * 帮我写一个循环
     */

    /**
     * main 方法 - Java 程序的入口
     * 类似前端：#!/usr/bin/env node
     */
    public static void main(String[] args) {
        // SpringApplication.run() 启动 Spring Boot 应用
        // 返回一个 ApplicationContext（应用上下文），包含所有 Spring 管理的 Bean
        SpringApplication.run(DemoApplication.class, args);
        // 打印成功信息（方便查看）
        System.out.println("========================================");
        System.out.println("   🚀 Spring Boot 启动成功!");
        System.out.println("   访问地址：http://localhost:18080");
        System.out.println("   测试接口：/hello, /api/user, /api/server-time");
        System.out.println("========================================");
    }
}

/**
 * ==================== 补充说明 ====================
 *
 * Spring Boot 启动流程：
 * 1. 读取 main 方法
 * 2. SpringApplication.run() 启动
 * 3. 扫描 @SpringBootApplication 所在包及子包
 * 4. 发现 @RestController、@Service 等注解
 * 5. 创建 Bean 并管理（依赖注入）
 * 6. 启动内置 Tomcat 服务器（默认 8080 端口）
 * 7. 注册所有路由（@GetMapping、@PostMapping 等）
 * 8. 等待请求...
 *
 * 类比前端 Express：
 * const express = require('express');
 * const app = express();
 *
 * app.get('/hello', (req, res) => {
 *     res.send('Hello');
 * });
 *
 * app.listen(8080, () => {
 *     console.log('Server running on port 8080');
 * });
 */
