package com.tokyo.train.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    private Integer code;    // 状态码：200 成功，500 服务器错误，400 参数错误
    private String message;  // 提示信息
    private T data;          // 真实的业务数据载荷

    // 快捷成功方法
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    // 快捷失败方法
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message, null);
    }
}