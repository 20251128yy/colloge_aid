package com.campus.delivery.controller;

import com.campus.delivery.common.Result;
import com.campus.delivery.entity.User;
import com.campus.delivery.exception.BusinessException;
import com.campus.delivery.exception.PermissionDeniedException;
import com.campus.delivery.model.dto.UserRegisterDTO;
import com.campus.delivery.service.UserService;
import com.campus.delivery.util.JwtUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 管理员控制器
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    /**
     * 用户审核
     */
    @PostMapping("/user/audit")
    public Result<Boolean> auditUser(@RequestParam Long userId, @RequestParam Integer auditStatus, @RequestParam String reason, HttpServletRequest request) {
        try {
            // 权限检查：只有管理员才能访问此接口
            checkAdminPermission(request);

            userService.updateAuditStatus(userId, auditStatus);
            return Result.success(true);
        } catch (PermissionDeniedException e) {
            return Result.forbidden(e.getMessage());
        } catch (Exception e) {
            return Result.badRequest(e.getMessage());
        }
    }

    /**
     * 任务审核/下架
     */
    @PostMapping("/task/audit")
    public Result<Boolean> auditTask(@RequestParam Long taskId, @RequestParam Integer auditStatus, @RequestParam String reason, HttpServletRequest request) {
        try {
            // 权限检查：只有管理员才能访问此接口
            checkAdminPermission(request);

            // TODO: 实现任务审核功能
            // taskService.updateTaskStatus(taskId, auditStatus, reason);
            return Result.success(true);
        } catch (PermissionDeniedException e) {
            return Result.forbidden(e.getMessage());
        } catch (Exception e) {
            return Result.badRequest(e.getMessage());
        }
    }

    /**
     * 获取平台统计数据
     */
    @GetMapping("/statistics")
    public Result<Map<String, Object>> getStatistics(HttpServletRequest request) {
        try {
            // 权限检查：只有管理员才能访问此接口
            checkAdminPermission(request);

            // TODO: 实现数据统计功能
            Map<String, Object> statistics = new HashMap<>();
            // 统计平台总用户数
            // statistics.put("totalUser", userService.countTotalUsers());
            // 统计任务总数
            // statistics.put("totalTask", taskService.countTotalTasks());
            // 统计各状态任务数
            // statistics.put("pendingTasks", taskService.countTasksByStatus(0));
            // statistics.put("deliveringTasks", taskService.countTasksByStatus(1));
            // statistics.put("completedTasks", taskService.countTasksByStatus(2));
            // statistics.put("cancelledTasks", taskService.countTasksByStatus(3));
            // 统计积分排行
            // statistics.put("pointRanking", userService.getPointRanking());
            
            // 模拟数据
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
    private void checkAdminPermission(HttpServletRequest request) {
        try {
            // 从JWT令牌中获取用户ID和身份类型
            String token = request.getHeader("Authorization").substring(7);
            Integer identityType = JwtUtil.getIdentityTypeFromToken(token);

            // 检查是否为管理员（identityType=2）
            if (identityType != 2) {
                throw new PermissionDeniedException("只有管理员才能访问此接口");
            }
        } catch (Exception e) {
            throw new PermissionDeniedException("权限不足");
        }
    }
}
