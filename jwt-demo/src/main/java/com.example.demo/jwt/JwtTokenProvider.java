package com.example.demo.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT Token 生成和验证工具类
 *
 * 功能：
 * 1. 生成 Token（登录成功后调用）
 * 2. 验证 Token（拦截器中调用）
 * 3. 从 Token 中提取用户信息
 */
@Component
public class JwtTokenProvider {

    // 密钥（从配置文件读取）
    @Value("${jwt.secret:mySecretKey123456789012345678901234567890}")
    private String secretKey;

    // Token 过期时间（毫秒）- 默认 7 天
    @Value("${jwt.expiration:604800000}")
    private long expiration;

    // 初始化密钥
    @PostConstruct
    protected void init() {
        // 确保密钥长度足够（至少 256 位）
        if (secretKey.length() < 32) {
            throw new RuntimeException("JWT secret key must be at least 32 characters");
        }
    }

    /**
     * 生成 JWT Token
     *
     * @param username 用户名
     * @param userId 用户 ID
     * @return 加密后的 Token 字符串
     *
     * 类似前端：jwt.sign({ userId, username }, secret)
     */
    public String generateToken(String username, Long userId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                // Header：设置算法
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")

                // Payload：载荷数据
                .setSubject(username)           // 主题（这里放用户名）
                .claim("userId", userId)        // 自定义字段：用户 ID
                .claim("username", username)    // 自定义字段：用户名
                .setIssuedAt(now)               // 签发时间
                .setExpiration(expiryDate)      // 过期时间

                // Signature：签名
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))

                // 压缩成紧凑格式
                .compact();
    }

    /**
     * 从 Token 中获取用户名
     *
     * @param token JWT Token
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.getSubject();
    }

    /**
     * 从 Token 中获取用户 ID
     *
     * @param token JWT Token
     * @return 用户 ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.get("userId", Long.class);
    }

    /**
     * 验证 Token 是否有效
     *
     * @param token JWT Token
     * @return true=有效，false=无效或过期
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException ex) {
            // 签名无效
            System.err.println("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            // Token 格式错误
            System.err.println("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            // Token 已过期
            System.err.println("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            // Token 不受支持
            System.err.println("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            // Token 为空
            System.err.println("JWT claims string is empty");
        }
        return false;
    }

    /**
     * 解析 Claims（私有方法）
     */
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
