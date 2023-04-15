package com.personalfinancemanager.repository;

import com.personalfinancemanager.domain.entity.SmsHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmsHistoryRepository extends JpaRepository<SmsHistoryEntity, Integer> {

}
