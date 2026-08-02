package com.joysistvi.sigsys.service;

import com.joysistvi.sigsys.model.User;
import java.util.List;

public interface UserService {
    User authenticate(String username, String password);
    boolean registerUser(User user);
    User getUserById(int userId);
    User getUserByUsername(String username);
    List<User> getAllUsers();
    boolean updateUser(User user);
    boolean changePassword(int userId, String newPassword);
    boolean deleteUser(int userId);
}
