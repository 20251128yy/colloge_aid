package com.campus.delivery.controller;

import com.campus.delivery.common.Result;
import com.campus.delivery.entity.User;
import com.campus.delivery.exception.BusinessException;
import com.campus.delivery.exception.UserNotAuditedException;
import com.campus.delivery.model.dto.UserLoginDTO;
import com.campus.delivery.model.dto.UserRegisterDTO;
import com.campus.delivery.model.vo.UserVO;
import com.campus.delivery.service.UserService;
import com.campus.delivery.util.JwtUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result<UserVO> register(@Validated @RequestBody UserRegisterDTO userRegisterDTO) {
        try {
            User user = userService.register(userRegisterDTO);
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            return Result.success(userVO); // 成功返回 code=200, msg=成功
        } catch (BusinessException e) {
            // 业务异常返回 code=400 + 具体提示
            return Result.error(400, e.getMessage());
        } catch (Exception e) {
            // 其他异常返回通用提示
            return Result.error(500, "服务器内部错误");
        }
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<String> login(@Validated @RequestBody UserLoginDTO userLoginDTO) {
        try {
            String token = userService.login(userLoginDTO);
            return Result.success(token);
        } catch (BusinessException e) {
            return Result.error(400, e.getMessage());
        } catch (Exception e) {
            return Result.error(500, "服务器内部错误");
        }
    }

    /**
     * 角色切换
     */
    @PostMapping("/switchRole")
    public Result<UserVO> switchRole(@RequestParam Integer role, HttpServletRequest request) {
        try {
            // 从JWT令牌中获取用户ID
            String token = request.getHeader("Authorization").substring(7);
            Long userId = JwtUtil.getUserIdFromToken(token);

            User user = userService.switchRole(userId, role);
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            return Result.success(userVO);
        } catch (Exception e) {
            return Result.badRequest(e.getMessage());
        }
    }

    /**
     * 查询个人信息
     */
    @GetMapping("/info")
    public Result<UserVO> getUserInfo(HttpServletRequest request) {
        try {
            // 从JWT令牌中获取用户ID
            String token = request.getHeader("Authorization").substring(7);
            Long userId = JwtUtil.getUserIdFromToken(token);

            Optional<User> userOptional = userService.getUserById(userId);
            if (userOptional.isPresent()) {
                UserVO userVO = new UserVO();
                BeanUtils.copyProperties(userOptional.get(), userVO);
                return Result.success(userVO);
            } else {
                return Result.error(404, "用户不存在");
            }
        } catch (Exception e) {
            return Result.badRequest(e.getMessage());
        }
    }

    /**
     * 获取用户积分
     */
    @GetMapping("/points")
    public Result<Integer> getPointBalance(HttpServletRequest request) {
        try {
            // 从JWT令牌中获取用户ID
            String token = request.getHeader("Authorization").substring(7);
            Long userId = JwtUtil.getUserIdFromToken(token);

            Optional<User> userOptional = userService.getUserById(userId);
            if (userOptional.isPresent()) {
                return Result.success(userOptional.get().getPointBalance());
            } else {
                return Result.error(404, "用户不存在");
            }
        } catch (Exception e) {
            return Result.badRequest(e.getMessage());
        }
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/profile")
    public Result<UserVO> updateUserInfo(@RequestBody User user, HttpServletRequest request) {
        try {
            // 从JWT令牌中获取用户ID
            String token = request.getHeader("Authorization").substring(7);
            Long userId = JwtUtil.getUserIdFromToken(token);

            user.setId(userId);
            User updatedUser = userService.updateUser(user);
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(updatedUser, userVO);
            return Result.success(userVO);
        } catch (Exception e) {
            return Result.badRequest(e.getMessage());
        }
    }

    /**
     * 修改密码
     */
    @PutMapping("/password")
    public Result<Boolean> changePassword(
            @RequestParam String oldPassword,
            @RequestParam String newPassword,
            HttpServletRequest request) {
        try {
            // 从JWT令牌中获取用户ID
            String token = request.getHeader("Authorization").substring(7);
            Long userId = JwtUtil.getUserIdFromToken(token);

            boolean result = userService.changePassword(userId, oldPassword, newPassword);
            return Result.success(result);
        } catch (Exception e) {
            return Result.badRequest(e.getMessage());
        }
    }
}
