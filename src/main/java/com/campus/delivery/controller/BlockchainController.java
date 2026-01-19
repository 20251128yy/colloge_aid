package com.campus.delivery.controller;

import com.campus.delivery.common.Result;
import com.campus.delivery.exception.BusinessException;
import com.campus.delivery.exception.PermissionDeniedException;
import com.campus.delivery.service.BlockchainService;
import com.campus.delivery.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 区块链控制器
 */
@RestController
@RequestMapping("/api/blockchain")
public class BlockchainController {
    @Autowired
    private BlockchainService blockchainService;

    /**
     * 手动触发积分上链
     */
    @PostMapping("/point/upload")
    public Result<Map<String, Object>> uploadPointTransaction(@RequestParam Long transactionId, HttpServletRequest request) {
        try {
            // TODO: 验证用户是否有权限操作该交易
            // Long userId = JwtUtil.getUserIdFromToken(getTokenFromRequest(request));
            // pointTransactionService.validateUserPermission(userId, transactionId);

            // 调用区块链服务上链
            String transactionHash = blockchainService.transferPoints(transactionId, 1L, 2L, 20); // 示例参数

            Map<String, Object> result = new HashMap<>();
            result.put("hash", transactionHash);
            result.put("transactionId", transactionId);
            return Result.success(result);
        } catch (Exception e) {
            return Result.badRequest(e.getMessage());
        }
    }

    /**
     * 查询链上积分流水
     */
    @GetMapping("/point/query")
    public Result<Object> queryPointTransactions(@RequestParam Long userId, HttpServletRequest request) {
        try {
            // 权限检查：只能查询自己的积分流水，管理员可以查询所有
            Long currentUserId = JwtUtil.getUserIdFromToken(getTokenFromRequest(request));
            Integer identityType = JwtUtil.getIdentityTypeFromToken(getTokenFromRequest(request));

            // 如果不是管理员且查询的不是自己的记录，拒绝访问
            if (identityType != 2 && !currentUserId.equals(userId)) {
                throw new PermissionDeniedException("权限不足，只能查询自己的积分流水");
            }

            // 调用区块链服务查询
            String history = blockchainService.getHistoryByTaskId(userId); // TODO: 改为按用户ID查询

            return Result.success(history);
        } catch (PermissionDeniedException e) {
            return Result.forbidden(e.getMessage());
        } catch (Exception e) {
            return Result.badRequest(e.getMessage());
        }
    }

    /**
     * 从请求头中获取JWT令牌
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        throw new BusinessException("未提供有效令牌");
    }
}
