package com.personalfinancemanager.service;

import com.personalfinancemanager.domain.dto.UserModel;
import com.personalfinancemanager.domain.entity.UserEntity;
import com.personalfinancemanager.exception.ValidationException;
import com.personalfinancemanager.repository.UserRepository;
import com.personalfinancemanager.service.validator.UserValidator;
import com.personalfinancemanager.util.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
}
