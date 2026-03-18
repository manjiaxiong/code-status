package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * ==================== 原生 SQL 操作示例 ====================
 *
 * 作用：学习手写 SQL 实现增删改查
 * 访问：http://localhost:18080/api/sql
 *
 * 与 JPA 的区别：
 * - JPA：方法命名自动生成 SQL，简单但不灵活
 * - 原生 SQL：手写 SQL，灵活但需要自己处理映射
 */
@RestController
@RequestMapping("/api/sql")
public class NativeSqlController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * ==================== 行映射器 ====================
     * 将 ResultSet 映射为 User 对象
     */
    private RowMapper<User> userRowMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setUsername(rs.getString("username"));
            user.setEmail(rs.getString("email"));
            user.setAge(rs.getInt("age"));
            user.setPassword(rs.getString("password"));
            user.setNickname(rs.getString("nickname"));
            return user;
        }
    };

    // =====================================================
    // ==================== 1. 查询（SELECT） ====================
    // =====================================================

    /**
     * ==================== 查询所有用户 ====================
     *
     * 访问：GET http://localhost:18080/api/sql/users
     *
     * 测试命令：
     * curl http://localhost:18080/api/sql/users
     *
     * 对应 SQL：
     * SELECT * FROM users;
     */
    @GetMapping("/users")
    public Result<List<User>> list() {
        String sql = "SELECT * FROM users ORDER BY id DESC";
        List<User> users = jdbcTemplate.query(sql, userRowMapper);
        return Result.success(users);
    }

    /**
     * ==================== 根据 ID 查询 ====================
     *
     * 访问：GET http://localhost:18080/api/sql/users/1
     *
     * 测试命令：
     * curl http://localhost:18080/api/sql/users/1
     *
     * 对应 SQL：
     * SELECT * FROM users WHERE id = ?
     *
     * 注意：使用 ? 占位符防止 SQL 注入
     */
    @GetMapping("/users/{id}")
    public Result<User> getById(@PathVariable Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql, userRowMapper, id);
            return Result.success(user);
        } catch (Exception e) {
            return Result.error(404, "用户不存在");
        }
    }

    /**
     * ==================== 条件查询 - 根据用户名 ====================
     *
     * 访问：GET http://localhost:18080/api/sql/users/by-username/zhangsan
     *
     * 测试命令：
     * curl http://localhost:18080/api/sql/users/by-username/zhangsan
     *
     * 对应 SQL：
     * SELECT * FROM users WHERE username = ?
     */
    @GetMapping("/users/by-username/{username}")
    public Result<User> getByUsername(@PathVariable String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql, userRowMapper, username);
            return Result.success(user);
        } catch (Exception e) {
            return Result.error(404, "用户不存在");
        }
    }

    /**
     * ==================== 模糊查询 ====================
     *
     * 访问：GET http://localhost:18080/api/sql/users/search?keyword=张
     *
     * 测试命令：
     * curl "http://localhost:18080/api/sql/users/search?keyword=张"
     *
     * 对应 SQL：
     * SELECT * FROM users WHERE username LIKE '%张%' OR nickname LIKE '%张%'
     *
     * 注意：LIKE 语句中通配符的拼接
     */
    @GetMapping("/users/search")
    public Result<List<User>> search(@RequestParam String keyword) {
        String sql = "SELECT * FROM users WHERE username LIKE ? OR nickname LIKE ? ORDER BY id DESC";
        String likeKeyword = "%" + keyword + "%";
        List<User> users = jdbcTemplate.query(sql, userRowMapper, likeKeyword, likeKeyword);
        return Result.success(users);
    }

    /**
     * ==================== 范围查询 - 年龄区间 ====================
     *
     * 访问：GET http://localhost:18080/api/sql/users/age-range?min=20&max=30
     *
     * 测试命令：
     * curl "http://localhost:18080/api/sql/users/age-range?min=20&max=30"
     *
     * 对应 SQL：
     * SELECT * FROM users WHERE age BETWEEN ? AND ?
     */
    @GetMapping("/users/age-range")
    public Result<List<User>> getByAgeRange(
        @RequestParam Integer min,
        @RequestParam Integer max
    ) {
        String sql = "SELECT * FROM users WHERE age BETWEEN ? AND ? ORDER BY age";
        List<User> users = jdbcTemplate.query(sql, userRowMapper, min, max);
        return Result.success(users);
    }

    /**
     * ==================== 聚合查询 - 统计 ====================
     *
     * 访问：GET http://localhost:18080/api/sql/users/count
     *
     * 测试命令：
     * curl http://localhost:18080/api/sql/users/count
     *
     * 对应 SQL：
     * SELECT COUNT(*) FROM users
     */
    @GetMapping("/users/count")
    public Result<Map<String, Object>> count() {
        String sql = "SELECT COUNT(*) as total FROM users";
        Map<String, Object> result = jdbcTemplate.queryForMap(sql);
        return Result.success(result);
    }

    /**
     * ==================== 聚合查询 - 平均年龄 ====================
     *
     * 访问：GET http://localhost:18080/api/sql/users/avg-age
     *
     * 测试命令：
     * curl http://localhost:18080/api/sql/users/avg-age
     *
     * 对应 SQL：
     * SELECT AVG(age) as avgAge FROM users
     */
    @GetMapping("/users/avg-age")
    public Result<Map<String, Object>> avgAge() {
        String sql = "SELECT AVG(age) as avgAge FROM users";
        Map<String, Object> result = jdbcTemplate.queryForMap(sql);
        return Result.success(result);
    }

    // =====================================================
    // ==================== 2. 新增（INSERT） ====================
    // =====================================================

    /**
     * ==================== 新增用户 ====================
     *
     * 访问：POST http://localhost:18080/api/sql/users
     * 请求体：{"username":"wangwu","password":"123456","email":"wangwu@example.com","age":28,"nickname":"老王"}
     *
     * 测试命令：
     * curl -X POST http://localhost:18080/api/sql/users \
     *   -H "Content-Type: application/json" \
     *   -d '{"username":"wangwu","password":"123456","email":"wangwu@example.com","age":28,"nickname":"老王"}'
     *
     * 对应 SQL：
     * INSERT INTO users (username, password, email, age, nickname, create_time, update_time)
     * VALUES (?, ?, ?, ?, ?, ?, ?)
     *
     * 注意：
     * - 使用 ? 占位符防止 SQL 注入
     * - jdbcTemplate.update() 返回受影响的行数
     */
    @PostMapping("/users/add")
    public Result<User> create(@RequestBody User user) {
        // 验证必填字段
        if (user.getUsername() == null || user.getPassword() == null) {
            return Result.error(400, "用户名和密码不能为空");
        }

        String sql = "INSERT INTO users (username, password, email, age, nickname, create_time, update_time) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        // 执行插入，返回受影响行数
        int rows = jdbcTemplate.update(sql,
            user.getUsername(),
            user.getPassword(),
            user.getEmail(),
            user.getAge(),
            user.getNickname(),
            new Date(),  // create_time
            new Date()   // update_time
        );

        if (rows > 0) {
            // 查询刚插入的用户（MySQL 特有：LAST_INSERT_ID()）
            Long generatedId = jdbcTemplate.queryForObject(
                "SELECT LAST_INSERT_ID()", Long.class
            );
            user.setId(generatedId);
            return Result.success("创建成功", user);
        } else {
            return Result.error(500, "创建失败");
        }
    }

    // =====================================================
    // ==================== 3. 更新（UPDATE） ====================
    // =====================================================

    /**
     * ==================== 更新用户（全量更新） ====================
     *
     * 访问：PUT http://localhost:18080/api/sql/users/1
     * 请求体：{"id":1,"username":"lisi","password":"123456","email":"lisi@example.com","age":28,"nickname":"李四"}
     *
     * 测试命令：
     * curl -X PUT http://localhost:18080/api/sql/users/1 \
     *   -H "Content-Type: application/json" \
     *   -d '{"id":1,"username":"lisi","password":"123456","email":"lisi@example.com","age":28,"nickname":"李四"}'
     *
     * 对应 SQL：
     * UPDATE users
     * SET username=?, password=?, email=?, age=?, nickname=?, update_time=?
     * WHERE id=?
     */
    @PutMapping("/users/{id}")
    public Result<User> update(
        @PathVariable Long id,
        @RequestBody User user
    ) {
        // 先检查用户是否存在
        try {
            jdbcTemplate.queryForObject("SELECT * FROM users WHERE id = ?", userRowMapper, id);
        } catch (Exception e) {
            return Result.error(404, "用户不存在");
        }

        String sql = "UPDATE users SET username=?, password=?, email=?, age=?, nickname=?, update_time=? WHERE id=?";

        int rows = jdbcTemplate.update(sql,
            user.getUsername(),
            user.getPassword(),
            user.getEmail(),
            user.getAge(),
            user.getNickname(),
            new Date(),  // update_time
            id
        );

        if (rows > 0) {
            user.setId(id);
            return Result.success("更新成功", user);
        } else {
            return Result.error(500, "更新失败");
        }
    }

    /**
     * ==================== 更新用户（部分字段更新） ====================
     *
     * 访问：PATCH http://localhost:18080/api/sql/users/1/email?email=new@example.com
     *
     * 测试命令：
     * curl -X PATCH "http://localhost:18080/api/sql/users/1/email?email=new@example.com"
     *
     * 对应 SQL：
     * UPDATE users SET email=?, update_time=? WHERE id=?
     */
    @PatchMapping("/users/{id}/email")
    public Result<Map<String, Object>> updateEmail(
        @PathVariable Long id,
        @RequestParam String email
    ) {
        String sql = "UPDATE users SET email=?, update_time=? WHERE id=?";

        int rows = jdbcTemplate.update(sql, email, new Date(), id);

        Map<String, Object> result = new java.util.HashMap<>();
        result.put("id", id);
        result.put("email", email);
        result.put("rows", rows);

        return rows > 0 ? Result.success(result) : Result.error(500, "更新失败");
    }

    // =====================================================
    // ==================== 4. 删除（DELETE） ====================
    // =====================================================

    /**
     * ==================== 删除用户 ====================
     *
     * 访问：DELETE http://localhost:18080/api/sql/users/1
     *
     * 测试命令：
     * curl -X DELETE http://localhost:18080/api/sql/users/1
     *
     * 对应 SQL：
     * DELETE FROM users WHERE id = ?
     */
    @DeleteMapping("/users/{id}")
    public Result<Map<String, Object>> delete(@PathVariable Long id) {
        // 先检查用户是否存在
        boolean exists = false;
        try {
            jdbcTemplate.queryForObject("SELECT id FROM users WHERE id = ?", Long.class, id);
            exists = true;
        } catch (Exception e) {
            // 用户不存在
        }

        if (!exists) {
            return Result.error(404, "用户不存在");
        }

        String sql = "DELETE FROM users WHERE id = ?";
        int rows = jdbcTemplate.update(sql, id);

        Map<String, Object> result = new java.util.HashMap<>();
        result.put("id", id);
        result.put("deleted", rows > 0);

        return rows > 0 ? Result.success("删除成功", result) : Result.error(500, "删除失败");
    }

    // =====================================================
    // ==================== 5. 批量操作 ====================
    // =====================================================

    /**
     * ==================== 批量插入 ====================
     *
     * 访问：POST http://localhost:18080/api/sql/users/batch
     * 请求体：[
     *   {"username":"user1","password":"123456","age":20},
     *   {"username":"user2","password":"123456","age":25},
     *   {"username":"user3","password":"123456","age":30}
     * ]
     *
     * 测试命令：
     * curl -X POST http://localhost:18080/api/sql/users/batch \
     *   -H "Content-Type: application/json" \
     *   -d '[{"username":"user1","password":"123456","age":20},{"username":"user2","password":"123456","age":25}]'
     *
     * 对应 SQL：
     * INSERT INTO users (username, password, age, create_time, update_time) VALUES (?, ?, ?, ?, ?)
     * （多次执行）
     */
    @PostMapping("/users/batch")
    public Result<Map<String, Object>> batchInsert(@RequestBody List<User> users) {
        String sql = "INSERT INTO users (username, password, age, create_time, update_time) VALUES (?, ?, ?, ?, ?)";

        // 批量执行
        List<Object[]> batchArgs = new java.util.ArrayList<>();
        for (User user : users) {
            Object[] args = {
                user.getUsername(),
                user.getPassword(),
                user.getAge(),
                new Date(),
                new Date()
            };
            batchArgs.add(args);
        }

        int[] results = jdbcTemplate.batchUpdate(sql, batchArgs);
        int totalInserted = 0;
        for (int result : results) {
            totalInserted += result;
        }

        Map<String, Object> resultMap = new java.util.HashMap<>();
        resultMap.put("total", totalInserted);
        resultMap.put("success", totalInserted == users.size());

        return Result.success("批量插入完成", resultMap);
    }

    // =====================================================
    // ==================== 6. 事务示例 ====================
    // =====================================================

    /**
     * ==================== 事务示例 - 转账 ====================
     *
     * 访问：POST http://localhost:18080/api/sql/transfer?fromId=1&toId=2&amount=100
     *
     * 说明：
     * - 这是一个事务示例（虽然用户表没有余额字段，仅做演示）
     * - 事务保证要么全部成功，要么全部回滚
     * - 实际场景应该是账户表有 balance 字段
     */
    @PostMapping("/transfer")
    public Result<String> transferExample(
        @RequestParam Long fromId,
        @RequestParam Long toId,
        @RequestParam Integer amount
    ) {
        // 注意：这个方法需要配合 @Transactional 注解使用
        // 由于 Controller 不适合直接处理事务，实际开发应该在 Service 层处理
        return Result.success("事务示例：实际应该在 Service 层使用 @Transactional 注解");
    }
}
