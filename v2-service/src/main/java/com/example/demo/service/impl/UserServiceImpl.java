package com.example.demo.service.impl;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * ==================== 用户服务实现类 ====================
 *
 * 作用：实现用户相关的业务逻辑
 *
 * 常用注解：
 * @Service - 标记为服务类，Spring 会自动管理
 * @Transactional - 事务注解，保证数据一致性
 *
 * 类似前端：
 * class UserService {
 *     async createUser(data) {
 *         // 1. 验证数据
 *         // 2. 处理业务逻辑
 *         // 3. 调用 DAO/Repository 保存
 *     }
 * }
 */
@Service  // 标记为 Service 类
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 查询所有用户
     */
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * 根据 ID 查询用户
     */
    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * 根据用户名查询
     */
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * 根据用户名模糊查询
     */
    @Override
    public List<User> findByUsernameContaining(String keyword) {
        return userRepository.findByUsernameContaining(keyword);
    }

    /**
     * 根据年龄范围查询
     */
    @Override
    public List<User> findByAgeBetween(Integer min, Integer max) {
        return userRepository.findByAgeBetween(min, max);
    }

    /**
     * ==================== 创建用户 ====================
     *
     * 业务逻辑：
     * 1. 验证用户名是否存在
     * 2. 验证用户名格式
     * 3. 保存用户
     */
    @Override
    @Transactional  // 事务：要么全部成功，要么全部失败
    public User createUser(User user) {
        // 1. 验证用户名不能为空
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new IllegalArgumentException("用户名不能为空");
        }

        // 2. 验证用户名是否已存在
        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null) {
            throw new IllegalArgumentException("用户名已存在");
        }

        // 3. 保存用户（id 为 null 时是新增）
        user.setId(null);  // 确保是新增
        return userRepository.save(user);
    }

    /**
     * ==================== 更新用户 ====================
     *
     * 业务逻辑：
     * 1. 查询用户是否存在
     * 2. 更新字段
     * 3. 保存
     */
    @Override
    @Transactional
    public User updateUser(Long id, User user) {
        // 1. 查询用户是否存在
        Optional<User> userOpt = userRepository.findById(id);

        if (userOpt.isPresent()) {
            // 2. 更新字段
            User existingUser = userOpt.get();
            existingUser.setUsername(user.getUsername());
            existingUser.setEmail(user.getEmail());
            existingUser.setAge(user.getAge());

            // 3. 保存
            return userRepository.save(existingUser);
        } else {
            throw new IllegalArgumentException("用户不存在");
        }
    }

    /**
     * ==================== 删除用户 ====================
     *
     * 业务逻辑：
     * 1. 查询用户是否存在
     * 2. 删除
     */
    @Override
    @Transactional
    public boolean deleteUser(Long id) {
        // 1. 查询用户是否存在
        if (userRepository.existsById(id)) {
            // 2. 删除
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
