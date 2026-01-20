package com.example.ai_resume.entity;

import lombok.Data;

import java.time.LocalDate;

@Data
public class User {

    private Long id;
    private String username;
    private String password;
    private String role;
    private LocalDate create_time;
}
