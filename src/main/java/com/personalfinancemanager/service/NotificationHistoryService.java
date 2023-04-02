package com.personalfinancemanager.service;

import com.personalfinancemanager.domain.entity.NotificationHistoryEntity;
import com.personalfinancemanager.repository.NotificationHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationHistoryService {

    private final NotificationHistoryRepository notificationHistoryRepository;

    public boolean checkIfRecordAlreadyExists() {
        Date date = new Date(Instant.now().toEpochMilli());

        Optional<NotificationHistoryEntity> optional = notificationHistoryRepository.findByNotificationDate(date);
        return optional.isPresent();
    }

    public void addRecord() {
        Date date = new Date(Instant.now().toEpochMilli());
        NotificationHistoryEntity entity = NotificationHistoryEntity.builder()
                .notificationDate(date)
                .build();

        notificationHistoryRepository.save(entity);
    }
}
