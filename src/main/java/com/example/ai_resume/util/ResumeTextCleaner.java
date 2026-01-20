package com.example.ai_resume.util;

public class ResumeTextCleaner {
    public static String cleanResumeText(String rawText) {
        if (rawText == null || rawText.isEmpty()) {
            return "";
        }
        // 1. 去除 ASCII 控制字符 (0-31 以及 127)，这些在 .doc 提取中非常多，AI 看不懂
        String text = rawText.replaceAll("[\\x00-\\x08\\x0b\\x0c\\x0e-\\x1f\\x7f]", "");

        // 2. 将连续的空白字符（空格、换行、制表符）统一替换为单个空格或标准换行
        // 注意：简历解析需要保留换行来区分模块，所以我们先处理多余的水平空格
        text = text.replaceAll("[ \\t\\x0B\\f]+", " ");

        // 3. 处理连续的换行符，将 3 个以上的连续换行压缩为 2 个（保持段落感同时节省 Token）
        text = text.replaceAll("(\\r?\\n){3,}", "\n\n");

        // 4. 去除一些常见的 .doc 解析乱码（如特殊的方块字符、问号字符）
        // 匹配大部分非打印字符和非法 Unicode
        text = text.replaceAll("[\\x00-\\x1F\\x7F-\\x9F\\u200B-\\u200D\\uFEFF]", "");

        // 5. 去除首尾空白
        return text.trim();
    }
}
