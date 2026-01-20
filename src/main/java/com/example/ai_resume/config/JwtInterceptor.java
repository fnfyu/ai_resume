package com.example.ai_resume.config;

import com.example.ai_resume.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
public class JwtInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String auth=request.getHeader("Authorization");
        boolean success=false;
        if(auth!=null && auth.startsWith("Bearer ")){
            String token=auth.substring(7);
            Map<String,Object> map=JwtUtil.decodeToken(token);
            success=(boolean)map.get("success");
            if(success){
                String username=(String)map.get("username");
                request.setAttribute("username",username);
                Long user_id=(Long)map.get("user_id");
                request.setAttribute("user_id",user_id);
            }
            else {
                ((HttpServletResponse)response).sendError(HttpServletResponse.SC_UNAUTHORIZED,map.get("error").toString());
            }

        }
        else
            ((HttpServletResponse)response).sendError(HttpServletResponse.SC_UNAUTHORIZED,"请先登录");
        return success;
    }
}
