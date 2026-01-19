package com.campus.delivery.schedule;

import com.campus.delivery.entity.Task;
import com.campus.delivery.repository.TaskRepository;
import com.campus.delivery.service.TaskService;
import com.campus.delivery.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 定时任务类，用于处理超时任务
 */
@Component
public class TaskSchedule {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    /**
     * 处理超时未接单任务
     * 每5分钟执行一次
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    @Transactional
    public void processTimeoutPendingTasks() {
        // 查找状态为0（待接单）的任务
        List<Task> pendingTasks = taskRepository.findByTaskStatus(0);
        LocalDateTime now = LocalDateTime.now();

        for (Task task : pendingTasks) {
            // 检查任务是否超时
            if (task.getExpectedCompletionTime() != null &&
                    task.getExpectedCompletionTime().isBefore(now)) {
                // 超时未接单，取消任务并解冻积分
                try {
                    // 取消任务
                    task.setTaskStatus(4); // 已取消
                    task.setCancelReason("超时未接单，系统自动取消");
                    task.setUpdateTime(LocalDateTime.now());
                    taskRepository.save(task);

                    // 解冻积分（假设有这个方法）
                    if (userService != null) {
                        userService.unfreezePoints(task.getRequesterId(), task.getPointAmount());
                    }

                    System.out.println("✅ 已取消超时任务: " + task.getId() + "，任务标题: " + task.getTitle());
                } catch (Exception e) {
                    System.err.println("❌ 取消超时任务失败: " + task.getId() + ", 错误: " + e.getMessage());
                }
            }
        }
    }

    /**
     * 处理派送超时任务
     * 每5分钟执行一次
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void processTimeoutDeliveringTasks() {
        // 查找状态为1（配送中）的任务
        List<Task> deliveringTasks = taskRepository.findByTaskStatus(1);
        LocalDateTime now = LocalDateTime.now();

        for (Task task : deliveringTasks) {
            if (task.getExpectedCompletionTime() != null &&
                    task.getExpectedCompletionTime().isBefore(now)) {
                // 派送超时，发送提醒通知
                sendDeliveryTimeoutNotification(task);
            }
        }
    }

    /**
     * 发送派送超时通知
     */
    private void sendDeliveryTimeoutNotification(Task task) {
        // 这里可以发送系统消息、邮件、短信等
        System.out.println("⚠️ 警告: 任务 " + task.getId() + " 已超时，请尽快处理");
        System.out.println("   任务标题: " + task.getTitle());
        System.out.println("   期望完成时间: " + task.getExpectedCompletionTime());
        System.out.println("   当前时间: " + LocalDateTime.now());

        // TODO: 实现实际的通知功能
        // 1. 可以调用消息服务发送给派送员
        // 2. 可以调用邮件服务发送邮件
        // 3. 可以推送消息到前端
    }
}