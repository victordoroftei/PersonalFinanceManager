package com.personalfinancemanager.repository;

import com.personalfinancemanager.domain.entity.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Integer> {

    @Query(value = "SELECT SUM(amount) FROM invoices WHERE user_id = :userId AND paid = 1", nativeQuery = true)
    Double getSumByUserId(Integer userId);

    @Query(value = "SELECT * FROM invoices WHERE user_id = :userId", nativeQuery = true)
    List<InvoiceEntity> findAllByUserId(Integer userId);
}
