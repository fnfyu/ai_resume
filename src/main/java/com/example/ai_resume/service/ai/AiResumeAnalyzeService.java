package com.example.ai_resume.service.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Service
public class AiResumeAnalyzeService {

    @Value("${api.key}")
    private String API_KEY;

    @Value("${api.url}")
    private String API_URL;

    public Map<String,Object> analyze(String resumeText) throws IOException {
        String prompt=buildPrompt(resumeText);

        // ===== 构造请求体 =====
        Map<String,Object> body=Map.of("model","qwen-turbo",
                "input", Map.of(
                        "messages",List.of(
                                Map.of("content",prompt,"role","user")))
                );

        ObjectMapper objectMapper=new ObjectMapper();
        String json=objectMapper.writeValueAsString(body);

        // ===== HTTP 请求 =====
        URL url=new URL(API_URL);
        HttpURLConnection conn = null;
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + API_KEY);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        conn.getOutputStream().write(json.getBytes(StandardCharsets.UTF_8));

            // ===== 读取响应 =====
        Map response = null;
        try {
            response = objectMapper.readValue(conn.getInputStream(), Map.class);

        } catch (IOException e) {
            {
                // 读取阿里返回的错误 JSON
                try (Scanner s = new Scanner(conn.getErrorStream(), StandardCharsets.UTF_8)) {
                    String errorMsg = s.useDelimiter("\\A").hasNext() ? s.next() : "";
                    throw new IOException(errorMsg);
                }
            }
        }

        return response;

        }


    private String buildPrompt(String resumeText) {
        return """
        你是一个资深的计算机行业猎头系统，专门负责高精度的简历解析。
        请从下方提供的简历文本中提取关键信息。
        仅返回一个标准的 JSON 字符串，不要包含任何解释性文字、不要包含 Markdown 代码块标记（如 ```json）。
        skills 字段应包含编程语言、框架、工具等核心关键词。
        projects 数组中，tech 字段需识别该项目使用的具体技术栈，desc 需简洁总结项目职责。
        如果简历中缺失某项内容，对应字段返回空数组 [] 或空字符串 ""，严禁虚构。
        保持简历原始语言。
        JSON 结构模板：
                JSON
                {
                  "skills": [],
                  "projects": [
                    {
                      "name": "",
                      "tech": [],
                      "desc": ""
                    }
                  ],
                  "summary": ""
                }
        待解析简历内容：
        """ + resumeText;
    }

}
