package com.example.ai_resume.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("job")
public class Job {
    @TableId(type= IdType.AUTO)
    private Long id;
    private String title;
    private String description;
    private String skills;
}
