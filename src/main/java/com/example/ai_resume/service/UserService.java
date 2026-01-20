package com.example.ai_resume.service;

import com.example.ai_resume.entity.User;
import com.example.ai_resume.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole("USER");
        return userRepository.save(user);
    }

    public User login(String username,String password){
        User user =userRepository.findByUsername(username).
                orElseThrow(()-> new RuntimeException("用户不存在"));

        if (!user.getPassword().equals(password))
            throw new RuntimeException("密码错误");

        return user;
    }
}
