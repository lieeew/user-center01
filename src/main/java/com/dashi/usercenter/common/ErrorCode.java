package com.dashi.usercenter.common;

/**
 * 错误码
 */
public enum ErrorCode {
    SUCCESS( 0, "success",  ""),
    PARAMS_ERROR( 4000, "请求参数错误",  ""),
    NULL_ERROR( 4001, "请求参数为空", ""),
    NO_LOGIN(40100, "未登录", ""),
    NO_AUTH( 40101, "无权限", ""),
    SYSTEM_ERROR(50000,"系统内部异常", "");


    private int code;

    /**
     * code message
     */
    private String message;

    /**
     * code description
     */
    private String description;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }

    ErrorCode(int code, String message, String description) {
        this.description = description;
        this.message = message;
        this.code = code;
    }
}
