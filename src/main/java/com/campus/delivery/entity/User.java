package com.campus.delivery.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.*;


import java.time.LocalDateTime;

/**
 * 用户表
 */
@Data
@Entity
@Table(name = "user")
public class User {
    @jakarta.persistence.Id
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

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Integer getCurrentRole() {
        return currentRole;
    }

    public void setCurrentRole(Integer currentRole) {
        this.currentRole = currentRole;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getFrozenPoints() {
        return frozenPoints;
    }

    public void setFrozenPoints(Integer frozenPoints) {
        this.frozenPoints = frozenPoints;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIdentityType() {
        return identityType;
    }

    public void setIdentityType(Integer identityType) {
        this.identityType = identityType;
    }

    public LocalDateTime getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(LocalDateTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getPointBalance() {
        return pointBalance;
    }

    public void setPointBalance(Integer pointBalance) {
        this.pointBalance = pointBalance;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}
