package com.campus.delivery.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

/**
 * 任务表
 * 关键修改1：添加public修饰符，确保跨包可访问
 * 关键修改2：调整字段定义顺序，修复get/set方法混乱问题
 * 关键修改3：删除重复的get/set方法（lombok的@Getter/@Setter已自动生成）
 */
@Getter // lombok自动生成getter
@Setter // lombok自动生成setter
@Entity
@Table(name = "task")
public class Task { // 添加public修饰符

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "from_location", nullable = false)
    private String fromLocation;

    @Column(name = "to_location", nullable = false)
    private String toLocation;

    @Column(name = "item_type")
    private Integer itemType = 1; // 默认快递

    @Column(name = "expected_completion_time")
    private LocalDateTime expectedCompletionTime;

    @Column(name = "point_amount")
    private Integer pointAmount = 0;

    @Column(name = "task_status")
    private Integer taskStatus = 0; // 默认待接单

    @Column(name = "requester_id", nullable = false)
    private Long requesterId;

    @Column(name = "deliverer_id")
    private Long delivererId;

    @Column(name = "audit_status")
    private Integer auditStatus = 0; // 默认待审核

    @CreationTimestamp
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    @UpdateTimestamp
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(name = "complete_time")
    private LocalDateTime completeTime;

    @Column(name = "cancel_reason", length = 500)
    private String cancelReason;

    @Column(name = "remark", length = 1000)
    private String remark; // 调整到字段定义区域，避免get/set方法混乱

    // 注意：lombok的@Getter/@Setter已自动生成所有字段的get/set方法，无需手动编写
    // 手动编写的get/set方法会与lombok冲突，全部删除
}