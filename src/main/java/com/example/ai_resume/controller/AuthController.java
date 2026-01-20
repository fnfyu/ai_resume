package com.example.ai_resume.controller;

import com.example.ai_resume.entity.User;
import com.example.ai_resume.service.UserService;
import com.example.ai_resume.util.JwtUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public User register(@RequestParam String username,
                         @RequestParam String password){
        return userService.register(username, password);
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                      @RequestParam String password){
        User user=userService.login(username, password);
        return JwtUtil.generateToken(user.getId(), user.getUsername());
    }
}
