package com.campus.delivery.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 任务创建入参DTO
 * 关键修改：
 * 1. 添加public修饰符，确保跨包可访问
 * 2. 字段定义在前，校验注解紧贴字段
 * 3. 删除手动get/set（@Data自动生成）
 * 4. 规范注解位置和提示信息
 */
@Data // lombok自动生成get/set/equals/hashCode/toString，无需手动编写
public class TaskCreateDTO { // 必须加public

    /** 任务标题 */
    @NotBlank(message = "任务标题不能为空")
    private String title;

    /** 任务描述 */
    private String description;

    /** 取件地点 */
    @NotBlank(message = "取件地点不能为空")
    private String fromLocation;

    /** 送达地点 */
    @NotBlank(message = "送达地点不能为空")
    private String toLocation;

    /** 物品类型（1=快递，2=外卖，3=其他） */
    @NotNull(message = "物品类型不能为空")
    private Integer itemType;

    /** 期望完成时间 */
    @NotNull(message = "期望完成时间不能为空")
    private LocalDateTime expectedCompletionTime;

    /** 积分数量 */
    @NotNull(message = "积分数量不能为空")
    private Integer pointAmount;

    // 注意：删除所有手动编写的get/set方法！
    // @Data注解会自动为所有private字段生成标准的get/set方法，手动编写会冲突
}