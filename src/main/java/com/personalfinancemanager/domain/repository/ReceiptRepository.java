package com.personalfinancemanager.domain.repository;

import com.personalfinancemanager.domain.entity.ReceiptEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiptRepository extends JpaRepository<ReceiptEntity, Integer> {

}
