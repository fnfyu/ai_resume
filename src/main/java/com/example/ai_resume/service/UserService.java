package com.example.ai_resume.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.ai_resume.common.exception.BusinessException;
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

    public User register(String username, String password) {
        User user = new User();
        user.setUsername(username);

        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setRole("USER");
        try {
            return user;
        }
        catch(DuplicateKeyException e){
            throw new RuntimeException("用户已存在");
        }
    }

    public User login(String username,String password){
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername,username));
        if(user==null){
            throw new BusinessException("用户不存在");
        }
        if (!bCryptPasswordEncoder.matches(password,user.getPassword()))
            throw new BusinessException("密码错误");
        return user;
    }
}
