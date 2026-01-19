package com.campus.delivery.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserVO {
    private Long id;
    private String phone;
    private String email;
    private String name;
    private String studentId;
    private Integer identityType;
    private Integer auditStatus;
    private Integer currentRole;
    private Integer pointBalance;
    private Integer frozenPoints;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime lastLoginTime;
}
