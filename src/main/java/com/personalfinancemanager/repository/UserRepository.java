package com.personalfinancemanager.repository;

import com.personalfinancemanager.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Optional<UserEntity> findUserEntityByEmailAddress(String emailAddress);
}
