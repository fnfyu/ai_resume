package com.example.ai_resume.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;

    public static <T> ApiResponse<T>success(T data){
        ApiResponse<T> res=new ApiResponse<>();
        res.setCode(200);
        res.setMessage("success");
        res.setData(data);
        return res;
    }

    public static <T> ApiResponse<T> error(String message){
        ApiResponse<T> res=new ApiResponse<>();
        res.setCode(500);
        res.setMessage(message);

        return res;
    }
}
