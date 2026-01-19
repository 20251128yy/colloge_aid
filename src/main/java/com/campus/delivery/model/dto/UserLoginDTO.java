package com.campus.delivery.model.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

/**
 * 用户登录入参DTO
 * 修复点：
 * 1. 加public修饰符，支持跨包访问
 * 2. 删除手动get/set（@Data自动生成）
 * 3. 补充校验注解，避免空账号/密码
 */
@Data // lombok自动生成get/set/toString等，无需手动编写
public class UserLoginDTO { // 必须加public

    /** 登录账号（手机号/邮箱） */
    @NotBlank(message = "账号不能为空")
    private String account;

    /** 登录密码 */
    @NotBlank(message = "密码不能为空")
    private String password;

    // 注意：删除所有手动编写的get/set方法！
}