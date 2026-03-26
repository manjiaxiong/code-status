package com.example.demo.common;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理（和 mybatis-plus-learning 一样）
 * Controller 不需要写 try-catch，这里统一兜底
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** 业务异常（参数错误等） */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<?> handleIllegalArgument(IllegalArgumentException e) {
        return Result.error(400, e.getMessage());
    }

    /** 运行时异常 */
    @ExceptionHandler(RuntimeException.class)
    public Result<?> handleRuntime(RuntimeException e) {
        return Result.error(500, "服务器错误: " + e.getMessage());
    }

    /** 兜底异常 */
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        return Result.error(500, "未知错误: " + e.getMessage());
    }
}
