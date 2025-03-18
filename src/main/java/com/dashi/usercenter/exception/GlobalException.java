package com.dashi.usercenter.exception;

import com.dashi.usercenter.common.BaseResponse;
import com.dashi.usercenter.common.ErrorCode;
import com.dashi.usercenter.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author leikooo
 * @create 2023-07-07 8:38
 * @Package com.yupi.user_center.exception
 * @Description 全局异常处理类
 */
@RestControllerAdvice
@Slf4j
public class GlobalException {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse businessException(BusinessException e) {
        log.info("businessException", e);
        // 从 BusinessException 里面取出来 code
        return ResultUtils.error(e.getCode(), e.getMessage(), e.getDescription());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse runtimeException(RuntimeException e) {
        log.info("runtimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, e.getMessage(), e.getMessage());
    }

}
