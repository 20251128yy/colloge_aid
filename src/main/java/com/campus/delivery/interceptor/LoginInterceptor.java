package com.campus.delivery.interceptor;

import com.campus.delivery.common.Result;
import com.campus.delivery.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 登录认证拦截器
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;  // 注入JwtUtil

    // 不需要登录认证的接口
    private static final String[] WHITE_LIST = {
            "/api/user/register",
            "/api/user/login",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/webjars/**"
    };

    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        // 获取请求URI
//        String uri = request.getRequestURI();
//
//        // 检查是否在白名单中
//        for (String whitePath : WHITE_LIST) {
//            if (uri.startsWith(whitePath)) {
//                return true;
//            }
//        }
//
//        // 获取Authorization头
//        String authHeader = request.getHeader("Authorization");
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            sendUnauthorized(response, "请先登录");
//            return false;
//        }
//
//        // 提取令牌
//        String token = authHeader.substring(7);
//
//        // 验证令牌
//        if (!jwtUtil.validateToken(token)) {  // 使用注入的jwtUtil
//            sendUnauthorized(response, "令牌无效或已过期");
//            return false;
//        }
//
//        // 令牌有效，继续处理请求
//        return true;
//    }
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 开发阶段：放行所有请求
        System.out.println("拦截器放行: " + request.getRequestURI());
        return true;
    }
    private void sendUnauthorized(HttpServletResponse response, String message) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write(new ObjectMapper().writeValueAsString(Result.unauthorized(message)));
        writer.flush();
    }
}