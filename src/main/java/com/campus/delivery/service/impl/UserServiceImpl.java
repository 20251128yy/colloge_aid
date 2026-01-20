package com.campus.delivery.service.impl;

import com.campus.delivery.entity.User;
import com.campus.delivery.model.dto.UserLoginDTO;
import com.campus.delivery.model.dto.UserRegisterDTO;
import com.campus.delivery.repository.UserRepository;
import com.campus.delivery.service.UserService;
import com.campus.delivery.util.JwtUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    @Transactional
    public User register(UserRegisterDTO userRegisterDTO) {
        // 检查手机号是否已存在
        if (userRepository.existsByPhone(userRegisterDTO.getPhone())) {
            throw new RuntimeException("手机号已被注册");
        }

        // 检查邮箱是否已存在
        if (userRepository.existsByEmail(userRegisterDTO.getEmail())) {
            throw new RuntimeException("邮箱已被注册");
        }

        User user = new User();
        BeanUtils.copyProperties(userRegisterDTO, user);

        // 密码加密
        user.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));

        // 设置初始积分（新用户赠送）
        user.setPointBalance(100);
        user.setFrozenPoints(0);

        // 设置初始状态
        user.setIdentityType(1); // 默认学生身份
        user.setAuditStatus(0); // 待审核
        user.setCurrentRole(1); // 默认需求方
        user.setStatus(1); // 启用状态

        return userRepository.save(user);
    }

    @Override
    public String login(UserLoginDTO userLoginDTO) {
        Optional<User> userOptional = userRepository.findByPhoneOrEmail(userLoginDTO.getAccount(), userLoginDTO.getAccount());
        if (!userOptional.isPresent()) {
            throw new RuntimeException("用户名或密码错误");
        }

        User user = userOptional.get();

        // 检查密码
        if (!passwordEncoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 检查审核状态
        if (user.getAuditStatus() != 1) {
            throw new RuntimeException("账号未审核通过");
        }

        // 生成JWT Token
        return JwtUtil.generateToken(user.getId(), user.getIdentityType());
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public User updateAuditStatus(Long userId, Integer auditStatus) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("用户不存在"));
        user.setAuditStatus(auditStatus);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User switchRole(Long userId, Integer role) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("用户不存在"));
        user.setCurrentRole(role);
        return userRepository.save(user);
    }

    @Override
    public Integer getCurrentRole(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("用户不存在"));
        return user.getCurrentRole();
    }

    @Override
    @Transactional
    public User updatePoints(Long userId, Integer amount, Integer type) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("用户不存在"));
        user.setPointBalance(user.getPointBalance() + amount);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User freezePoints(Long userId, Integer amount) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("用户不存在"));
        if (user.getPointBalance() < amount) {
            throw new RuntimeException("积分不足");
        }
        user.setPointBalance(user.getPointBalance() - amount);
        user.setFrozenPoints(user.getFrozenPoints() + amount);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User unfreezePoints(Long userId, Integer amount) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("用户不存在"));
        if (user.getFrozenPoints() < amount) {
            throw new RuntimeException("冻结积分不足");
        }
        user.setFrozenPoints(user.getFrozenPoints() - amount);
        user.setPointBalance(user.getPointBalance() + amount);
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        return false;
    }
}
