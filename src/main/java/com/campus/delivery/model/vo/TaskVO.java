package com.campus.delivery.model.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 任务返回视图对象（VO）
 * 字段与Task实体类完全对齐，状态值与后端保持一致
 */
@Data // 确保Lombok生效，自动生成getter/setter/toString等
public class TaskVO {
    // 任务基础信息
    private Long id;
    private String title;
    private String description;
    private String fromLocation; // 取件地点
    private String toLocation; // 送达地点
    private Integer itemType; // 1=书籍，2=实验器材，3=包裹，4=餐食（和后端保持一致）
    private LocalDateTime expectedCompletionTime; // 预计完成时间
    private Integer pointAmount; // 积分数量

    // 任务状态相关（关键：状态值和后端完全对齐）
    private Integer taskStatus; // 0=待接单，1=配送中，2=已完成，3=积分已发放，4=已取消（和TaskServiceImpl一致）
    private Integer auditStatus; // 新增：审核状态（0=待审核，1=审核通过，2=审核拒绝）
    private Long requesterId; // 发布人ID
    private Long delivererId; // 接单者ID

    // 时间相关
    private LocalDateTime createTime; // 创建时间
    private LocalDateTime updateTime; // 更新时间
    private LocalDateTime completeTime; // 完成时间

    // 其他信息
    private String cancelReason; // 取消原因
    private String remark; // 备注
}