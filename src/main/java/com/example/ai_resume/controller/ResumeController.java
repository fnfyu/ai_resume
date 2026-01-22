package com.example.ai_resume.controller;

import com.example.ai_resume.common.response.ApiResponse;
import com.example.ai_resume.service.ResumeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/resume")
@RequiredArgsConstructor
public class ResumeController {
    private final ResumeService resumeService;

    @PostMapping("/upload")
    public ApiResponse<Map<String, Object>> upload(@RequestParam("file") MultipartFile file,
                                                  HttpServletRequest request) {
        Long user_id=(Long) request.getAttribute("user_id");
        Map<String,Object> ai_result =resumeService.uploadAndAnalyze(user_id,file);
        return ApiResponse.success(ai_result);
    }
}
