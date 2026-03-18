package com.example.demo.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录响应 DTO
 *
 * 登录成功后返回给前端的数据
 * 类似前端：interface LoginResponse { token: string; user: UserInfo; }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

    /**
     * JWT Token
     */
    private String token;

    /**
     * Token 类型（固定为 Bearer）
     */
    private String tokenType = "Bearer";

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * 过期时间（毫秒时间戳）
     */
    private Long expiresAt;

    /**
     * 方便前端显示的过期时间（秒）
     */
    private Long expiresIn;

    public LoginResponse(String token, String username, Long userId, long expiresIn) {
        this.token = token;
        this.tokenType = "Bearer";
        this.username = username;
        this.userId = userId;
        this.expiresIn = expiresIn / 1000; // 转成秒
        this.expiresAt = System.currentTimeMillis() + expiresIn;
    }
}
