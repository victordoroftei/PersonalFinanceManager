package com.personalfinancemanager.controller;

import com.personalfinancemanager.domain.dto.UserModel;
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
}
