package com.example.ai_resume.entity;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Resume {
    private long id;
    private Long user_id;
    private String file_name;
    private String file_path;
    private LocalDate upload_time;
}
