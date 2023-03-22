package com.personalfinancemanager.repository;

import com.personalfinancemanager.domain.entity.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Integer> {

    @Query(value = "SELECT SUM(amount) FROM invoices WHERE user_id = :userId", nativeQuery = true)
    Double getSumByUserId(Integer userId);
}
