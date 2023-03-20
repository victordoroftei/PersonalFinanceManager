package com.personalfinancemanager.repository;

import com.personalfinancemanager.domain.entity.ReceiptItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReceiptItemRepository extends JpaRepository<ReceiptItemEntity, Integer> {

    @Query(value = "SELECT * FROM receipt_items WHERE receipt_id = :receiptId", nativeQuery = true)
    List<ReceiptItemEntity> findAllByReceiptId(Integer receiptId);
}
