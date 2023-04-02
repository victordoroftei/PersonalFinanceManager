package com.personalfinancemanager.repository;

import com.personalfinancemanager.domain.entity.NotificationHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.Optional;

public interface NotificationHistoryRepository extends JpaRepository<NotificationHistoryEntity, Integer> {

    Optional<NotificationHistoryEntity> findByNotificationDate(Date date);
}
