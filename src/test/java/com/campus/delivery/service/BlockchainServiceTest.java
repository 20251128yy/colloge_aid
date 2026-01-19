package com.campus.delivery.service;

import com.campus.delivery.service.impl.BlockchainServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
        // 由于是模拟实现，这里只验证返回了有效的交易哈希格式
    }

    @Test
    void testGetHistoryByTaskId() {
        // 准备测试数据
        Long taskId = 1L;

        // 执行测试
        String history = blockchainService.getHistoryByTaskId(taskId);

        // 验证结果
        assertNotNull(history);
        // 由于是模拟实现，这里只验证返回了有效的历史记录格式
    }

    @Test
    void testGetAllHistory() {
        // 执行测试
        String history = blockchainService.getAllHistory();

        // 验证结果
        assertNotNull(history);
        // 由于是模拟实现，这里只验证返回了有效的历史记录格式
    }
}
