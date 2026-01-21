package com.example.ai_resume.service;

import com.example.ai_resume.dto.MatchResultDTO;
import com.example.ai_resume.entity.MatchResult;
import com.example.ai_resume.repository.MatchResultMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchResultMapper matchResultMapper;

    public MatchResultDTO matchAndSave(
            Long resume_id,
            Long job_id,
            List<String> resumeSkills,
            String jobSkills
    ){

        MatchResultDTO res = match(resumeSkills, jobSkills);

        MatchResult mr = new MatchResult();
        mr.setResume_id(resume_id);
        mr.setJob_id(job_id);
        mr.setScore(res.getScore());
        String matchedSkills=String.join(",",res.getMatched_skills());
        matchResultMapper.insert(mr);

        return res;
    }

    public MatchResultDTO match(List<String>resumeSkills, String jobSkills){
        Set<String> jobSkillSet= Arrays.stream(jobSkills.split(",")).
                map(this::normalize).
                filter(s->!s.isEmpty()).
                collect(Collectors.toSet());

        resumeSkills=resumeSkills.stream().map(this::normalize).toList();

        List<String>matched=new ArrayList<>();
        for(String resumeSkill:resumeSkills){
            if (jobSkillSet.contains(resumeSkill)){
                matched.add(resumeSkill);
            }
        }

        int score= (int)((double) matched.size() / jobSkillSet.size()*100.0);

        MatchResultDTO matchResultDTO=new MatchResultDTO();
        matchResultDTO.setScore(score);
        matchResultDTO.setMatched_skills(matched);

        return matchResultDTO;

    }

    public String normalize(String skills){
        return skills.toLowerCase().
                replaceAll("\\s+","").
                replaceAll("-","").
                replaceAll("_","");
    }

}
