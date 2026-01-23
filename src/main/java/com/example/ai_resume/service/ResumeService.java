package com.example.ai_resume.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.ai_resume.common.enums.ResumeStatus;
import com.example.ai_resume.common.exception.BusinessException;
import com.example.ai_resume.common.response.ApiResponse;
import com.example.ai_resume.entity.Resume;
import com.example.ai_resume.repository.ResumeMapper;
import com.example.ai_resume.service.ai.AiResumeAnalyzeService;
import com.example.ai_resume.util.ResumeTextCleaner;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ResumeService {
    private final ResumeMapper resumeMapper;
    private final StringRedisTemplate redisTemplate;

    @Value("${file.upload.dir}")
    private String UPLOAD_DIR;

    public void updateResult(Long resume_id,Map<String,Object> result,ResumeStatus status)  {
        // 创建一个新的实体对象，只设置 ID 和需要更新的字段
        Resume resume = new Resume();
        resume.setId(resume_id);
        resume.setAnalysisResult(result); // 此时会正确触发 JacksonTypeHandler
        resume.setStatus(status);

        // 使用 updateById，MyBatis-Plus 会自动根据 ID 更新不为 null 的字段
        resumeMapper.updateById(resume);
    }

//    public Map<String,Object>analyzeAndCache(Long resume_id, String text)  {
//        String key="resume_id:"+resume_id;
//        String cached=(String) redisTemplate.opsForValue().get(key);
//
//        if (cached!=null) {
//            ObjectMapper mapper = new ObjectMapper();
//            return mapper.readValue(cached, Map.class);
//        }
//
//        Map<String,Object> map= null;
//        try {
//            map = aiResumeAnalyzeService.analyzeAsyc(text).get();
//        } catch (Exception e) {
//            throw new BusinessException(e.getMessage());
//        }
//        redisTemplate.opsForValue().
//                set(key,new ObjectMapper().writeValueAsString(map),1, TimeUnit.HOURS);
//
//        return map;
//
//    }

    public Long uploadAndAnalyze(Long userId, MultipartFile file)  {

        try {
            Long resume_id=saveFileAndParseText(userId, file);
            redisTemplate.opsForList().leftPush("resume_task_queue", String.valueOf(resume_id));
            return resume_id;
        }
        catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    public Long saveFileAndParseText(Long user_id, MultipartFile file)  {
        //确保文件存在
        File dir=new File(UPLOAD_DIR);
        if (!dir.exists()) dir.mkdirs();

        //保存文件
        String file_name = System.currentTimeMillis()+"_"+file.getOriginalFilename();
        File dest = new File(dir, file_name);
        try {
            file.transferTo(dest);
        } catch (Exception e) {
            throw new BusinessException("服务器存储空间异常，请联系管理员或稍后重试");
        }

        Resume resume = new Resume();
        resume.setUser_id(user_id);
        resume.setFile_name(file.getOriginalFilename());
        resume.setFile_path(dest.getAbsolutePath());
        resume.setStatus(ResumeStatus.PENDING);

        try {
            resume.setRawText(parseFile(dest));
        } catch (Exception e) {
            throw new BusinessException("简历内容识别失败，请检查文件是否损坏或是否包含文本");
        }
        resumeMapper.insert(resume);

        return resume.getId();
    }


    private String parseFile(File file) throws Exception {
        String name=file.getName().toLowerCase();
        if (name.endsWith(".pdf")){
            try(PDDocument document = Loader.loadPDF(file)) {
                PDFTextStripper stripper = new PDFTextStripper();
                //提取文本
                return stripper.getText(document);
            }
        }else if (name.endsWith(".docx")){
            try (FileInputStream fis = new FileInputStream(file);
                XWPFDocument document = new XWPFDocument(fis);
                XWPFWordExtractor extractor = new XWPFWordExtractor(document)) {
                return extractor.getText();
            }
        } else if (name.endsWith(".doc")) {
            try (FileInputStream fis = new FileInputStream(file);
                 HWPFDocument document = new HWPFDocument(fis);
                 WordExtractor extractor = new WordExtractor(document)) {
                return ResumeTextCleaner.cleanResumeText(extractor.getText());

            }
            
        }
        else {
            throw new BusinessException("不支持的文件类型");
        }
    }

    public List<String> getSkills(Long resume_id) {
        Resume resume=resumeMapper.selectById(resume_id);

        if(resume==null){
            throw new BusinessException("未找到 "+resume_id +" 号简历信息");
        }

        if (resume.getStatus() != ResumeStatus.COMPLETED) {
            throw new BusinessException("简历未分析完成，请稍后再试");
        }
        return (List<String>) resume.getAnalysisResult().get("skills");
    }
}
