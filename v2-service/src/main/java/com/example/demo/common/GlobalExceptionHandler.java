package com.example.demo.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * ==================== 全局异常处理器 ====================
 *
 * 作用：捕获所有 Controller 抛出的异常，统一返回格式
 * 类似前端：全局的 error handler
 *
 * 不用在每个 Controller 里写 try-catch
 */
@Slf4j  // Lombok 注解：自动生成 log 对象
@RestControllerAdvice  // 标记为全局异常处理器
public class GlobalExceptionHandler {

    /**
     * ==================== 1. 处理业务异常 ====================
     *
     * 用法：在 Controller 中抛出 IllegalArgumentException
     * throw new IllegalArgumentException("参数错误");
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<?> handleIllegalArgument(IllegalArgumentException e) {
        log.error("参数错误：{}", e.getMessage());
        return Result.error(400, e.getMessage());
    }

    /**
     * ==================== 2. 处理资源未找到 ====================
     *
     * 访问不存在的 URL 时触发
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public Result<?> handleNotFound(NoHandlerFoundException e) {
        return Result.error(404, "接口不存在");
    }

    /**
     * ==================== 3. 处理其他所有异常 ====================
     *
     * 兜底处理，防止暴露敏感信息
     */
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        // 打印详细错误日志（方便调试）
        log.error("服务器内部错误：", e);

        // 返回模糊错误信息（防止暴露敏感信息）
        return Result.error(500, "服务器内部错误：" + e.getMessage());
    }
}
