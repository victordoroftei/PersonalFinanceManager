package com.personalfinancemanager.controller;

import com.personalfinancemanager.domain.entity.UserSettingsEntity;
import com.personalfinancemanager.domain.model.UserModel;
import com.personalfinancemanager.security.JWTAuthorizationFilter;
import com.personalfinancemanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public String register(@RequestBody UserModel user) {
        userService.addUser(user);
        return "Account has been created successfully!\n";
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public UserModel getUserById(@RequestHeader("Authorization") String token) {
        return userService.getUserById(JWTAuthorizationFilter.getUserIdFromJwt(token));
    }

    @GetMapping("/settings")
    @ResponseStatus(HttpStatus.OK)
    public UserSettingsEntity getUserSettingsById(@RequestHeader("Authorization") String token) {
        return userService.getUserSettingsById(JWTAuthorizationFilter.getUserIdFromJwt(token));
    }

    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public UserModel updateUser(@RequestBody UserModel model, @RequestHeader("Authorization") String token) {
        return userService.updateUser(model, JWTAuthorizationFilter.getUserIdFromJwt(token));
    }

    @PutMapping("/settings")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public UserSettingsEntity updateUserSettings(@RequestBody UserSettingsEntity userSettings, @RequestHeader("Authorization") String token) {
        return userService.updateUserSettings(userSettings, JWTAuthorizationFilter.getUserIdFromJwt(token));
    }
}
