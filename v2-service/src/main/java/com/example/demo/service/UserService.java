package com.example.demo.service;

import com.example.demo.entity.User;
import java.util.List;
import java.util.Optional;

/**
 * ==================== 用户服务接口 ====================
 *
 * 作用：定义用户相关的业务逻辑
 * 类似前端：Service 层 / Business Logic Layer
 */
public interface UserService {

    /**
     * 查询所有用户
     */
    List<User> findAll();

    /**
     * 根据 ID 查询用户
     */
    Optional<User> findById(Long id);

    /**
     * 根据用户名查询
     */
    User findByUsername(String username);

    /**
     * 根据用户名模糊查询
     */
    List<User> findByUsernameContaining(String keyword);

    /**
     * 根据年龄范围查询
     */
    List<User> findByAgeBetween(Integer min, Integer max);

    /**
     * 创建用户
     */
    User createUser(User user);

    /**
     * 更新用户
     */
    User updateUser(Long id, User user);

    /**
     * 删除用户
     */
    boolean deleteUser(Long id);
}
