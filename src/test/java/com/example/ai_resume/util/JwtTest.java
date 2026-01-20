package com.example.ai_resume.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JwtTest {

    private static final String SECRET =
            "ai_resume_secret_key_ai_resume_secret_key";

    public static void main(String[] args) {

        String token = Jwts.builder()
                .setSubject("testUser")
                .claim("userId", 1L)
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .compact();

        System.out.println("生成的 token：");
        System.out.println(token);

        Claims claims = Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();

        System.out.println("解析结果：");
        System.out.println(claims);
    }
}
