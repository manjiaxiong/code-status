package com.example.demo.common;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * ==================== 全局异常处理器 ====================
 *
 * 作用：所有 Controller 抛出的异常都会被这里捕获
 * 不用每个接口都写 try-catch 了！
 *
 * 类比前端：
 * axios.interceptors.response.use(
 *   res => res,
 *   err => {
 *     message.error(err.response.data.message)
 *     return Promise.reject(err)
 *   }
 * )
 *
 * ★★★ 和 mysql-learning 的区别 ★★★
 * mysql-learning 没有全局异常处理，每个接口自己 try-catch
 * 这里统一处理，Controller 代码更干净
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理参数校验异常（比如：商品不能为空、库存不足）
     *
     * IllegalArgumentException 通常是业务逻辑主动抛出的
     * 比如 OrderServiceImpl 里的：
     *   throw new IllegalArgumentException("商品不能为空");
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<?> handleIllegalArgument(IllegalArgumentException e) {
        return Result.error(400, e.getMessage());
    }

    /**
     * 处理运行时异常（比如：订单不存在、数据库查询出错）
     */
    @ExceptionHandler(RuntimeException.class)
    public Result<?> handleRuntime(RuntimeException e) {
        return Result.error(500, "操作失败：" + e.getMessage());
    }

    /**
     * 兜底：处理所有其他未预料的异常
     *
     * 类比前端：
     * window.onerror = (msg) => { alert('出错了：' + msg) }
     */
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        return Result.error(500, "服务器内部错误：" + e.getMessage());
    }
}
