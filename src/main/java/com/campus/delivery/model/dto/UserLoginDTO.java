package com.campus.delivery.model.dto;

import lombok.Data;

@Data
public class UserLoginDTO {
    private String account; // 手机号或邮箱
    private String password;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
