package com.example.demo.controller;

import lombok.Data;

/**
 * 注册请求 DTO
 *
 * 用于接收前端传来的注册参数
 * 类似前端：interface RegisterRequest { username: string; password: string; nickname?: string; }
 */
@Data
public class RegisterRequest {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 昵称（可选）
     */
    private String nickname;
}
