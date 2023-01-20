package com.ljq.handler.exception;

import com.ljq.domain.ResponseResult;
import com.ljq.enums.AppHttpCodeEnum;
import com.ljq.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(SystemException.class)
    public ResponseResult systemExceptionHandler(SystemException e){
        //打印异常方便追溯
        log.info("出现了异常！{}",e);
        //响应给前端
        return ResponseResult.errorResult(e.getCode(), e.getMsg());
    }


    @ExceptionHandler(Exception.class)
    public ResponseResult systemExceptionHandler(Exception e){
        //打印异常方便追溯
        log.info("出现了异常！{}",e);
        //响应给前端
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }
}
