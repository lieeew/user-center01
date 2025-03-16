package com.dashi.usercenter.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 *
 * @param <T>
 */
@Data
public class BaseResponse<T> implements Serializable {
    private int code;

    private T date;

    private String message;

    private String description;


    public BaseResponse(int code, T date, String message, String description) {
        this.code = code;
        this.date = date;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(int code, T date, String message) {
        this(code,date,message,"");
    }

    public BaseResponse(int code, T date) {
        this(code,date,"","");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(),null,errorCode.getMessage(),errorCode.getDescription());
    }
}
