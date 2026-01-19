package com.campus.delivery.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 用户注册入参DTO
 * 关键修改：
 * 1. 添加public修饰符，确保跨包访问
 * 2. 删除所有手动get/set（@Data自动生成）
 * 3. 校验注解紧贴对应字段上方，规范顺序
 */
@Data // lombok自动生成get/set/toString等，无需手动编写
public class UserRegisterDTO { // 必须加public

    /** 手机号（11位数字，以13-9开头） */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    /** 邮箱（可选，但格式需正确） */
    @Email(message = "邮箱格式不正确")
    private String email;

    /** 密码（必填） */
    @NotBlank(message = "密码不能为空")
    private String password;

    /** 姓名（必填） */
    @NotBlank(message = "姓名不能为空")
    private String name;

    /** 学号（必填） */
    @NotBlank(message = "学号不能为空")
    private String studentId;

    // 注意：删除所有手动编写的get/set方法！
    // @Data会自动为所有private字段生成标准get/set，手动编写会冲突
}