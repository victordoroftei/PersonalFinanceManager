package com.personalfinancemanager.repository;

import com.personalfinancemanager.domain.entity.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Integer> {

    @Query(value = "SELECT SUM(price) FROM expenses WHERE user_id = :userId", nativeQuery = true)
    Double getSumByUserId(Integer userId);

}
