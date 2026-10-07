package com.jiankang.common.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiankang.common.Result;
import com.jiankang.common.ResultCode;
import com.jiankang.common.annotation.RequireRole;
import com.jiankang.common.util.JwtUtil;
import com.jiankang.common.util.UserContext;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * 鉴权拦截器：登录状态维持 + 角色权限控制
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Resource
    private JwtUtil jwtUtil;

    @Value("${jwt.header}")
    private String header;

    @Value("${jwt.prefix}")
    private String prefix;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        // CORS 预检请求直接放行
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }

        String token = request.getHeader(header);
        if (token == null || !token.startsWith(prefix)) {
            write(response, Result.error(ResultCode.UNAUTHORIZED));
            return false;
        }

        token = token.substring(prefix.length()).trim();
        try {
            Claims claims = jwtUtil.parseToken(token);
            UserContext.setUserId(JwtUtil.getUserId(claims));
            UserContext.setRole(JwtUtil.getRole(claims));
        } catch (Exception e) {
            UserContext.clear();
            write(response, Result.error(ResultCode.UNAUTHORIZED));
            return false;
        }

        // 角色权限校验
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            RequireRole requireRole = handlerMethod.getMethodAnnotation(RequireRole.class);
            if (requireRole == null) {
                requireRole = handlerMethod.getBeanType().getAnnotation(RequireRole.class);
            }
            if (requireRole != null && !Arrays.asList(requireRole.value()).contains(UserContext.getRole())) {
                write(response, Result.error(ResultCode.FORBIDDEN));
                return false;
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 清理线程变量，避免内存泄漏
        UserContext.clear();
    }

    private void write(HttpServletResponse response, Result<?> result) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
