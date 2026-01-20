package com.campus.delivery.service;

/**
 * 区块链服务接口（积分流转相关）
 * 修复点：显式声明public，避免包访问权限的隐性问题
 */
public interface BlockchainService { // 显式添加public（接口默认public，但显式声明更稳妥）
    /**
     * 转账积分（调用智能合约）
     * @param taskId 任务ID
     * @param requesterId 需求方ID
     * @param delivererId 派送方ID
     * @param amount 积分数量
     * @return 区块链交易哈希/执行结果
     */
    String transferPoints(Long taskId, Long requesterId, Long delivererId, Integer amount);

    /**
     * 查询指定任务的积分流转记录
     * @param taskId 任务ID
     * @return 积分流转记录（JSON格式/字符串）
     */
    String getHistoryByTaskId(Long taskId);

    /**
     * 获取所有积分流转记录
     * @return 所有积分流转记录（JSON格式/字符串）
     */
    String getAllHistory();

    /**
     * 实现积分上链功能，支持重试机制
     * @param transactionId 积分交易ID
     * @return 上链是否成功
     */
    boolean uploadPointTransaction(Long transactionId);
}