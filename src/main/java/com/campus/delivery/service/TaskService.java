package com.campus.delivery.service;

import com.campus.delivery.entity.Task;
import com.campus.delivery.model.dto.TaskCreateDTO;
import com.campus.delivery.model.vo.TaskVO;

import java.util.List;

public interface TaskService {
    /**
     * 创建任务
     */
    Task createTask(TaskCreateDTO taskCreateDTO, Long userId);

    /**
     * 接单
     */
    Task acceptTask(Long taskId, Long delivererId);

    /**
     * 完成任务
     */
    Task completeTask(Long taskId);

    /**
     * 取消任务
     */
    Task cancelTask(Long taskId, String reason);

    /**
     * 发放积分
     */
    Task issuePoints(Long taskId);

    /**
     * 根据ID获取任务
     */
    Task getTaskById(Long taskId);

    /**
     * 获取用户发布的任务列表
     */
    List<Task> getTasksByRequesterId(Long requesterId);

    /**
     * 获取用户接收的任务列表
     */
    List<Task> getTasksByDelivererId(Long delivererId);

    /**
     * 获取待接单任务列表
     */
    List<Task> getPendingTasks(Integer sortBy);

    /**
     * 处理超时未接单任务
     */
    void processTimeoutTasks();
}
