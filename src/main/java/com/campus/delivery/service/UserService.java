package com.campus.delivery.service;

import com.campus.delivery.entity.User;
import com.campus.delivery.model.dto.UserLoginDTO;
import com.campus.delivery.model.dto.UserRegisterDTO;
import com.campus.delivery.model.vo.UserVO;

import java.util.Optional;

public interface UserService {
    /**
     * 用户注册
     */
    User register(UserRegisterDTO userRegisterDTO);

    /**
     * 用户登录
     */
    String login(UserLoginDTO userLoginDTO);

    /**
     * 根据ID获取用户信息
     */
    Optional<User> getUserById(Long id);

    /**
     * 更新用户审核状态
     */
    User updateAuditStatus(Long userId, Integer auditStatus);

    /**
     * 切换用户角色
     */
    User switchRole(Long userId, Integer role);

    /**
     * 获取用户当前角色
     */
    Integer getCurrentRole(Long userId);

    /**
     * 更新用户积分
     */
    User updatePoints(Long userId, Integer amount, Integer type);

    /**
     * 冻结用户积分
     */
    User freezePoints(Long userId, Integer amount);

    /**
     * 解冻用户积分
     */
    User unfreezePoints(Long userId, Integer amount);
}
