package com.example.ai_resume.common.exception;

import com.example.ai_resume.common.response.ApiResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ApiResponse<?> handleBusiness(BusinessException e) {
        return ApiResponse.error(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handle(Exception e){
        return ApiResponse.error("系统内部错误");
    }
}
