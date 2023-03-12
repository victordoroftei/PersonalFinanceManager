package com.personalfinancemanager.repository;

import com.personalfinancemanager.domain.entity.ReceiptEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReceiptRepository extends JpaRepository<ReceiptEntity, Integer> {

    @Query(value = "SELECT * FROM receipts LIMIT 1;", nativeQuery = true)
    ReceiptEntity getOne();
}
