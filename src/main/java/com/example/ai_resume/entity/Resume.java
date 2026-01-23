package com.example.ai_resume.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.example.ai_resume.common.enums.ResumeStatus;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@TableName(value = "resume", autoResultMap = true) // 必须开启 autoResultMap 以支持 JSON 解析
public class Resume {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long user_id;
    private String file_name;
    private String file_path;
    private String rawText;
    /**
     * AI 分析后的结构化结果
     * 使用 JacksonTypeHandler 自动实现 Map <-> JSON 的转换
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> analysisResult;

    /**
     * 分析状态: 0-待处理, 1-分析中, 2-完成, 3-失败
     */
    private ResumeStatus status;

    /**
     * 插入时自动填充 (MyBatis-Plus 功能)
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime uploadTime;

    /**
     * 插入和更新时自动填充
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}