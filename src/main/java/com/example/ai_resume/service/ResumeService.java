package com.example.ai_resume.service;

import com.example.ai_resume.common.exception.BusinessException;
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
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ResumeService {
    private final ResumeMapper resumeMapper;
    private final AiResumeAnalyzeService aiResumeAnalyzeService;
    private final StringRedisTemplate redisTemplate;

    @Value("${file.upload.dir}")
    private String UPLOAD_DIR;

    public Map<String,Object>analyzeAndCache(Long resume_id, String text)  {
        String key="resume_id"+resume_id;
        String cached=(String) redisTemplate.opsForValue().get(key);

        if (cached!=null) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(cached, Map.class);
        }

        Map<String,Object> map= null;
        try {
            map = aiResumeAnalyzeService.analyzeAsyc(text).get();
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
        redisTemplate.opsForValue().
                set(key,new ObjectMapper().writeValueAsString(map),1, TimeUnit.HOURS);

        return map;

    }

    public Map<String,Object> uploadAndAnalyze(Long userId, MultipartFile file)  {
        String text=saveFileAndParseText(userId, file);
        try {
            return aiResumeAnalyzeService.analyzeAsyc(text).get();
        }
        catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }

    public String saveFileAndParseText(Long user_id, MultipartFile file)  {
        //确保文件存在
        File dir=new File(UPLOAD_DIR);
        if (!dir.exists()) dir.mkdirs();

        //保存文件
        String file_name = System.currentTimeMillis()+"_"+file.getOriginalFilename();
        File dest = new File(dir, file_name);
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            throw new BusinessException("服务器存储空间异常，请联系管理员或稍后重试");
        }

        Resume resume = new Resume();
        resume.setUser_id(user_id);
        resume.setFile_name(file.getOriginalFilename());
        resume.setFile_path(dest.getAbsolutePath());
        resumeMapper.insert(resume);

        try {
            return parseFile(dest);
        } catch (Exception e) {
            throw new BusinessException("简历内容识别失败，请检查文件是否损坏或是否包含文本");
        }
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
}
