package com.campus.delivery.service;

public interface BlockchainService {
    /**
     * 转账积分（调用智能合约）
     */
    String transferPoints(Long taskId, Long requesterId, Long delivererId, Integer amount);

    /**
     * 查询指定任务的积分流转记录
     */
    String getHistoryByTaskId(Long taskId);

    /**
     * 获取所有积分流转记录
     */
    String getAllHistory();
}
