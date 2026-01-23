package com.example.ai_resume.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResumeStatus {
    PENDING(0, "待处理"),
    PROCESSING(1, "分析中"),
    COMPLETED(2, "完成"),
    FAILED(3, "失败");

    @EnumValue
    private final int code;
    @JsonValue
    private final String desc;


}
