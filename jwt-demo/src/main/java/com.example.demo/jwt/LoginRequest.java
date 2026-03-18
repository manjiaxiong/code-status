package com.example.demo.jwt;

import lombok.Data;

/**
 * 登录请求 DTO（数据传输对象）
 *
 * 用于接收前端传来的登录参数
 * 类似前端：interface LoginRequest { username: string; password: string; }
 */
@Data
public class LoginRequest {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;
}
