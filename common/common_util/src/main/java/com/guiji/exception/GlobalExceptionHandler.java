package com.guiji.exception;

import com.guiji.result.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result handlerException(Exception e){
        e.printStackTrace();
        return Result.fail("服务器故障,请稍后再试!!!");
    }

    @ExceptionHandler(YyghException.class)
    public Result customExceptionHandler(YyghException e){
        return Result.build(e.getCode(), e.getMessage());
    }
}
