package com.personalfinancemanager.util.mapper;

import com.personalfinancemanager.domain.model.UserModel;
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

    public static UserModel entityToModel(UserEntity entity) {
        return UserModel.builder()
                .emailAddress(entity.getEmailAddress())
                .firstname(entity.getFirstname())
                .lastname(entity.getLastname())
                .build();
    }
}
