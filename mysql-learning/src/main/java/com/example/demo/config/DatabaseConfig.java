package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * ==================== 数据库配置类 ====================
 *
 * 作用：手动配置数据源（可选，Spring Boot 会自动配置）
 *
 * 说明：
 * - Spring Boot 会自动读取 application.properties 中的数据库配置
 * - 这个类仅供学习参考，展示如何手动配置 DataSource
 */
@Configuration
public class DatabaseConfig {

    /**
     * ==================== 手动配置数据源 ====================
     *
     *  Spring Boot 会自动创建这个 Bean，这里只是演示
     */
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/demo?useSSL=false&serverTimezone=Asia/Shanghai&characterEncoding=utf-8&allowPublicKeyRetrieval=true");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
        return dataSource;
    }

    /**
     * ==================== 配置 JdbcTemplate ====================
     *
     * JdbcTemplate 是 Spring 提供的原生 SQL 操作工具
     * 类似前端的：db.query('SELECT * FROM users')
     */
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
