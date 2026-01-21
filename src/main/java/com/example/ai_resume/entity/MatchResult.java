package com.example.ai_resume.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("match_result")
public class MatchResult {
    @TableId(type= IdType.AUTO)
    private Long id;
    private Long resume_id;
    private Long job_id;
    private int score;
    private String skills;

}
