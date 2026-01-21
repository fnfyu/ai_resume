package com.example.ai_resume.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.ai_resume.entity.MatchResult;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MatchResultMapper extends BaseMapper<MatchResult> {
}
