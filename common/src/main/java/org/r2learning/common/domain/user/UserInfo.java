package org.r2learning.common.domain.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 跨模块通用的用户信息实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    private Long userId;
    private String username;
    // 可以根据需要添加更多字段，如角色、部门等
}