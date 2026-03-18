package com.example.demo.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ==================== MyBatis-Plus 配置类 ====================
 *
 * ★★★ 和 mysql-learning 的区别 ★★★
 *
 * mysql-learning 有一个 WebConfig.java（注册拦截器 LoggingInterceptor）
 * 这个项目没有那个拦截器（暂时不需要）
 *
 * 这个配置类是 MyBatis-Plus 专属的，mysql-learning 里没有
 * 因为 JPA 不需要这种配置（JPA 分页用 Pageable 参数，自动处理）
 *
 * 类比前端：
 * mysql-learning 的 WebConfig = Express 中间件配置
 * 这个 MybatisPlusConfig = axios 全局拦截器配置（interceptors）
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * 分页插件
     *
     * MyBatis-Plus 自带分页功能，但需要注册这个插件才能用
     * 配置好之后，用 Page 对象就能自动分页：
     *
     * Page<Product> page = new Page<>(1, 10);  // 第1页，每页10条
     * productMapper.selectPage(page, null);     // 自动加 LIMIT
     *
     * 类比前端：
     * 相当于配置了一个全局的分页中间件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}
