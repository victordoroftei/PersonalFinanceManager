package com.personalfinancemanager.domain.repository;

import com.personalfinancemanager.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

}
