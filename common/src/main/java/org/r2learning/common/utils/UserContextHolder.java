package org.r2learning.common.utils;

import org.r2learning.common.domain.user.UserInfo;

public class UserContextHolder {

    // 存储当前用户信息的ThreadLocal
    private static final ThreadLocal<UserInfo> USER_CONTEXT = new ThreadLocal<>();

    /**
     * 设置当前用户信息
     */
    public static void setUserInfo(UserInfo userInfo) {
        USER_CONTEXT.set(userInfo);
    }

    /**
     * 获取当前用户信息
     */
    public static UserInfo getUserInfo() {
        return USER_CONTEXT.get();
    }

    /**
     * 获取当前用户ID
     */
    public static Long getCurrentUserId() {
        UserInfo userInfo = USER_CONTEXT.get();
        return userInfo != null ? userInfo.getUserId() : null;
    }

    /**
     * 获取当前用户名
     */
    public static String getCurrentUsername() {
        UserInfo userInfo = USER_CONTEXT.get();
        return userInfo != null ? userInfo.getUsername() : null;
    }

    /**
     * 清理当前用户信息（非常重要，避免内存泄漏）
     */
    public static void clear() {
        USER_CONTEXT.remove();
    }
}
