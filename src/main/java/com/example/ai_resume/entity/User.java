package com.example.ai_resume.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
//对应数据库中表名
@Table(name="user")
public class User {

    //主键
    @Id
    //自增
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String role;
}
