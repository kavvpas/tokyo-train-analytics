package com.tokyo.train.exception;

import com.tokyo.train.model.dto.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // 🌟 核心注解：自动兜底拦截整个项目所有 Controller 抛出的异常！
public class GlobalExceptionHandler {

    /**
     * 捕获未知的系统级大崩溃（如数据库断连、空指针等）
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        System.out.println("🚨 [全局防崩盾牌生效] 拦截到系统未处理异常: " + e.getMessage());
        return Result.error(500, "系统繁忙或网络异常，请稍后再试：" + e.getMessage());
    }

    /**
     * 捕获非法参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        return Result.error(400, "非法请求参数：" + e.getMessage());
    }
}