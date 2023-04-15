package com.personalfinancemanager.service;

import com.personalfinancemanager.domain.entity.SmsHistoryEntity;
import com.personalfinancemanager.domain.entity.UserEntity;
import com.personalfinancemanager.repository.SmsHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SmsHistoryService {

    private final SmsHistoryRepository smsHistoryRepository;

    public void addSmsHistoryRecord(UserEntity user, String body) {
        SmsHistoryEntity entity = SmsHistoryEntity.builder()
                .id(user.getId())
                .user(user)
                .phoneNumber(user.getPhoneNumber())
                .body(body)
                .timestamp(LocalDateTime.now())
                .build();

        smsHistoryRepository.save(entity);
    }
}
