package com.example.demo.config;

import com.example.demo.interceptor.AuthInterceptor;
import com.example.demo.interceptor.LoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * ==================== Web 配置类 ====================
 *
 * 作用：配置 Spring MVC 的行为
 * 类似前端：Express 的 app.use() 配置
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoggingInterceptor loggingInterceptor;

    @Autowired
    private AuthInterceptor authInterceptor;

    /**
     * 注册拦截器
     * 类似前端：
     * app.use('/api', loggingMiddleware);
     * app.use('/api', authMiddleware);
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 1. 日志拦截器 - 记录所有请求
        registry.addInterceptor(loggingInterceptor)
                .addPathPatterns("/api/**");

        // 2. 权限拦截器 - 只拦截需要登录的接口 暂时先不用
//        registry.addInterceptor(authInterceptor)
//                .addPathPatterns("/api/**")
//                .excludePathPatterns(
//                    "/api/login",      // 登录
//                    "/api/register",   // 注册
//                    "/api/public/**",  // 公开接口
//                    "/api/health"      // 健康检查
//                );
    }
}