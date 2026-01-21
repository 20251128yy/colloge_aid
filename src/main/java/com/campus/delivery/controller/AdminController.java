package com.campus.delivery.controller;

import com.campus.delivery.common.Result;
import com.campus.delivery.entity.User;
import com.campus.delivery.exception.BusinessException;
import com.campus.delivery.exception.PermissionDeniedException;
import com.campus.delivery.model.dto.UserLoginDTO;
import com.campus.delivery.service.UserService;
import com.campus.delivery.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 管理员控制器
 */
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    /**
     * 管理员登录（前端要调用的/admin/login）
     * 实际复用/user/login逻辑，只是返回管理员专属提示
     */
//    @PostMapping("/login")
//    public Result<String> adminLogin(@Validated @RequestBody UserLoginDTO userLoginDTO) {
//        try {
//            String token = userService.login(userLoginDTO);
//            // 验证是否为管理员
//            Integer identityType = JwtUtil.getIdentityTypeFromToken(token);
//            if (identityType != 2) {
//                throw new PermissionDeniedException("非管理员账号，无法登录管理后台");
//            }
//            return Result.success(token);
//        } catch (BusinessException e) {
//            return Result.error(400, e.getMessage());
//        } catch (PermissionDeniedException e) {
//            return Result.forbidden(e.getMessage());
//        } catch (Exception e) {
//            e.printStackTrace();
//            return Result.error(500, "服务器内部错误");
//        }
//    }
    @PostMapping("/login")
    public Result<Map<String, Object>> adminLogin(@RequestBody Map<String, Object> params) {
        System.out.println("=== 管理员登录开始（调试版）===");
        System.out.println("前端参数: " + params);

        // 打印所有可能的键
        System.out.println("参数键列表: " + params.keySet());

        String account = null;
        String password = null;

        // 检查每个可能的键
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            System.out.println("键: " + entry.getKey() + ", 值: " + entry.getValue());
        }

        // 尝试获取account
        account = (String) params.get("account");
        if (account == null) {
            account = (String) params.get("loginName");
        }
        if (account == null) {
            account = (String) params.get("username");
        }

        password = (String) params.get("password");

        System.out.println("提取的账号: " + account);
        System.out.println("提取的密码: " + password);

        if (account == null || password == null) {
            System.out.println("账号或密码为空");
            return Result.error(400, "账号和密码不能为空");
        }

        try {
            System.out.println("创建UserLoginDTO...");
            UserLoginDTO dto = new UserLoginDTO();
            dto.setAccount(account);
            dto.setPassword(password);

            System.out.println("DTO创建完成: account=" + dto.getAccount() + ", password=" + dto.getPassword());

            System.out.println("调用userService.login()...");
            String token = userService.login(dto);
            System.out.println("userService返回Token: " + token);

            System.out.println("验证Token身份类型...");
            Integer identityType = JwtUtil.getIdentityTypeFromToken(token);
            System.out.println("身份类型: " + identityType);

            if (identityType != 2) {
                System.out.println("非管理员身份: " + identityType);
                return Result.forbidden("非管理员账号");
            }

            System.out.println("从Token解析用户ID...");
            Long userId = JwtUtil.getUserIdFromToken(token);
            System.out.println("用户ID: " + userId);

            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            data.put("user", Map.of(
                    "id", userId,
                    "account", account,
                    "identityType", identityType
            ));

            System.out.println("登录成功，返回数据");
            return Result.success(data);

        } catch (Exception e) {
            System.err.println("========== 管理员登录异常详情 ==========");
            System.err.println("异常类型: " + e.getClass().getName());
            System.err.println("异常消息: " + e.getMessage());
            System.err.println("完整堆栈:");
            e.printStackTrace();
            System.err.println("========== 异常结束 ==========");

            return Result.error(500, "登录失败: " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }
    /**
     * 用户审核（适配前端PUT请求 + 路径）
     */
    @PutMapping("/users/{id}/audit")
    public Result<Boolean> updateUserAuditStatus(
            @PathVariable Long id,
            @RequestParam Integer auditStatus,
            HttpServletRequest request) {
        try {
            AdminController.checkAdminPermission(request);
            // 复用原有的更新审核状态逻辑
            userService.updateAuditStatus(id, auditStatus);
            return Result.success(true);
        } catch (PermissionDeniedException e) {
            return Result.forbidden(e.getMessage());
        } catch (Exception e) {
            return Result.error(400, e.getMessage());
        }
    }

    /**
     * 更新用户角色（前端需要的接口）
     */
    @PutMapping("/users/{id}/role")
    public Result<Boolean> updateUserRole(
            @PathVariable Long id,
            @RequestParam Integer role,
            HttpServletRequest request) {
        try {
            AdminController.checkAdminPermission(request);
            userService.switchRole(id, role);
            return Result.success(true);
        } catch (PermissionDeniedException e) {
            return Result.forbidden(e.getMessage());
        } catch (Exception e) {
            return Result.error(400, e.getMessage());
        }
    }

    /**
     * 更新用户状态（前端需要的接口）
     */
    @PutMapping("/users/{id}/status")
    public Result<Boolean> updateUserStatus(
            @PathVariable Long id,
            @RequestParam Integer status,
            HttpServletRequest request) {
        try {
            AdminController.checkAdminPermission(request);
            userService.updateUserStatus(id, status);
            return Result.success(true);
        } catch (PermissionDeniedException e) {
            return Result.forbidden(e.getMessage());
        } catch (Exception e) {
            return Result.error(400, e.getMessage());
        }
    }

    /**
     * 获取用户积分记录（前端需要的接口）
     * 需在UserService中实现getUserPointsHistory方法
     */
    @GetMapping("/users/{id}/points")
    public Result<Map<String, Object>> getUserPointsHistory(
            @PathVariable Long id,
            @RequestParam Map<String, Object> params,
            HttpServletRequest request) {
        try {
            AdminController.checkAdminPermission(request);
            // TODO: 在UserService中实现getUserPointsHistory方法
            Map<String, Object> result = new HashMap<>();
            result.put("total", 0);
            result.put("items", null);
            return Result.success(result);
        } catch (PermissionDeniedException e) {
            return Result.forbidden(e.getMessage());
        } catch (Exception e) {
            return Result.error(400, e.getMessage());
        }
    }

    // ========== 原有接口保留（兼容旧调用） ==========
    @PostMapping("/user/audit")
    public Result<Boolean> auditUser(@RequestParam Long userId, @RequestParam Integer auditStatus, @RequestParam String reason, HttpServletRequest request) {
        try {
            AdminController.checkAdminPermission(request);
            userService.updateAuditStatus(userId, auditStatus);
            return Result.success(true);
        } catch (PermissionDeniedException e) {
            return Result.forbidden(e.getMessage());
        } catch (Exception e) {
            return Result.badRequest(e.getMessage());
        }
    }

    @PostMapping("/task/audit")
    public Result<Boolean> auditTask(@RequestParam Long taskId, @RequestParam Integer auditStatus, @RequestParam String reason, HttpServletRequest request) {
        try {
            AdminController.checkAdminPermission(request);
            return Result.success(true);
        } catch (PermissionDeniedException e) {
            return Result.forbidden(e.getMessage());
        } catch (Exception e) {
            return Result.badRequest(e.getMessage());
        }
    }

    @GetMapping("/users")
    public Result<Map<String, Object>> getUserList(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer auditStatus,
            @RequestParam(required = false) Integer role,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request) {
        try {
            AdminController.checkAdminPermission(request);
            Map<String, Object> result = new HashMap<>();
            result.put("total", 100);
            result.put("items", null);
            return Result.success(result);
        } catch (PermissionDeniedException e) {
            return Result.forbidden(e.getMessage());
        } catch (Exception e) {
            return Result.badRequest(e.getMessage());
        }
    }

    @GetMapping("/users/{userId}")
    public Result<User> getUserDetail(@PathVariable Long userId, HttpServletRequest request) {
        try {
            AdminController.checkAdminPermission(request);
            User user = userService.getUserById(userId).orElse(null);
            return Result.success(user);
        } catch (PermissionDeniedException e) {
            return Result.forbidden(e.getMessage());
        } catch (Exception e) {
            return Result.badRequest(e.getMessage());
        }
    }

    @GetMapping("/statistics")
    public Result<Map<String, Object>> getStatistics(HttpServletRequest request) {
        try {
            AdminController.checkAdminPermission(request);
            Map<String, Object> statistics = new HashMap<>();
            statistics.put("totalUser", 100);
            statistics.put("totalTask", 50);
            statistics.put("pendingTasks", 10);
            statistics.put("deliveringTasks", 20);
            statistics.put("completedTasks", 15);
            statistics.put("cancelledTasks", 5);
            return Result.success(statistics);
        } catch (PermissionDeniedException e) {
            return Result.forbidden(e.getMessage());
        } catch (Exception e) {
            return Result.badRequest(e.getMessage());
        }
    }

    /**
     * 权限检查：只有管理员才能访问
     */
    public static void checkAdminPermission(HttpServletRequest request) {
        try {
            // 兼容Token为空的情况（避免substring报错）
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new PermissionDeniedException("未登录或Token格式错误");
            }
            String token = authHeader.substring(7).trim();
            if (token == null || token.isEmpty()) {
                throw new PermissionDeniedException("Token为空");
            }
            if (token.split("\\.").length != 3) {
                throw new PermissionDeniedException("Token格式错误");
            }
            Integer identityType = JwtUtil.getIdentityTypeFromToken(token);
            if (identityType != 2) {
                throw new PermissionDeniedException("只有管理员才能访问此接口");
            }
        } catch (Exception e) {
            throw new PermissionDeniedException("权限不足：" + e.getMessage());
        }
    }
}