package com.example.ai_resume.service;

import com.example.ai_resume.entity.User;
import com.example.ai_resume.repository.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class UserService {
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();

    public boolean register(String username, String password) {
        if (userMapper.findByUsername(username) != null){
            return false;
        }

        User user = new User();
        user.setUsername(username);

        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setRole("USER");
        return  userMapper.insert(user);

    }

    public User login(String username,String password){
        User user = userMapper.findByUsername(username);
        if(user==null){
            return null;
        }
        if (!bCryptPasswordEncoder.matches(password,user.getPassword()))
            return null;
        return user;
    }
}
