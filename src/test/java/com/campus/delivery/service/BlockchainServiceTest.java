package com.campus.delivery.service;

import com.campus.delivery.service.impl.BlockchainServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BlockchainServiceTest {

    @InjectMocks
    private BlockchainServiceImpl blockchainService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testTransferPoints() {
        // 准备测试数据
        Long taskId = 1L;
        Long requesterId = 1L;
        Long delivererId = 2L;
        Integer amount = 20;

        // 执行测试
        String transactionHash = blockchainService.transferPoints(taskId, requesterId, delivererId, amount);

        // 验证结果
        assertNotNull(transactionHash);
        assertTrue(transactionHash.startsWith("0x"));
        assertTrue(transactionHash.length() > 2);
    }

    @Test
    void testTransferPoints_SpecialCases() {
        // 测试边界情况
        // 1. 零金额
        String result1 = blockchainService.transferPoints(1L, 1L, 2L, 0);
        assertNotNull(result1);
        assertTrue(result1.startsWith("0x"));

        // 2. 负数金额（如果需要处理的话）
        String result2 = blockchainService.transferPoints(2L, 1L, 2L, -10);
        assertNotNull(result2);
        assertTrue(result2.startsWith("0x"));

        // 3. 大金额
        String result3 = blockchainService.transferPoints(3L, 1L, 2L, 1000000);
        assertNotNull(result3);
        assertTrue(result3.startsWith("0x"));
    }

    @Test
    void testGetHistoryByTaskId() {
        // 准备测试数据
        Long taskId = 1L;

        // 执行测试
        String history = blockchainService.getHistoryByTaskId(taskId);

        // 验证结果
        assertNotNull(history);
        assertTrue(history.contains("taskId"));
        assertTrue(history.contains("blockchainMode"));
    }

    @Test
    void testGetHistoryByTaskId_NotFound() {
        // 测试不存在的任务ID
        String history = blockchainService.getHistoryByTaskId(999L);

        assertNotNull(history);
        assertTrue(history.contains("999"));  // 应该包含任务ID
    }

    @Test
    void testGetAllHistory() {
        // 执行测试
        String history = blockchainService.getAllHistory();

        // 验证结果
        assertNotNull(history);
        assertTrue(history.contains("total"));
        assertTrue(history.contains("histories"));
    }
}