package com.example.ai_resume.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.util.Date;

public class JwtUtil {
    private static final String SECRET = "s-e-c-r-e-t-k-e-y-ai_resume_secret_key";//私钥
    private static final long EXPIRE_TIME = 60 * 60 * 1000;//1小时

    public static String generateToken(Long userID,String username){
        return Jwts.builder().
                setSubject(username).//设置主题，说明这个 Token “属于谁”
                claim("userID",userID).//设置自定义负载，除了标准字段外，你可以往 Token 里塞入任何你需要的非敏感信息。这里是存入了一个名为 userID 的键值对。
                setExpiration(new Date(System.currentTimeMillis()+EXPIRE_TIME)).//设置过期时间
                signWith(Keys.hmacShaKeyFor(SECRET.getBytes())).//数字签名 保证安全
                compact();
    }
}
