package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * ==================== 用户数据访问层 ====================
 *
 * 作用：操作 User 表的数据库
 * 类似前端：TypeORM 的 Repository、Prisma Client
 *
 * 神奇之处：
 * - 不用写实现类，Spring 自动实现
 * - 方法名遵循命名规则，自动生成 SQL
 *
 * 例如：
 *   findByUsername()  → SELECT * FROM users WHERE username = ?
 *   findByAgeGreaterThan() → SELECT * FROM users WHERE age > ?
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * ==================== 内置方法（JpaRepository 提供） ====================
     *
     * 这些方法已经自动实现，直接使用：
     *
     * save(User user)        - 保存（新增或更新）
     * findById(Long id)      - 根据 ID 查询
     * findAll()              - 查询所有
     * deleteById(Long id)    - 根据 ID 删除
     * count()                - 统计数量
     * existsById(Long id)    - 判断是否存在
     *
     * ==================== 自定义方法 ====================
     *
     * 遵循命名规则，Spring 自动生成 SQL
     */

    /**
     * 根据用户名查询
     * 类似 SQL：SELECT * FROM users WHERE username = ?
     *
     * 用法：userRepository.findByUsername("zhangsan")
     */
    User findByUsername(String username);

    /**
     * 根据邮箱查询
     * 类似 SQL：SELECT * FROM users WHERE email = ?
     */
    User findByEmail(String email);

    /**
     * 根据年龄范围查询
     * 类似 SQL：SELECT * FROM users WHERE age BETWEEN ? AND ?
     */
    List<User> findByAgeBetween(Integer minAge, Integer maxAge);

    /**
     * 根据年龄大于查询
     * 类似 SQL：SELECT * FROM users WHERE age > ?
     */
    List<User> findByAgeGreaterThan(Integer age);

    /**
     * 根据用户名模糊查询
     * 类似 SQL：SELECT * FROM users WHERE username LIKE '%xx%'
     */
    List<User> findByUsernameContaining(String keyword);

    /**
     * 多条件查询
     * 类似 SQL：SELECT * FROM users WHERE username = ? AND email = ?
     */
    User findByUsernameAndEmail(String username, String email);

    /**
     * 排序查询
     * 类似 SQL：SELECT * FROM users ORDER BY age DESC, create_time ASC
     */
    List<User> findAllByOrderByAgeDescCreateTimeAsc();
}