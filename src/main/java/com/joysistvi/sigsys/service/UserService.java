package com.joysistvi.sigsys.service;

import com.joysistvi.sigsys.model.User;

public interface UserService {

    User login(String username, String password);
}
