package com.example.ai_resume.controller;

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
    public Map<String, Object> upload(@RequestParam("file") MultipartFile file,
                                      HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        Long user_id=(Long) request.getAttribute("user_id");
        try {
            Map<String,Object> ai_result =resumeService.uploadAndAnalyze(user_id,file);
            result.put("success", true );
            result.put("ai_result", ai_result);
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        return result;
    }
}
