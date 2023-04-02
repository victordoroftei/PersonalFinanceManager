package com.personalfinancemanager.service;

import com.personalfinancemanager.domain.model.UserModel;
import com.personalfinancemanager.domain.entity.UserEntity;
import com.personalfinancemanager.exception.ValidationException;
import com.personalfinancemanager.repository.UserRepository;
import com.personalfinancemanager.service.validator.UserValidator;
import com.personalfinancemanager.util.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(13);

    public void addUser(UserModel user) {
        UserValidator.validate(user);

        if (userRepository.findUserEntityByEmailAddress(user.getEmailAddress()).isPresent()) {
            throw new ValidationException("Email address already in use!");
        }

        user.setPassword(encoder.encode(user.getPassword()));

        UserEntity userEntity = UserMapper.modelToEntity(user);
        userRepository.save(userEntity);
    }

    public UserModel getUserById(Integer userId) {
        Optional<UserEntity> userEntityOptional = userRepository.findById(userId);
        if (userEntityOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no user with the provided user ID!");
        }

        UserEntity userEntity = userEntityOptional.get();
        return UserMapper.entityToModel(userEntity);
    }

    public UserModel updateUser(UserModel model, Integer userId) {
        Optional<UserEntity> userEntityOptional = userRepository.findById(userId);
        if (userEntityOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no user with the provided user ID!");
        }

        UserEntity userEntity = userEntityOptional.get();
        userEntity.setFirstname(model.getFirstname());
        userEntity.setLastname(model.getLastname());

        String encoded = encoder.encode(model.getPassword());
        userEntity.setPassword(encoded);

        userRepository.save(userEntity);

        model.setPassword(null);
        return model;
    }
}
