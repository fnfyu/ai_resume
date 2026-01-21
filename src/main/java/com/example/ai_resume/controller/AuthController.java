package com.example.ai_resume.controller;

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
    public Map<String, Object> register(@RequestParam String username,
                         @RequestParam String password){
        Map<String,Object> res=new HashMap<>();
        try{
            res.put("success",userService.register(username, password));
        } catch (Exception e) {
            res.put("success", false);
            res.put("error", e.getMessage());
        }
        return res;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestParam String username,
                                      @RequestParam String password){
        User user=userService.login(username, password);
        Map<String,Object> res=new HashMap<>();
        if (user==null){
            res.put("success",false);
        }
        res.put("success",true);
        res.put("token",JwtUtil.generateToken(user.getId(), user.getUsername()));
        return res;
    }
}
