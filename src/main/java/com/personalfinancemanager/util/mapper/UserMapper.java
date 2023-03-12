package com.personalfinancemanager.util.mapper;

import com.personalfinancemanager.domain.dto.UserModel;
import com.personalfinancemanager.domain.entity.UserEntity;

public class UserMapper {

    public static UserEntity modelToEntity(UserModel model) {
        return UserEntity.builder()
                .emailAddress(model.getEmailAddress())
                .firstname(model.getFirstname())
                .lastname(model.getLastname())
                .password(model.getPassword())
                .build();
    }
}
