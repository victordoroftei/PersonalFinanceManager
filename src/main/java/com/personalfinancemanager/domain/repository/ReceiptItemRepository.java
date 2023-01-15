package com.personalfinancemanager.domain.repository;

import com.personalfinancemanager.domain.entity.ReceiptItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiptItemRepository extends JpaRepository<ReceiptItemEntity, Integer> {

}
