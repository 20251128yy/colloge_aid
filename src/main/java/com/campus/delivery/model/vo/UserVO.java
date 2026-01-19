package com.campus.delivery.model.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 用户返回视图对象（VO）
 * 修复点：
 * 1. 添加public修饰符，支持跨包访问
 * 2. 补充字段注释，明确状态枚举含义
 * 3. 隐藏敏感字段（如密码），仅暴露前端需要的信息
 */
@Data // lombok自动生成get/set，无需手动编写
public class UserVO { // 必须添加public修饰符

    // 基础信息
    private Long id;
    private String phone;
    private String email;
    private String name;
    private String studentId;

    // 身份/状态相关（补充注释，方便前后端对接）
    private Integer identityType; // 1-学生, 2-管理员
    private Integer auditStatus;   // 0-待审核, 1-审核通过, 2-审核拒绝
    private Integer currentRole;   // 1-需求方, 2-派送方
    private Integer status;        // 1-启用, 0-禁用

    // 积分相关
    private Integer pointBalance;  // 当前积分余额
    private Integer frozenPoints;  // 冻结积分

    // 时间相关
    private LocalDateTime createTime;      // 创建时间
    private LocalDateTime lastLoginTime;   // 最后登录时间
}