package com.joysistvi.sigsys.repository;

import com.joysistvi.sigsys.model.User;
import java.util.List;

public interface UserRepository {
    User authenticateUser(String username, String passwordHash);
    boolean createUser(User user);
    User getUserById(int userId);
    User getUserByUsername(String username);
    List<User> getAllUsers();
    boolean updateUser(User user);
    boolean updatePassword(int userId, String newPasswordHash);
}