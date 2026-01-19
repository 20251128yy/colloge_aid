package com.campus.delivery.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 用户表
 * 关键修改：
 * 1. 删除所有手动get/set（@Data自动生成）
 * 2. 移除重复的import导入
 * 3. 简化@Id注解写法
 */
@Data // lombok自动生成get/set/toString/equals等，无需手动编写
@Entity
@Table(name = "user")
public class User {

    @Id // 简化写法（已导入jakarta.persistence.*，无需加全路径）
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone", unique = true)
    private String phone;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "student_id")
    private String studentId;

    @Column(name = "identity_type")
    private Integer identityType; // 1-学生, 2-管理员

    @Column(name = "audit_status")
    private Integer auditStatus; // 0-待审核, 1-审核通过, 2-审核拒绝

    @Column(name = "current_role")
    private Integer currentRole; // 1-需求方, 2-派送方

    @Column(name = "point_balance")
    private Integer pointBalance; // 当前积分余额

    @Column(name = "frozen_points")
    private Integer frozenPoints; // 冻结积分

    @Column(name = "status")
    private Integer status; // 1-启用, 0-禁用

    @CreationTimestamp
    @Column(name = "create_time")
    private LocalDateTime createTime;

    @UpdateTimestamp
    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;

    // 注意：删除所有手动编写的get/set方法！
    // @Data注解会自动生成，手动编写会导致编译冲突
}