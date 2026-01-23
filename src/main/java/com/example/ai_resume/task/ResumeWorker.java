package com.example.ai_resume.task;

import com.example.ai_resume.common.enums.ResumeStatus;
import com.example.ai_resume.common.exception.BusinessException;
import com.example.ai_resume.service.ResumeService;
import com.example.ai_resume.service.ai.AiResumeAnalyzeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ResumeWorker {

    private final StringRedisTemplate redisTemplate;


    private final AiResumeAnalyzeService aiResumeAnalyzeService;

    private final ObjectMapper objectMapper;

    private final ResumeService resumeService;

    @Scheduled(fixedDelayString = "${resume.task.delay:1000}")
    public void processTask(){
        String resumeId=redisTemplate.opsForList().rightPop("resume_task_queue");
        if (resumeId!=null){
            Long resume_id = Long.valueOf(resumeId);
            try {
                Map<String, Object> res = aiResumeAnalyzeService.analyze(resume_id);
                // 2. 存入数据库
                resumeService.updateResult(resume_id, res, ResumeStatus.COMPLETED);

                redisTemplate.opsForValue().set("resume_cache:" + resume_id, objectMapper.writeValueAsString(res), Duration.ofHours(1));

            }
            catch (Exception e) {
                resumeService.updateResult(resume_id,null,ResumeStatus.FAILED);
                throw new BusinessException(e.getMessage());
            }
        }



    }
}
