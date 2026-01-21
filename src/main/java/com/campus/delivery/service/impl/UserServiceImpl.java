package com.campus.delivery.service.impl;

import com.campus.delivery.entity.User;
import com.campus.delivery.exception.BusinessException;
import com.campus.delivery.model.dto.UserLoginDTO;
import com.campus.delivery.model.dto.UserRegisterDTO;
import com.campus.delivery.repository.UserRepository;
import com.campus.delivery.service.UserService;
import com.campus.delivery.util.JwtUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Predicate;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    // 实现带条件的分页查询
    @Override
    public Map<String, Object> getUserListByCondition(String keyword, Integer auditStatus, Integer role, Integer pageNum, Integer pageSize) {
        // 1. 构建查询条件（显式指定JPA的Predicate类型，避免和java.util.function.Predicate混淆）
        Specification<User> spec = new Specification<User>() {
            @Override
            public jakarta.persistence.criteria.Predicate toPredicate(
                    Root<User> root,
                    CriteriaQuery<?> query,
                    CriteriaBuilder cb) {

                // 显式声明JPA的Predicate列表（核心修复）
                List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();

                // 关键词模糊查询（姓名/手机号/学号）
                if (keyword != null && !keyword.trim().isEmpty()) {
                    jakarta.persistence.criteria.Predicate nameLike = cb.like(root.get("name"), "%" + keyword.trim() + "%");
                    jakarta.persistence.criteria.Predicate phoneLike = cb.like(root.get("phone"), "%" + keyword.trim() + "%");
                    jakarta.persistence.criteria.Predicate studentIdLike = cb.like(root.get("studentId"), "%" + keyword.trim() + "%");
                    predicates.add(cb.or(nameLike, phoneLike, studentIdLike)); // 无需强制转换
                }

                // 审核状态筛选
                if (auditStatus != null) {
                    predicates.add(cb.equal(root.get("auditStatus"), auditStatus));
                }

                // 角色筛选
                if (role != null) {
                    predicates.add(cb.equal(root.get("currentRole"), role));
                }

                // 组合所有条件（转换为JPA的Predicate数组，无类型冲突）
                return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
            }
        };

        // 2. 分页查询（JPA页码从0开始，前端传的是1开始）
        int jpaPageNum = pageNum - 1;
        Pageable pageable = PageRequest.of(jpaPageNum, pageSize);
        Page<User> userPage = userRepository.findAll(spec, pageable);

        // 3. 封装结果
        Map<String, Object> result = new HashMap<>();
        result.put("total", userPage.getTotalElements()); // 总条数
        result.put("list", userPage.getContent());        // 用户列表数据

        return result;
    }
    @Override
    @Transactional
    public User register(UserRegisterDTO userRegisterDTO) {
        // 1. 手机号非空时校验（解决空字符串也校验的问题）
        if (userRegisterDTO.getPhone() != null && !userRegisterDTO.getPhone().isEmpty()) {
            if (userRepository.existsByPhone(userRegisterDTO.getPhone())) {
                throw new BusinessException("手机号已被注册");
            }
        }

        // 2. 邮箱非空时校验
        if (userRegisterDTO.getEmail() != null && !userRegisterDTO.getEmail().isEmpty()) {
            if (userRepository.existsByEmail(userRegisterDTO.getEmail())) {
                throw new BusinessException("邮箱已被注册");
            }
        }

        // 3. 新增学号校验（前端传了学号，后端必须校验）
        if (userRegisterDTO.getStudentId() != null && !userRegisterDTO.getStudentId().isEmpty()) {
            if (userRepository.existsByStudentId(userRegisterDTO.getStudentId())) {
                throw new BusinessException("学号已被注册");
            }
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
//    public String login(UserLoginDTO userLoginDTO) {
//        try {
//            Optional<User> userOptional = userRepository.findByPhoneOrEmail(userLoginDTO.getAccount(), userLoginDTO.getAccount());
//            if (!userOptional.isPresent()) {
//                throw new BusinessException("账号不存在");
//            }
//            User user = userOptional.get();
//
//            // 1. 打印用户信息（确认查询到的用户字段是否正常）
//            System.out.println("查询到的用户信息：" + user);
//            System.out.println("identity_type: " + user.getIdentityType());
//            System.out.println("audit_status: " + user.getAuditStatus());
//
//            // 2. 校验密码
//            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//            if (!encoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
//                throw new BusinessException("密码错误");
//            }
//
//            // 3. 生成JWT（重点排查）
//            String token = JwtUtil.generateToken(user.getId(), user.getIdentityType());
//            System.out.println("生成的JWT Token：" + token);
//
//            // 4. 更新最后登录时间
//            userRepository.save(user);
//
//            return token;
//        } catch (Exception e) {
//            // 强制打印完整异常堆栈
//            e.printStackTrace();
//            throw e; // 继续向上抛，让全局异常处理器返回给前端
//        }
//    }
    public String login(UserLoginDTO userLoginDTO) {
        String account = userLoginDTO.getAccount();
        System.out.println("登录账号: " + account);

        // 1. 先按用户名查询
        Optional<User> userOptional = userRepository.findByName(account);

        // 2. 再按手机/邮箱查询
        if (!userOptional.isPresent()) {
            userOptional = userRepository.findByPhoneOrEmail(account, account);
        }

        if (!userOptional.isPresent()) {
            throw new BusinessException("账号不存在");
        }

        User user = userOptional.get();
        System.out.println("找到用户: " + user.getName());

        // 密码验证
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            if (!encoder.matches(userLoginDTO.getPassword(), user.getPassword())) {
                throw new BusinessException("密码错误");
            }
        return JwtUtil.generateToken(user.getId(), user.getIdentityType());
    }
    // 其他方法不变...
    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public User updateAuditStatus(Long userId, Integer auditStatus) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException("用户不存在"));
        user.setAuditStatus(auditStatus);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User switchRole(Long userId, Integer role) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException("用户不存在"));
        user.setCurrentRole(role);
        return userRepository.save(user);
    }

    @Override
    public Integer getCurrentRole(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException("用户不存在"));
        return user.getCurrentRole();
    }

    @Override
    @Transactional
    public User updatePoints(Long userId, Integer amount, Integer type) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException("用户不存在"));
        user.setPointBalance(user.getPointBalance() + amount);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User freezePoints(Long userId, Integer amount) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException("用户不存在"));
        if (user.getPointBalance() < amount) {
            throw new BusinessException("积分不足");
        }
        user.setPointBalance(user.getPointBalance() - amount);
        user.setFrozenPoints(user.getFrozenPoints() + amount);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User unfreezePoints(Long userId, Integer amount) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException("用户不存在"));
        if (user.getFrozenPoints() < amount) {
            throw new BusinessException("冻结积分不足");
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

    @Override
    @Transactional
    public User updateUserStatus(Long userId, Integer status) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException("用户不存在"));
        user.setStatus(status);
        return userRepository.save(user);
    }
}