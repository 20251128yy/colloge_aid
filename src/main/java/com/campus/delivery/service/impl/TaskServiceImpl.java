package com.campus.delivery.service.impl;

import com.campus.delivery.entity.Task;
import com.campus.delivery.model.dto.TaskCreateDTO;
import com.campus.delivery.repository.TaskRepository;
import com.campus.delivery.service.TaskService;
import com.campus.delivery.service.UserService;
import com.campus.delivery.util.RedisUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtil redisUtil;

    private ObjectMapper objectMapper = new ObjectMapper();

    private static final String PENDING_TASKS_KEY = "pending_tasks:";

    @Override
    @Transactional
    public Task createTask(TaskCreateDTO taskCreateDTO, Long userId) {
        Task task = new Task();
        BeanUtils.copyProperties(taskCreateDTO, task);

        // 设置任务基本信息
        task.setRequesterId(userId);
        task.setTaskStatus(0); // 待接单
        task.setAuditStatus(1); // 审核通过

        // 冻结需求方积分
        userService.freezePoints(userId, taskCreateDTO.getPointAmount());

        Task savedTask = taskRepository.save(task);

        // 清除待接单任务缓存
//        redisUtil.delete(PENDING_TASKS_KEY + "0");
//        redisUtil.delete(PENDING_TASKS_KEY + "1");

        return savedTask;
    }

    @Override
    @Transactional
    public Task acceptTask(Long taskId, Long delivererId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("任务不存在"));

        if (task.getTaskStatus() != 0) {
            throw new RuntimeException("任务已被接单或已完成");
        }

        task.setDelivererId(delivererId);
        task.setTaskStatus(1); // 配送中

        Task updatedTask = taskRepository.save(task);

        // 清除待接单任务缓存
//        redisUtil.delete(PENDING_TASKS_KEY + "0");
//        redisUtil.delete(PENDING_TASKS_KEY + "1");

        return updatedTask;
    }

    @Override
    @Transactional
    public Task completeTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("任务不存在"));

        if (task.getTaskStatus() != 1) {
            throw new RuntimeException("任务未在配送中");
        }

        task.setTaskStatus(2); // 已完成
        task.setCompleteTime(LocalDateTime.now());

        Task updatedTask = taskRepository.save(task);

        // 清除待接单任务缓存
//        redisUtil.delete(PENDING_TASKS_KEY + "0");
//        redisUtil.delete(PENDING_TASKS_KEY + "1");

        return updatedTask;
    }

    @Override
    @Transactional
    public Task cancelTask(Long taskId, String reason) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("任务不存在"));

        if (task.getTaskStatus() == 3 || task.getTaskStatus() == 4) {
            throw new RuntimeException("任务已完成或已取消");
        }

        task.setTaskStatus(4); // 已取消
        task.setCancelReason(reason);

        // 解冻积分
        userService.unfreezePoints(task.getRequesterId(), task.getPointAmount());

        Task updatedTask = taskRepository.save(task);

        // 清除待接单任务缓存
//        redisUtil.delete(PENDING_TASKS_KEY + "0");
//        redisUtil.delete(PENDING_TASKS_KEY + "1");

        return updatedTask;
    }

    @Override
    @Transactional
    public Task issuePoints(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("任务不存在"));

        if (task.getTaskStatus() != 2) {
            throw new RuntimeException("任务未完成");
        }

        // 更新任务状态
        task.setTaskStatus(3); // 积分已发放

        // 将积分从冻结状态转移到派送方账户
        userService.updatePoints(task.getRequesterId(), -task.getPointAmount(), 2);
        userService.updatePoints(task.getDelivererId(), task.getPointAmount(), 1);

        Task updatedTask = taskRepository.save(task);

        // 清除待接单任务缓存
//        redisUtil.delete(PENDING_TASKS_KEY + "0");
//        redisUtil.delete(PENDING_TASKS_KEY + "1");

        return updatedTask;
    }

    @Override
    public Task getTaskById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("任务不存在"));
    }

    @Override
    public List<Task> getTasksByRequesterId(Long requesterId) {
        return taskRepository.findByRequesterId(requesterId);
    }

    @Override
    public List<Task> getTasksByDelivererId(Long delivererId) {
        return taskRepository.findByDelivererId(delivererId);
    }

    @Override
    public List<Task> getPendingTasks(Integer sortBy) {
        // 尝试从缓存获取
        String cacheKey = PENDING_TASKS_KEY + sortBy;
//        Object cachedTasks = redisUtil.get(cacheKey);

//        if (cachedTasks != null) {
//            try {
//                return objectMapper.readValue(cachedTasks.toString(),
//                        objectMapper.getTypeFactory().constructCollectionType(List.class, Task.class));
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
//        }

        // 缓存未命中，从数据库查询
        List<Task> tasks;
        if (sortBy == 1) {
            tasks = taskRepository.findByTaskStatusOrderByExpectedCompletionTimeAsc(0);
        } else {
            tasks = taskRepository.findByTaskStatusOrderByPointAmountDesc(0);
        }

        // 保存到缓存（5分钟过期）
//        try {
////            redisUtil.set(cacheKey, objectMapper.writeValueAsString(tasks), 5, TimeUnit.MINUTES);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }

        return tasks;
    }

    @Override
    @Transactional
    public void processTimeoutTasks() {
        // 处理超时未接单的任务
        List<Task> pendingTasks = taskRepository.findByTaskStatus(0);
        LocalDateTime now = LocalDateTime.now();

        for (Task task : pendingTasks) {
            if (task.getExpectedCompletionTime().isBefore(now)) {
                // 超时未接单，取消任务并解冻积分
                task.setTaskStatus(4); // 已取消
                task.setCancelReason("超时未接单");
                taskRepository.save(task);

                userService.unfreezePoints(task.getRequesterId(), task.getPointAmount());
            }
        }

        // 清除待接单任务缓存
//        redisUtil.delete(PENDING_TASKS_KEY + "0");
//        redisUtil.delete(PENDING_TASKS_KEY + "1");
    }
}
