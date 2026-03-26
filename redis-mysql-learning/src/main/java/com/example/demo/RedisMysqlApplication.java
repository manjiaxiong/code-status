package com.example.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * MyBatis-Plus + Redis 整合学习项目启动类
 */
@SpringBootApplication
@MapperScan("com.example.demo.mapper")  // 扫描 Mapper 接口
public class RedisMysqlApplication {
    public static void main(String[] args) {
        SpringApplication.run(RedisMysqlApplication.class, args);
    }
}
