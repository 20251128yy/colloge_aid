package com.campus.delivery.controller;

import com.campus.delivery.common.Result;
import com.campus.delivery.entity.Task;
import com.campus.delivery.model.dto.TaskCreateDTO;
import com.campus.delivery.model.vo.TaskVO;
import com.campus.delivery.service.TaskService;
import com.campus.delivery.util.JwtUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务控制器
 */
@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    private TaskService taskService;

    /**
     * 创建任务
     */
    @PostMapping("/create")
    public Result<TaskVO> createTask(@Validated @RequestBody TaskCreateDTO taskCreateDTO, HttpServletRequest request) {
        try {
            // 从JWT令牌中获取用户ID
            String token = request.getHeader("Authorization").substring(7);
            Long userId = JwtUtil.getUserIdFromToken(token);

            Task task = taskService.createTask(taskCreateDTO, userId);
            TaskVO taskVO = new TaskVO();
            BeanUtils.copyProperties(task, taskVO);
            return Result.success(taskVO);
        } catch (Exception e) {
            return Result.badRequest(e.getMessage());
        }
    }

    /**
     * 接单
     */
    @PostMapping("/{taskId}/accept")
    public Result<TaskVO> acceptTask(@PathVariable Long taskId, HttpServletRequest request) {
        try {
            // 从JWT令牌中获取用户ID
            String token = request.getHeader("Authorization").substring(7);
            Long userId = JwtUtil.getUserIdFromToken(token);

            Task task = taskService.acceptTask(taskId, userId);
            TaskVO taskVO = new TaskVO();
            BeanUtils.copyProperties(task, taskVO);
            return Result.success(taskVO);
        } catch (Exception e) {
            return Result.badRequest(e.getMessage());
        }
    }

    /**
     * 完成任务
     */
    @PostMapping("/{taskId}/complete")
    public Result<TaskVO> completeTask(@PathVariable Long taskId, HttpServletRequest request) {
        try {
            // 从JWT令牌中获取用户ID
            String token = request.getHeader("Authorization").substring(7);
            Long userId = JwtUtil.getUserIdFromToken(token);

            Task task = taskService.completeTask(taskId);
            TaskVO taskVO = new TaskVO();
            BeanUtils.copyProperties(task, taskVO);
            return Result.success(taskVO);
        } catch (Exception e) {
            return Result.badRequest(e.getMessage());
        }
    }

    /**
     * 取消任务
     */
    @PostMapping("/{taskId}/cancel")
    public Result<TaskVO> cancelTask(@PathVariable Long taskId, @RequestParam String reason, HttpServletRequest request) {
        try {
            // 从JWT令牌中获取用户ID
            String token = request.getHeader("Authorization").substring(7);
            Long userId = JwtUtil.getUserIdFromToken(token);

            Task task = taskService.cancelTask(taskId, reason);
            TaskVO taskVO = new TaskVO();
            BeanUtils.copyProperties(task, taskVO);
            return Result.success(taskVO);
        } catch (Exception e) {
            return Result.badRequest(e.getMessage());
        }
    }

    /**
     * 发放积分
     */
    @PostMapping("/{taskId}/issuePoints")
    public Result<TaskVO> issuePoints(@PathVariable Long taskId, HttpServletRequest request) {
        try {
            // 从JWT令牌中获取用户ID
            String token = request.getHeader("Authorization").substring(7);
            Long userId = JwtUtil.getUserIdFromToken(token);

            Task task = taskService.issuePoints(taskId);
            TaskVO taskVO = new TaskVO();
            BeanUtils.copyProperties(task, taskVO);
            return Result.success(taskVO);
        } catch (Exception e) {
            return Result.badRequest(e.getMessage());
        }
    }
//    @GetMapping("/admin/list")
//    public Result<Page<TaskVO>> listAllTasksForAdmin(
//            @RequestParam(required = false) Integer auditStatus,
//            @RequestParam(required = false) Integer taskStatus,
//            @RequestParam(required = false) String keyword,
//            @RequestParam(defaultValue = "1") Integer pageNum, // 页码从1开始
//            @RequestParam(defaultValue = "10") Integer pageSize) {
//        try {
//            // JPA Pageable 页码从0开始，所以要减1
//            Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
//            // 调用Service查询
//            Page<Task> taskPage = taskService.getAllTasks(auditStatus, taskStatus, keyword, pageable);
//
//            // 转换为VO分页对象
//            Page<TaskVO> taskVOPage = taskPage.map(task -> {
//                TaskVO taskVO = new TaskVO();
//                BeanUtils.copyProperties(task, taskVO);
//                return taskVO;
//            });
//
//            return Result.success(taskVOPage);
//        } catch (Exception e) {
//            return Result.badRequest(e.getMessage());
//        }
//    }
@GetMapping("/admin/list")
public Result<Map<String, Object>> listAllTasksForAdmin(
        @RequestParam(required = false) Integer auditStatus,
        @RequestParam(required = false) Integer taskStatus,
        @RequestParam(required = false) String keyword,
        @RequestParam(defaultValue = "1") Integer pageNum,
        @RequestParam(defaultValue = "10") Integer pageSize) {
    try {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<Task> taskPage = taskService.getAllTasks(auditStatus, taskStatus, keyword, pageable);

        // 手动构建纯Map数据（避免VO序列化问题）
        List<Map<String, Object>> taskList = new ArrayList<>();
        for (Task task : taskPage.getContent()) {
            Map<String, Object> taskMap = new HashMap<>();
            // 只返回前端需要的字段，避免冗余
            taskMap.put("id", task.getId());
            taskMap.put("title", task.getTitle());
            taskMap.put("description", task.getDescription());
            taskMap.put("fromLocation", task.getFromLocation());
            taskMap.put("toLocation", task.getToLocation());
            taskMap.put("taskStatus", task.getTaskStatus());
            taskMap.put("auditStatus", task.getAuditStatus());
            taskMap.put("pointAmount", task.getPointAmount());
            // 时间字段格式化为字符串
            taskMap.put("createTime", task.getCreateTime() != null ?
                    task.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null);

            taskList.add(taskMap);
        }

        // 构建分页返回数据
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("list", taskList); // 前端习惯的list字段（替代content）
        resultMap.put("total", taskPage.getTotalElements()); // 前端习惯的total字段（替代totalElements）

        System.out.println("返回给前端的纯JSON数据：" + resultMap); // 调试日志
        return Result.success(resultMap);
    } catch (Exception e) {
        System.out.println("查询失败：" + e.getMessage());
        return Result.badRequest(e.getMessage());
    }
}
    /**
     * 查询任务列表
     */
    @GetMapping("/list")
    public Result<List<TaskVO>> listTasks(
            @RequestParam(defaultValue = "0") Integer sortBy,
            @RequestParam(required = false) Integer taskStatus,
            HttpServletRequest request) {
        try {
            List<Task> tasks;
            // 根据参数查询不同类型的任务列表
            if (taskStatus != null) {
                // 查询指定状态的任务
                // 这里需要根据实际情况实现
                tasks = taskService.getPendingTasks(sortBy);
            } else {
                // 查询待接单任务
                tasks = taskService.getPendingTasks(sortBy);
            }

            // 转换为VO列表
            List<TaskVO> taskVOs = new ArrayList<>();
            for (Task task : tasks) {
                TaskVO taskVO = new TaskVO();
                BeanUtils.copyProperties(task, taskVO);
                taskVOs.add(taskVO);
            }

            return Result.success(taskVOs);
        } catch (Exception e) {
            return Result.badRequest(e.getMessage());
        }
    }

    /**
     * 查询个人发布的任务
     */
    @GetMapping("/my/published")
    public Result<List<TaskVO>> listMyPublishedTasks(HttpServletRequest request) {
        try {
            // 从JWT令牌中获取用户ID
            String token = request.getHeader("Authorization").substring(7);
            Long userId = JwtUtil.getUserIdFromToken(token);

            List<Task> tasks = taskService.getTasksByRequesterId(userId);
            List<TaskVO> taskVOs = new ArrayList<>();
            for (Task task : tasks) {
                TaskVO taskVO = new TaskVO();
                BeanUtils.copyProperties(task, taskVO);
                taskVOs.add(taskVO);
            }

            return Result.success(taskVOs);
        } catch (Exception e) {
            return Result.badRequest(e.getMessage());
        }
    }

    /**
     * 查询个人接单任务
     */
    @GetMapping("/my/accepted")
    public Result<List<TaskVO>> listMyAcceptedTasks(HttpServletRequest request) {
        try {
            // 从JWT令牌中获取用户ID
            String token = request.getHeader("Authorization").substring(7);
            Long userId = JwtUtil.getUserIdFromToken(token);

            List<Task> tasks = taskService.getTasksByDelivererId(userId);
            List<TaskVO> taskVOs = new ArrayList<>();
            for (Task task : tasks) {
                TaskVO taskVO = new TaskVO();
                BeanUtils.copyProperties(task, taskVO);
                taskVOs.add(taskVO);
            }

            return Result.success(taskVOs);
        } catch (Exception e) {
            return Result.badRequest(e.getMessage());
        }
    }

    /**
     * 查询任务详情
     */
    @GetMapping("/{taskId}")
    public Result<TaskVO> getTaskDetail(@PathVariable Long taskId) {
        try {
            Task task = taskService.getTaskById(taskId);
            TaskVO taskVO = new TaskVO();
            BeanUtils.copyProperties(task, taskVO);
            return Result.success(taskVO);
        } catch (Exception e) {
            return Result.badRequest(e.getMessage());
        }
    }
}
