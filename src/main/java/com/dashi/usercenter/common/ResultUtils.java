package com.dashi.usercenter.common;

/**
 * 返回工具类
 */


public class ResultUtils {

    /**
     * success
     * @param data
     * @return
     * @param <T>
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "success");
    }

    /**
     * failed
     * @param errorCode
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    /**
     *
     * @param code
     * @param message
     * @param description
     * @return
     */
    public  static BaseResponse error(int code, String message, String description) {
        return new BaseResponse<>(code, null, message, description);
    }

    /**
     *
     * @param errorCode
     * @param message
     * @param description
     * @return
     */
    public  static BaseResponse error(ErrorCode errorCode, String message, String description) {
        return new BaseResponse<>(errorCode.getCode(), null, message, description);
    }

    /**
     *
     * @param errorCode
     * @param description
     * @return
     */
    public  static BaseResponse error(ErrorCode errorCode, String description) {
        return new BaseResponse<>(errorCode.getCode(), errorCode.getMessage(), description);
    }


}
