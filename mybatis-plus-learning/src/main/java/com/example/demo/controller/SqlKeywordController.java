package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.mapper.SqlKeywordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * ==================== SQL 关键词速查 Controller ====================
 *
 * 每个接口对应一个 SQL 关键词，浏览器直接访问就能看效果
 * 全部是 GET 请求，方便你在浏览器地址栏直接测试
 *
 * 使用方式：启动项目后，浏览器访问 http://localhost:18081/sql/xxx
 *
 * ★★★ SQL 关键词执行顺序（很重要！）★★★
 *
 * 你写 SQL 的顺序：SELECT → FROM → WHERE → GROUP BY → HAVING → ORDER BY → LIMIT
 * 数据库执行顺序：FROM → WHERE → GROUP BY → HAVING → SELECT → ORDER BY → LIMIT
 *
 * 类比前端：
 * 你写代码：  products.filter().map().sort().slice()
 * 执行顺序：  filter → map → sort → slice（和写的顺序一样）
 * SQL 不一样：先 FROM（找表），再 WHERE（过滤），最后 SELECT（取字段）
 */
@RestController
@RequestMapping("/sql")
public class SqlKeywordController {

    @Autowired
    private SqlKeywordMapper sqlKeywordMapper;

    // ==================== 基础筛选 ====================

    /**
     * 1. WHERE —— 筛选条件
     * GET /sql/where?price=100
     * 查价格大于 100 的商品
     */
    @GetMapping("/where")
    public Result<List<Map<String, Object>>> where(
            @RequestParam(defaultValue = "100") double price) {
        return Result.success(sqlKeywordMapper.demo_WHERE(price));
    }

    /**
     * 2. AND —— 多条件（都满足）
     * GET /sql/and?price=50
     * 价格大于 50 并且有库存
     */
    @GetMapping("/and")
    public Result<List<Map<String, Object>>> and(
            @RequestParam(defaultValue = "50") double price) {
        return Result.success(sqlKeywordMapper.demo_AND(price));
    }

    /**
     * 3. OR —— 多条件（满足一个就行）
     * GET /sql/or
     * 待付款或已付款的订单
     */
    @GetMapping("/or")
    public Result<List<Map<String, Object>>> or() {
        return Result.success(sqlKeywordMapper.demo_OR());
    }

    /**
     * 4. IN —— 在集合里
     * GET /sql/in
     * 状态在 (0, 1) 里的订单
     */
    @GetMapping("/in")
    public Result<List<Map<String, Object>>> in() {
        return Result.success(sqlKeywordMapper.demo_IN());
    }

    /**
     * 5. NOT IN —— 不在集合里
     * GET /sql/not-in
     * 不是"已完成"的订单
     */
    @GetMapping("/not-in")
    public Result<List<Map<String, Object>>> notIn() {
        return Result.success(sqlKeywordMapper.demo_NOT_IN());
    }

    /**
     * 6. != —— 不等于
     * GET /sql/not-equal
     * 库存不为 0 的商品
     */
    @GetMapping("/not-equal")
    public Result<List<Map<String, Object>>> notEqual() {
        return Result.success(sqlKeywordMapper.demo_NOT_EQUAL());
    }

    /**
     * 7. LIKE —— 模糊搜索
     * GET /sql/like?keyword=手机
     */
    @GetMapping("/like")
    public Result<List<Map<String, Object>>> like(
            @RequestParam(defaultValue = "手") String keyword) {
        return Result.success(sqlKeywordMapper.demo_LIKE(keyword));
    }

    /**
     * 8. BETWEEN —— 范围查询
     * GET /sql/between?min=50&max=200
     */
    @GetMapping("/between")
    public Result<List<Map<String, Object>>> between(
            @RequestParam(defaultValue = "50") double min,
            @RequestParam(defaultValue = "200") double max) {
        return Result.success(sqlKeywordMapper.demo_BETWEEN(min, max));
    }

    /**
     * 9. IS NULL —— 判断为空
     * GET /sql/is-null
     */
    @GetMapping("/is-null")
    public Result<List<Map<String, Object>>> isNull() {
        return Result.success(sqlKeywordMapper.demo_IS_NULL());
    }

    /**
     * 10. IS NOT NULL —— 判断不为空
     * GET /sql/is-not-null
     */
    @GetMapping("/is-not-null")
    public Result<List<Map<String, Object>>> isNotNull() {
        return Result.success(sqlKeywordMapper.demo_IS_NOT_NULL());
    }

    // ==================== 排序和分页 ====================

    /**
     * 11. ORDER BY ASC —— 升序（小到大）
     * GET /sql/order-asc
     */
    @GetMapping("/order-asc")
    public Result<List<Map<String, Object>>> orderAsc() {
        return Result.success(sqlKeywordMapper.demo_ORDER_BY_ASC());
    }

    /**
     * 12. ORDER BY DESC —— 降序（大到小）
     * GET /sql/order-desc
     */
    @GetMapping("/order-desc")
    public Result<List<Map<String, Object>>> orderDesc() {
        return Result.success(sqlKeywordMapper.demo_ORDER_BY_DESC());
    }

    /**
     * 13. ORDER BY 多字段 —— 先按A排，A相同再按B排
     * GET /sql/order-multi
     */
    @GetMapping("/order-multi")
    public Result<List<Map<String, Object>>> orderMulti() {
        return Result.success(sqlKeywordMapper.demo_ORDER_BY_MULTI());
    }

    /**
     * 14. LIMIT —— 取前 N 条
     * GET /sql/limit?n=3
     */
    @GetMapping("/limit")
    public Result<List<Map<String, Object>>> limit(
            @RequestParam(defaultValue = "3") int n) {
        return Result.success(sqlKeywordMapper.demo_LIMIT(n));
    }

    /**
     * 15. LIMIT 分页 —— 跳过 offset 条，取 size 条
     * GET /sql/page?page=1&size=3
     * page 从 1 开始（第1页）
     */
    @GetMapping("/page")
    public Result<List<Map<String, Object>>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "3") int size) {
        int offset = (page - 1) * size;
        return Result.success(sqlKeywordMapper.demo_LIMIT_OFFSET(offset, size));
    }

    /**
     * 16. DISTINCT —— 去重
     * GET /sql/distinct
     */
    @GetMapping("/distinct")
    public Result<List<Map<String, Object>>> distinct() {
        return Result.success(sqlKeywordMapper.demo_DISTINCT());
    }

    // ==================== 聚合与分组 ====================

    /**
     * 17. 聚合函数 COUNT/SUM/AVG/MAX/MIN
     * GET /sql/aggregate
     */
    @GetMapping("/aggregate")
    public Result<Map<String, Object>> aggregate() {
        return Result.success(sqlKeywordMapper.demo_AGGREGATE());
    }

    /**
     * 18. GROUP BY —— 分组统计
     * GET /sql/group-by
     */
    @GetMapping("/group-by")
    public Result<List<Map<String, Object>>> groupBy() {
        return Result.success(sqlKeywordMapper.demo_GROUP_BY());
    }

    /**
     * 19. HAVING —— 分组后筛选
     * GET /sql/having?minCount=2
     * 查下单 2 次以上的用户
     */
    @GetMapping("/having")
    public Result<List<Map<String, Object>>> having(
            @RequestParam(defaultValue = "2") int minCount) {
        return Result.success(sqlKeywordMapper.demo_HAVING(minCount));
    }

    // ==================== 连表查询 ====================

    /**
     * 20. INNER JOIN —— 内连接
     * GET /sql/inner-join
     */
    @GetMapping("/inner-join")
    public Result<List<Map<String, Object>>> innerJoin() {
        return Result.success(sqlKeywordMapper.demo_INNER_JOIN());
    }

    /**
     * 21. LEFT JOIN —— 左连接
     * GET /sql/left-join
     */
    @GetMapping("/left-join")
    public Result<List<Map<String, Object>>> leftJoin() {
        return Result.success(sqlKeywordMapper.demo_LEFT_JOIN());
    }

    // ==================== 高级查询 ====================

    /**
     * 22. 子查询 —— SQL 里嵌套 SQL
     * GET /sql/subquery
     */
    @GetMapping("/subquery")
    public Result<List<Map<String, Object>>> subquery() {
        return Result.success(sqlKeywordMapper.demo_SUBQUERY());
    }

    /**
     * 23. EXISTS —— 存在性判断
     * GET /sql/exists
     */
    @GetMapping("/exists")
    public Result<List<Map<String, Object>>> exists() {
        return Result.success(sqlKeywordMapper.demo_EXISTS());
    }

    /**
     * 24. CASE WHEN —— 条件表达式
     * GET /sql/case-when
     */
    @GetMapping("/case-when")
    public Result<List<Map<String, Object>>> caseWhen() {
        return Result.success(sqlKeywordMapper.demo_CASE_WHEN());
    }

    /**
     * 25. IFNULL —— 空值替换
     * GET /sql/ifnull
     */
    @GetMapping("/ifnull")
    public Result<List<Map<String, Object>>> ifnull() {
        return Result.success(sqlKeywordMapper.demo_IFNULL());
    }

    /**
     * 26. DATE 函数 —— 按月统计
     * GET /sql/date-format
     */
    @GetMapping("/date-format")
    public Result<List<Map<String, Object>>> dateFormat() {
        return Result.success(sqlKeywordMapper.demo_DATE_FORMAT());
    }

    /**
     * 27. UNION —— 合并查询结果
     * GET /sql/union
     */
    @GetMapping("/union")
    public Result<List<Map<String, Object>>> union() {
        return Result.success(sqlKeywordMapper.demo_UNION());
    }

    /**
     * 28. AS —— 别名
     * GET /sql/as
     */
    @GetMapping("/as")
    public Result<List<Map<String, Object>>> as() {
        return Result.success(sqlKeywordMapper.demo_AS());
    }
}
