package com.personalfinancemanager.repository;

import com.personalfinancemanager.domain.entity.ReceiptEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReceiptRepository extends JpaRepository<ReceiptEntity, Integer> {

    @Query(value = "SELECT * FROM receipts LIMIT 1;", nativeQuery = true)
    ReceiptEntity getOne();

    @Query(value = "SELECT * FROM receipts WHERE user_id = :userId", nativeQuery = true)
    List<ReceiptEntity> findAllByUserId(Integer userId);

    @Query(value = "SELECT SUM(calculated_total) FROM receipts WHERE user_id = :userId", nativeQuery = true)
    Double getSumByUserId(Integer userId);
}
