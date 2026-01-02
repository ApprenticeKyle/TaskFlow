package org.r2learning.common.infrastructure.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.r2learning.common.domain.user.UserInfo;
import org.r2learning.common.utils.UserContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

public class UserInfoInterceptor implements HandlerInterceptor {

    // 请求头名称常量
    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String USERNAME_HEADER = "X-Username";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) {
        // 从请求头获取用户信息
        String userIdStr = request.getHeader(USER_ID_HEADER);
        String username = request.getHeader(USERNAME_HEADER);

        // 创建UserInfo对象并存储到ThreadLocal
        if (userIdStr != null) {
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(Long.parseLong(userIdStr));
            userInfo.setUsername(username);
            UserContextHolder.setUserInfo(userInfo);
        }

        return true; // 继续执行后续流程
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        // 清理ThreadLocal，避免内存泄漏
        UserContextHolder.clear();
    }
}