package com.example.ai_resume.repository;

import com.example.ai_resume.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO user(username,password,role) values (#{username},#{password},#{role})")
    boolean insert(User user);

    @Select("SELECT * FROM user WHERE username=#{username}")
    User findByUsername(String username);
}
