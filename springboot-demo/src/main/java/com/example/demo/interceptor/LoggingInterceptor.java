package com.example.demo.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ==================== 请求拦截器 ====================
 *
 * 作用：拦截所有请求，打印日志
 * 类似前端：Express 的中间件
 *
 * app.use((req, res, next) => {
 *     console.log(`${req.method} ${req.url}`);
 *     next();
 * });
 */
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    /**
     * 在请求处理之前执行
     * 类似前端中间件的第一层
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);  // 记录开始时间

        // 打印请求信息
        System.out.println("\n========== 请求开始 ==========");
        System.out.println("方法：" + request.getMethod());
        System.out.println("URL: " + request.getRequestURI());
        System.out.println("参数：" + request.getQueryString());
        System.out.println("IP: " + request.getRemoteAddr());

        return true;  // true = 继续处理，false = 拦截请求
    }

    /**
     * 在请求处理完成后执行
     * 类似前端中间件的最后一层
     */
    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) throws Exception {

        long startTime = (Long) request.getAttribute("startTime");
        long cost = System.currentTimeMillis() - startTime;

        System.out.println("耗时：" + cost + "ms");
        System.out.println("状态码：" + response.getStatus());
        System.out.println("========== 请求结束 ==========\n");
    }
}