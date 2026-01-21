package com.example.ai_resume.dto;

import lombok.Data;

import java.util.List;

@Data
public class MatchResultDTO {
    private int score;
    private List<String> matched_skills;

}
