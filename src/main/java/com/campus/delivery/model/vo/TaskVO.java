package com.campus.delivery.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskVO {
    private Long id;
    private String title;
    private String description;
    private String fromLocation;
    private String toLocation;
    private Integer itemType;
    private LocalDateTime expectedCompletionTime;
    private Integer pointAmount;
    private Integer taskStatus;
    private Long requesterId;
    private Long delivererId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private LocalDateTime completeTime;
    private String cancelReason;
    private String remark;
}
