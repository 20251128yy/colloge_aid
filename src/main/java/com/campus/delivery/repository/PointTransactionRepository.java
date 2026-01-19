package com.campus.delivery.repository;

import com.campus.delivery.entity.PointTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointTransactionRepository extends JpaRepository<PointTransaction, Long> {
    List<PointTransaction> findByUserId(Long userId);
    List<PointTransaction> findByTaskId(Long taskId);
    List<PointTransaction> findByTransactionType(Integer transactionType);
}
