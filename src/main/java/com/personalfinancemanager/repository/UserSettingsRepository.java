package com.personalfinancemanager.repository;

import com.personalfinancemanager.domain.entity.UserSettingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserSettingsRepository extends JpaRepository<UserSettingsEntity, Integer> {

    @Query(value = "SELECT * FROM user_settings WHERE user_id = :userId", nativeQuery = true)
    UserSettingsEntity findByUserId(Integer userId);
}
