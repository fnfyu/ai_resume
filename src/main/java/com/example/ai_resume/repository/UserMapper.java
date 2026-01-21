package com.example.ai_resume.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.ai_resume.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
