package com.joysistvi.sigsys.controller;

import com.joysistvi.sigsys.model.User;
import com.joysistvi.sigsys.service.UserService;

public class UserController {

    private final UserService userService; // Composition

    // Constructor injection
    public UserController(UserService userService) {
        this.userService = userService;
    }

    public User handleLogin(String username, String password) {
        return userService.login(username, password);
    }
}
