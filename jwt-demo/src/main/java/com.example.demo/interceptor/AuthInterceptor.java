package com.example.demo.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * ==================== 权限验证拦截器 ====================
 *
 * 作用：检查用户是否登录，验证 Token 有效性
 * 使用场景：
 *   - 用户登录后查看接口
 *   - 个人信息修改接口
 *   - 需要登录才能访问的页面
 *
 * 类似前端：
 *   // Next.js middleware
 *   if (!token && !path.startsWith('/public')) {
 *       return NextResponse.redirect('/login')
 *   }
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    /**
     * 免登录路径（白名单）
     * 类似前端：public 路由
     */
    private static final List<String> WHITE_LIST = Arrays.asList(
        "/api/login",      // 登录接口
        "/api/register",   // 注册接口
        "/api/public",     // 公开接口
        "/api/health"      // 健康检查
    );

    /**
     * 在请求处理之前执行
     * @return true = 放行，false = 拦截（返回 401）
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String uri = request.getRequestURI();
        String method = request.getMethod();

        // 1. 检查是否是白名单路径
        if (isWhiteList(uri)) {
            System.out.println("[Auth] 白名单路径，放行：" + uri);
            return true;
        }

        // 2. 从 Header 中获取 Token
        // 前端传递方式：axios.defaults.headers.common['Authorization'] = 'Bearer xxx'
        String token = request.getHeader("Authorization");

        // 兼容两种格式：Bearer xxx 或 直接 token
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // 3. 验证 Token
        if (token == null || token.isEmpty()) {
            System.out.println("[Auth] ❌ 缺少 Token：" + uri);
            return unauthorized(response, "未登录，请先登录");
        }

        // 4. 验证 Token 有效性（这里做简单验证，实际项目要查数据库或解析 JWT）
        if (!isValidToken(token)) {
            System.out.println("[Auth] ❌ Token 无效：" + uri);
            return unauthorized(response, "Token 无效或已过期");
        }

        // 5. 将用户信息存入 request，方便后续 Controller 使用
        String userId = getUserIdFromToken(token);
        request.setAttribute("userId", userId);
        request.setAttribute("username", getUsernameFromToken(token));

        System.out.println("[Auth] ✅ 验证通过：" + uri + " (用户：" + userId + ")");
        return true;
    }

    /**
     * 判断是否在白名单中
     */
    private boolean isWhiteList(String uri) {
        for (String path : WHITE_LIST) {
            if (uri.startsWith(path)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 简单验证 Token 格式
     * TODO: 实际项目要验证 JWT 签名或查询 Redis
     */
    private boolean isValidToken(String token) {
        // 简单规则：token 长度大于 10 且不以 "invalid" 开头
        return token != null && token.length() > 10 && !token.startsWith("invalid");
    }

    /**
     * 从 Token 中解析用户 ID
     * TODO: 实际项目要解析 JWT 或查 Redis
     */
    private String getUserIdFromToken(String token) {
        // 模拟：从 token 中提取用户 ID
        // 实际项目：return JwtUtil.getUserId(token);
        return "user_" + token.substring(0, Math.min(8, token.length()));
    }

    /**
     * 从 Token 中解析用户名
     */
    private String getUsernameFromToken(String token) {
        return "user_" + token.hashCode() % 10000;
    }

    /**
     * 返回 401 未授权
     */
    private boolean unauthorized(HttpServletResponse response, String message) throws Exception {
        response.setStatus(401);  // 401 Unauthorized
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(
            "{\"code\": 401, \"message\": \"" + message + "\"}"
        );
        return false;  // false = 拦截请求，不再继续处理
    }
}