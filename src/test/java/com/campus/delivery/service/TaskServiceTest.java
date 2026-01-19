package com.campus.delivery.service;

import com.campus.delivery.entity.Task;
import com.campus.delivery.entity.User;
import com.campus.delivery.model.dto.TaskCreateDTO;
import com.campus.delivery.repository.TaskRepository;
import com.campus.delivery.service.impl.TaskServiceImpl;
import com.campus.delivery.util.RedisUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserService userService;

    @Mock
    private RedisUtil redisUtil;

    @InjectMocks
    private TaskServiceImpl taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTask() {
        // 准备测试数据
        Long userId = 1L;
        TaskCreateDTO createDTO = new TaskCreateDTO();
        createDTO.setTitle("取快递");
        createDTO.setDescription("取顺丰快递");
        createDTO.setFromLocation("南门");
        createDTO.setToLocation("宿舍1号楼");
        createDTO.setItemType(1);
        createDTO.setExpectedCompletionTime(LocalDateTime.now().plusHours(2));
        createDTO.setPointAmount(20);

        // 模拟数据库操作
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
            Task task = invocation.getArgument(0);
            task.setId(1L);
            return task;
        });

        // 执行测试
        Task task = taskService.createTask(createDTO, userId);

        // 验证结果
        assertNotNull(task);
        assertEquals(1L, task.getId());
        assertEquals(createDTO.getTitle(), task.getTitle());
        assertEquals(createDTO.getDescription(), task.getDescription());
        assertEquals(createDTO.getFromLocation(), task.getFromLocation());
        assertEquals(createDTO.getToLocation(), task.getToLocation());
        assertEquals(createDTO.getItemType(), task.getItemType());
        assertEquals(createDTO.getExpectedCompletionTime(), task.getExpectedCompletionTime());
        assertEquals(createDTO.getPointAmount(), task.getPointAmount());
        assertEquals(userId, task.getRequesterId());
        assertEquals(0, task.getTaskStatus());
        assertEquals(1, task.getAuditStatus());

        // 验证方法调用
        verify(userService, times(1)).freezePoints(userId, createDTO.getPointAmount());
        verify(taskRepository, times(1)).save(any(Task.class));
//        verify(redisUtil, times(1)).delete("pending_tasks:0");
//        verify(redisUtil, times(1)).delete("pending_tasks:1");
    }

    @Test
    void testAcceptTask() {
        // 准备测试数据
        Long taskId = 1L;
        Long delivererId = 2L;

        Task task = new Task();
        task.setId(taskId);
        task.setTitle("取快递");
        task.setTaskStatus(0); // 待接单

        // 模拟数据库操作
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // 执行测试
        Task acceptedTask = taskService.acceptTask(taskId, delivererId);

        // 验证结果
        assertNotNull(acceptedTask);
        assertEquals(taskId, acceptedTask.getId());
        assertEquals(delivererId, acceptedTask.getDelivererId());
        assertEquals(1, acceptedTask.getTaskStatus()); // 配送中

        // 验证方法调用
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).save(any(Task.class));
//        verify(redisUtil, times(1)).delete("pending_tasks:0");
//        verify(redisUtil, times(1)).delete("pending_tasks:1");
    }

    @Test
    void testAcceptTaskAlreadyAccepted() {
        // 准备测试数据
        Long taskId = 1L;
        Long delivererId = 2L;

        Task task = new Task();
        task.setId(taskId);
        task.setTitle("取快递");
        task.setTaskStatus(1); // 已接单

        // 模拟数据库操作
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        // 执行测试并验证异常
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            taskService.acceptTask(taskId, delivererId);
        });

        assertEquals("任务已被接单或已完成", exception.getMessage());
    }

    @Test
    void testCompleteTask() {
        // 准备测试数据
        Long taskId = 1L;

        Task task = new Task();
        task.setId(taskId);
        task.setTitle("取快递");
        task.setTaskStatus(1); // 配送中

        // 模拟数据库操作
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // 执行测试
        Task completedTask = taskService.completeTask(taskId);

        // 验证结果
        assertNotNull(completedTask);
        assertEquals(taskId, completedTask.getId());
        assertEquals(2, completedTask.getTaskStatus()); // 已完成
        assertNotNull(completedTask.getCompleteTime());

        // 验证方法调用
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).save(any(Task.class));
//        verify(redisUtil, times(1)).delete("pending_tasks:0");
//        verify(redisUtil, times(1)).delete("pending_tasks:1");
    }

    @Test
    void testCancelTask() {
        // 准备测试数据
        Long taskId = 1L;
        String reason = "临时不需要了";
        Long requesterId = 1L;
        Integer pointAmount = 20;

        Task task = new Task();
        task.setId(taskId);
        task.setTitle("取快递");
        task.setTaskStatus(0); // 待接单
        task.setRequesterId(requesterId);
        task.setPointAmount(pointAmount);

        // 模拟数据库操作
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // 执行测试
        Task canceledTask = taskService.cancelTask(taskId, reason);

        // 验证结果
        assertNotNull(canceledTask);
        assertEquals(taskId, canceledTask.getId());
        assertEquals(4, canceledTask.getTaskStatus()); // 已取消
        assertEquals(reason, canceledTask.getCancelReason());

        // 验证方法调用
        verify(taskRepository, times(1)).findById(taskId);
        verify(userService, times(1)).unfreezePoints(requesterId, pointAmount);
        verify(taskRepository, times(1)).save(any(Task.class));
//        verify(redisUtil, times(1)).delete("pending_tasks:0");
//        verify(redisUtil, times(1)).delete("pending_tasks:1");
    }

    @Test
    void testIssuePoints() {
        // 准备测试数据
        Long taskId = 1L;
        Long requesterId = 1L;
        Long delivererId = 2L;
        Integer pointAmount = 20;

        Task task = new Task();
        task.setId(taskId);
        task.setTitle("取快递");
        task.setTaskStatus(2); // 已完成
        task.setRequesterId(requesterId);
        task.setDelivererId(delivererId);
        task.setPointAmount(pointAmount);

        // 模拟数据库操作
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // 执行测试
        Task issuedTask = taskService.issuePoints(taskId);

        // 验证结果
        assertNotNull(issuedTask);
        assertEquals(taskId, issuedTask.getId());
        assertEquals(3, issuedTask.getTaskStatus()); // 积分已发放

        // 验证方法调用
        verify(taskRepository, times(1)).findById(taskId);
        verify(userService, times(1)).updatePoints(requesterId, -pointAmount, 2);
        verify(userService, times(1)).updatePoints(delivererId, pointAmount, 1);
        verify(taskRepository, times(1)).save(any(Task.class));
//        verify(redisUtil, times(1)).delete("pending_tasks:0");
//        verify(redisUtil, times(1)).delete("pending_tasks:1");
    }

    @Test
    void testGetTaskById() {
        // 准备测试数据
        Long taskId = 1L;

        Task task = new Task();
        task.setId(taskId);
        task.setTitle("取快递");

        // 模拟数据库操作
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        // 执行测试
        Task foundTask = taskService.getTaskById(taskId);

        // 验证结果
        assertNotNull(foundTask);
        assertEquals(taskId, foundTask.getId());
        assertEquals("取快递", foundTask.getTitle());

        // 验证方法调用
        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    void testGetPendingTasks() {
        // 准备测试数据
        Integer sortBy = 0; // 按积分降序

        Task task = new Task();
        task.setId(1L);
        task.setTitle("取快递");
        task.setTaskStatus(0);
        task.setPointAmount(20);

        List<Task> tasks = Collections.singletonList(task);

        // 模拟数据库操作
//        when(redisUtil.get("pending_tasks:" + sortBy)).thenReturn(null);
        when(taskRepository.findByTaskStatusOrderByPointAmountDesc(0)).thenReturn(tasks);

        // 执行测试
        List<Task> pendingTasks = taskService.getPendingTasks(sortBy);

        // 验证结果
        assertNotNull(pendingTasks);
        assertEquals(1, pendingTasks.size());
        assertEquals(task.getId(), pendingTasks.get(0).getId());

        // 验证方法调用
//        verify(redisUtil, times(1)).get("pending_tasks:" + sortBy);
        verify(taskRepository, times(1)).findByTaskStatusOrderByPointAmountDesc(0);
//        verify(redisUtil, times(1)).set(anyString(), anyString(), anyLong(), any());
    }

    @Test
    void testProcessTimeoutTasks() {
        // 准备测试数据
        Task task = new Task();
        task.setId(1L);
        task.setTitle("取快递");
        task.setTaskStatus(0);
        task.setRequesterId(1L);
        task.setPointAmount(20);
        task.setExpectedCompletionTime(LocalDateTime.now().minusHours(1)); // 已超时

        List<Task> pendingTasks = Collections.singletonList(task);

        // 模拟数据库操作
        when(taskRepository.findByTaskStatus(0)).thenReturn(pendingTasks);
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // 执行测试
        taskService.processTimeoutTasks();

        // 验证方法调用
        verify(taskRepository, times(1)).findByTaskStatus(0);
        verify(taskRepository, times(1)).save(any(Task.class));
        verify(userService, times(1)).unfreezePoints(task.getRequesterId(), task.getPointAmount());
//        verify(redisUtil, times(1)).delete("pending_tasks:0");
//        verify(redisUtil, times(1)).delete("pending_tasks:1");
    }
}
