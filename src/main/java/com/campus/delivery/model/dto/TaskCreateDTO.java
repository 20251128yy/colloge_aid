package com.campus.delivery.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;


import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class TaskCreateDTO {
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getExpectedCompletionTime() {
        return expectedCompletionTime;
    }

    public void setExpectedCompletionTime(LocalDateTime expectedCompletionTime) {
        this.expectedCompletionTime = expectedCompletionTime;
    }

    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    public Integer getItemType() {
        return itemType;
    }

    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }

    public Integer getPointAmount() {
        return pointAmount;
    }

    public void setPointAmount(Integer pointAmount) {
        this.pointAmount = pointAmount;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    @NotBlank(message = "任务标题不能为空")
    private String title;

    private String description;

    @NotBlank(message = "取件地点不能为空")
    private String fromLocation;

    @NotBlank(message = "送达地点不能为空")
    private String toLocation;

    @NotNull(message = "物品类型不能为空")
    private Integer itemType;

    @NotNull(message = "期望完成时间不能为空")
    private LocalDateTime expectedCompletionTime;

    @NotNull(message = "积分数量不能为空")
    private Integer pointAmount;
}
