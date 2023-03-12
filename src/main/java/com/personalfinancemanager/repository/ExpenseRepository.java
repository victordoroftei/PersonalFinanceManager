package com.personalfinancemanager.repository;

import com.personalfinancemanager.domain.entity.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseEntity, Integer> {

}
