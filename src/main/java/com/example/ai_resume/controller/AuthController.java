package com.example.ai_resume.controller;

import com.example.ai_resume.common.response.ApiResponse;
import com.example.ai_resume.entity.User;
import com.example.ai_resume.service.UserService;
import com.example.ai_resume.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public ApiResponse<User> register(@RequestParam String username,
                                @RequestParam String password){
        return ApiResponse.success(userService.register(username, password));
    }

    @PostMapping("/login")
    public ApiResponse<String> login(@RequestParam String username,
                                      @RequestParam String password){
        User user=userService.login(username, password);
        return ApiResponse.success(JwtUtil.generateToken(user.getId(), user.getUsername()));

    }
}
