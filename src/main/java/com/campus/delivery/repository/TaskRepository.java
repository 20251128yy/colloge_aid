package com.campus.delivery.repository;

import com.campus.delivery.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByRequesterId(Long requesterId);
    List<Task> findByDelivererId(Long delivererId);
    List<Task> findByTaskStatus(Integer taskStatus);
    List<Task> findByTaskStatusOrderByPointAmountDesc(Integer taskStatus);
    List<Task> findByTaskStatusOrderByExpectedCompletionTimeAsc(Integer taskStatus);
}
