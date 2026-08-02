package com.joysistvi.sigsys.controller;

import com.joysistvi.sigsys.model.User;
import com.joysistvi.sigsys.service.UserService;
import com.joysistvi.sigsys.service.UserServiceImpl;
import java.util.List;

public class UserController {
    private final UserService userService;

    public UserController() {
        this.userService = new UserServiceImpl();
    }

    /**
     * Authenticates a user given their credentials.
     * @return User object if successful, null if failed/inactive.
     */
    public User login(String username, String password) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            System.err.println("Error: Username and password cannot be empty.");
            return null;
        }

        User user = userService.authenticate(username.trim(), password);
        if (user == null) {
            System.err.println("Error: Invalid username/password or account is inactive.");
        }
        return user;
    }

    /**
     * Registers a new user account into the system.
     */
    public boolean registerUser(User user) {
        if (user == null || user.getUsername() == null || user.getEmail() == null || user.getRole() == null) {
            System.err.println("Error: Missing required user details.");
            return false;
        }
        if (userService.getUserByUsername(user.getUsername().trim()) != null) {
            System.err.println("Error: Username already exists.");
            return false;
        }
        for (User existing : userService.getAllUsers()) {
            if (existing.getEmail() != null && user.getEmail().trim().equalsIgnoreCase(existing.getEmail())) {
                System.err.println("Error: Email address already exists.");
                return false;
            }
        }
        return userService.registerUser(user);
    }

    public boolean deleteUser(int userId) {
        return userService.deleteUser(userId);
    }

    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    public User getUserById(int userId) {
        return userId > 0 ? userService.getUserById(userId) : null;
    }

    public User getUserByUsername(String username) {
        return username == null || username.trim().isEmpty()
                ? null : userService.getUserByUsername(username.trim());
    }

    public boolean updateUser(User user) {
        return userService.updateUser(user);
    }

    public boolean changePassword(int userId, String password) {
        return userService.changePassword(userId, password);
    }
}
