package com.example.springboot.exception;

import com.example.springboot.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

//告诉程序，这个异常处理器只负责处理 com.example.springboot.controller 这个 "文件夹" 下的 Controller 抛出的异常。
@ControllerAdvice("com.example.springboot.controller")
public class GlobalExceptionHandler {       //异常处理中心"，所有异常都送到这里来处理
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e) {
        e.printStackTrace();
        return Result.error();
    }

    @ExceptionHandler(CustomerException.class)
    @ResponseBody
    public Result error(CustomerException e) {
        e.printStackTrace();
        return Result.error(e.getCode(),e.getMsg());
    }
}
