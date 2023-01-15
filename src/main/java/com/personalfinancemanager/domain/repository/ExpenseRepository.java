package com.personalfinancemanager.domain.repository;

import com.personalfinancemanager.domain.entity.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Integer> {

}
