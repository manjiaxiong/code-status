package com.example.demo.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * ==================== 统一返回结果类 ====================
 *
 * 作用：所有接口都返回统一的格式
 * 类似前端：封装的 APIResponse 类型
 *
 * 返回格式：
 * {
 *     "code": 200,
 *     "message": "success",
 *     "data": {...}
 * }
 *
 * 成功示例：
 * {
 *     "code": 200,
 *     "message": "success",
 *     "data": {"id":1,"username":"zhangsan"}
 * }
 *
 * 失败示例：
 * {
 *     "code": 400,
 *     "message": "参数错误",
 *     "data": null
 * }
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    /**
     * 状态码
     * 200 = 成功
     * 400 = 参数错误
     * 401 = 未登录
     * 403 = 无权限
     * 404 = 不存在
     * 500 = 服务器错误
     */
    private Integer code;

    /**
     * 提示信息
     */
    private String message;

    /**
     * 返回数据（泛型，可以是任何类型）
     */
    private T data;

    // ==================== 静态工厂方法（方便调用） ====================

    /**
     * 成功 - 有数据
     * 用法：Result.success(userList)
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("success");
        result.setData(data);
        return result;
    }

    /**
     * 成功 - 带自定义消息
     * 用法：Result.success("操作成功", userList)
     */
    public static <T> Result<T> success(String message, T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    /**
     * 失败 - 无数据
     * 用法：Result.error("参数错误")
     */
    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<>();
        result.setCode(400);
        result.setMessage(message);
        result.setData(null);
        return result;
    }

    /**
     * 失败 - 指定状态码
     * 用法：Result.error(404, "用户不存在")
     */
    public static <T> Result<T> error(Integer code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(null);
        return result;
    }
}
