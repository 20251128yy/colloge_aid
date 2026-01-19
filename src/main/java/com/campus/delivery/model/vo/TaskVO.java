package com.campus.delivery.model.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 任务返回视图对象（VO）
 * 关键修改：添加public修饰符，确保跨包可访问
 * 字段与Task实体类保持一致，仅暴露前端需要的字段
 */
@Data // lombok自动生成get/set，无需手动编写
public class TaskVO { // 必须添加public修饰符

    // 任务基础信息
    private Long id;
    private String title;
    private String description;
    private String fromLocation;
    private String toLocation;
    private Integer itemType; // 1=快递，2=外卖，3=其他
    private LocalDateTime expectedCompletionTime;
    private Integer pointAmount; // 积分数量

    // 任务状态相关
    private Integer taskStatus; // 0=待接单，1=配送中，2=已完成，3=已取消
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