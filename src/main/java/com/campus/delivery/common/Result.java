package com.campus.delivery.common;

import lombok.Data;

/**
 * 统一返回结果类
 */
@Data
public class Result<T> {
    private int code;
    private String msg;
    private T data;

    /**
     * 成功返回
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMsg("成功");
        result.setData(data);
        return result;
    }

    /**
     * 成功返回（无数据）
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * 失败返回
     */
    public static <T> Result<T> error(int code, String msg) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(null);
        return result;
    }

    /**
     * 参数错误
     */
    public static <T> Result<T> badRequest(String msg) {
        return error(400, msg);
    }

    /**
     * 未授权
     */
    public static <T> Result<T> unauthorized(String msg) {
        return error(401, msg);
    }

    /**
     * 权限不足
     */
    public static <T> Result<T> forbidden(String msg) {
        return error(403, msg);
    }

    /**
     * 服务器异常
     */
    public static <T> Result<T> serverError(String msg) {
        return error(500, msg);
    }
}
