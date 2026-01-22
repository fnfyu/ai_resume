package com.example.ai_resume.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.ai_resume.common.response.ApiResponse;
import com.example.ai_resume.dto.MatchResultDTO;
import com.example.ai_resume.entity.Job;
import com.example.ai_resume.repository.JobMapper;
import com.example.ai_resume.repository.MatchResultMapper;
import com.example.ai_resume.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/job")
@RequiredArgsConstructor
public class JobController {

    private final JobMapper jobMapper;
    private final MatchService matchService;
    private final MatchResultMapper matchResultMapper;

    @PostMapping("/create")
    public ApiResponse<Job> create(@RequestBody Job job) {
        jobMapper.insert(job);
        return ApiResponse.success(job);
    }

    @PostMapping("/match")
    public ApiResponse<Map<String,Object>> match(@RequestParam long resume_id,
                                   @RequestParam long job_id){
        Job job=jobMapper.selectOne(Wrappers.<Job>lambdaQuery().eq(Job::getId,job_id));

        //ai分析的技能
        List<String>resume_skills=List.of("Java","Spring Boot","MySQL");

        MatchResultDTO dto =matchService.matchAndSave(resume_id,job_id,resume_skills,job.getSkills());

        return ApiResponse.success(Map.of(
                "score",dto.getScore(),
                "matched",dto.getMatched_skills()
        ));
    }
}
