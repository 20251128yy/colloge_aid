package com.campus.delivery.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * 积分流水表
 */
@Data
@Entity
@Table(name = "point_transaction")
public class PointTransaction {
    // 关键修正：明确使用 jakarta.persistence.Id（删除springframework的Id导入）
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户ID
     */
    @Column(name = "user_id", nullable = false) // 非空约束，根据业务调整
    private Long userId;

    /**
     * 任务ID
     */
    @Column(name = "task_id")
    private Long taskId;

    /**
     * 交易类型：1-任务发布冻结, 2-任务完成发放, 3-任务取消解冻, 4-系统赠送, 5-其他
     */
    @Column(name = "transaction_type", nullable = false)
    private Integer transactionType;

    /**
     * 积分变动数量（可正可负，比如冻结为负、发放为正）
     */
    @Column(name = "amount", nullable = false)
    private Integer amount;

    /**
     * 变动前余额
     */
    @Column(name = "balance_before", nullable = false)
    private Integer balanceBefore;

    /**
     * 变动后余额
     */
    @Column(name = "balance_after", nullable = false)
    private Integer balanceAfter;

    /**
     * 交易状态：1-成功, 0-失败
     */
    @Column(name = "transaction_status", nullable = false)
    private Integer transactionStatus;

    /**
     * 区块链交易哈希（可为空）
     */
    @Column(name = "blockchain_hash", length = 100) // 指定长度，适配数据库
    private String blockchainHash;

    /**
     * 上链状态：0-未上链, 1-已上链, 2-上链失败
     */
    @Column(name = "chain_status", nullable = false)
    private Integer chainStatus = 0;

    /**
     * 上链失败错误信息
     */
    @Column(name = "chain_error_msg", length = 255)
    private String chainErrorMessage;

    /**
     * 上链重试次数（最多3次）
     */
    @Column(name = "upload_retry_count", nullable = false)
    private Integer uploadRetryCount = 0;

    /**
     * 最后上链时间
     */
    @Column(name = "last_upload_time")
    private LocalDateTime lastUploadTime;

    /**
     * 哈希值（用于验证数据一致性）
     */
    @Column(name = "hash_value", length = 64) // SHA-256 长度64
    private String hashValue;

    /**
     * 创建时间（自动填充，无需手动设置）
     */
    @CreationTimestamp
    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;

    /**
     * 备注信息（可为空）
     */
    @Column(name = "remark", length = 255)
    private String remark;
}