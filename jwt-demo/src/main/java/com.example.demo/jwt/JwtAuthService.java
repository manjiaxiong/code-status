package com.example.demo.jwt;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * JWT 登录服务
 *
 * 处理用户登录逻辑
 */
@Service
public class JwtAuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // Token 有效期（毫秒）- 7 天
    private static final long TOKEN_EXPIRATION = 7 * 24 * 60 * 60 * 1000L;

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录成功返回 Token 和用户信息
     * @throws RuntimeException 登录失败抛出异常
     */
    public LoginResponse login(String username, String password) {
        // 1. 根据用户名查找用户
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (!userOptional.isPresent()) {
            throw new RuntimeException("用户不存在");
        }

        User user = userOptional.get();

        // 2. 验证密码（实际项目中应该用加密后的密码对比）
        // 演示项目暂时用明文密码
        if (!password.equals(user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        // 3. 生成 JWT Token
        String token = jwtTokenProvider.generateToken(user.getUsername(), user.getId());

        // 4. 返回登录信息
        return new LoginResponse(token, user.getUsername(), user.getId(), TOKEN_EXPIRATION);
    }

    /**
     * 根据用户名注册新用户
     *
     * @param username 用户名
     * @param password 密码
     * @param nickname 昵称
     * @return 注册成功的用户
     */
    public User register(String username, String password, String nickname) {
        // 1. 检查用户名是否已存在
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("用户名已存在");
        }

        // 2. 创建新用户
        User user = new User();
        user.setUsername(username);
        user.setPassword(password); // 实际项目中应该加密
        user.setNickname(nickname != null ? nickname : username);

        // 3. 保存用户
        return userRepository.save(user);
    }

    /**
     * 从 Token 中获取用户 ID
     *
     * @param token JWT Token
     * @return 用户 ID
     */
    public Long getUserIdFromToken(String token) {
        return jwtTokenProvider.getUserIdFromToken(token);
    }

    /**
     * 获取当前用户信息
     *
     * @param userId 用户 ID
     * @return 用户信息
     */
    public User getCurrentUser(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            throw new RuntimeException("用户不存在");
        }
        return userOptional.get();
    }
}
