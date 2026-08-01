package com.joysistvi.sigsys.repository;

import com.joysistvi.sigsys.model.User;

public interface UserRepository {
    public User findRecordByUsername(String username);
}
