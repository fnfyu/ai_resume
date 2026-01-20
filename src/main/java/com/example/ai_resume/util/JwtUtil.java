package com.example.ai_resume.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    public static Map<String,Object> decodeToken(String token) {
        Map<String,Object> map=new HashMap<>();
        try {
            Claims claims = Jwts.parser().
                    verifyWith(Keys.hmacShaKeyFor(SECRET.getBytes())).
                    build().parseSignedClaims(token).
                    getPayload();
            String username=claims.getSubject();
            Long userID=claims.get("userID",Long.class);
            map.put("success",true);
            map.put("userID",userID);
            map.put("username",username);

        } catch (ExpiredJwtException e) {
            map.put("success",false);
            map.put("error","登录已过期，请重新登录");
        }
        catch (JwtException e) {
            map.put("success",false);
            map.put("error","无效的Token");
        }catch (Exception e) {
            map.put("success",false);
            map.put("error","解析Token时发生未知错误");
        }

        return map;

    }
}
