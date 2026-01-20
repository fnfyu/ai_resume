package com.example.ai_resume.repository;

import com.example.ai_resume.entity.Resume;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ResumeMapper {
    @Insert("INSERT INTO resume(user_id, file_name, file_path) VALUES (#{user_id},#{file_name},#{file_path})")
    boolean insert(Resume resume);
}
