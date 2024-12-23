package com.personalfinancemanager.repository;

import com.personalfinancemanager.domain.entity.ExpenseEntity;
import com.personalfinancemanager.domain.entity.ExpenseTypeSum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Integer> {

    @Query(value = "SELECT SUM(price) FROM expenses WHERE user_id = :userId", nativeQuery = true)
    Double getSumByUserId(Integer userId);

    @Query(value = "SELECT * FROM expenses WHERE user_id = :userId", nativeQuery = true)
    List<ExpenseEntity> findAllByUserId(Integer userId);
}
