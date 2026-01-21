package com.example.ai_resume.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.ai_resume.entity.User;
import com.example.ai_resume.repository.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class UserService {
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();

    public boolean register(String username, String password) {
        User user = new User();
        user.setUsername(username);

        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setRole("USER");
        try {
            return userMapper.insert(user) > 0;
        }
        catch(DuplicateKeyException e){
            throw new DuplicateKeyException("用户已存在");
        }
    }

    public User login(String username,String password){
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername,username));
        if(user==null){
            return null;
        }
        if (!bCryptPasswordEncoder.matches(password,user.getPassword()))
            return null;
        return user;
    }
}
