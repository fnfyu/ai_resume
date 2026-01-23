package com.example.ai_resume.controller;

import com.example.ai_resume.common.response.ApiResponse;
import com.example.ai_resume.service.ResumeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/resume")
@RequiredArgsConstructor
public class ResumeController {
    private final ResumeService resumeService;

    @PostMapping("/upload")
    public ApiResponse<Long> upload(@RequestParam("file") MultipartFile file,
                                                  HttpServletRequest request) {
        Long user_id=(Long) request.getAttribute("user_id");
        if(user_id==null) {
            return ApiResponse.error("请先登录");
        }
        Long resume_id=resumeService.uploadAndAnalyze(user_id,file);
        return ApiResponse.success(resume_id);
    }
}
