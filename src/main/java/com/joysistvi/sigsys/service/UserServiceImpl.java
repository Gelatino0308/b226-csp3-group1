package com.joysistvi.sigsys.service;

import com.joysistvi.sigsys.repository.UserRepository;
import com.joysistvi.sigsys.repository.UserRepositoryImpl;
import com.joysistvi.sigsys.model.User;
import org.mindrot.jbcrypt.BCrypt;
import java.util.List;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository = new UserRepositoryImpl();

    @Override
    public User authenticate(String username, String password) {
        if (username == null || password == null) {
            return null;
        }

        String trimmedUser = username.trim();

        User user = userRepository.getUserByUsername(trimmedUser);
        if (user != null) {
            return user.isActive() && matchesPassword(password, user.getPasswordHash()) ? user : null;
        }

        // Only retain the fallback for installations that have not seeded an admin row yet.
        if ("admin".equalsIgnoreCase(trimmedUser) && "admin123".equals(password)) {
            User adminUser = new User();
            adminUser.setUserId(0);
            adminUser.setUsername("admin");
            adminUser.setEmail("admin@system.com");
            adminUser.setRole("ADMIN");
            adminUser.setActive(true);
            return adminUser;
        }
        return null;
    }

    @Override
    public boolean registerUser(User user) {
        if (user == null || isBlank(user.getUsername()) || isBlank(user.getPasswordHash())
                || isBlank(user.getRole()) || isBlank(user.getEmail())) {
            return false;
        }
        user.setUsername(user.getUsername().trim());
        user.setRole(user.getRole().trim().toUpperCase());
        user.setPasswordHash(BCrypt.hashpw(user.getPasswordHash(), BCrypt.gensalt()));
        return userRepository.createUser(user);
    }

    @Override
    public User getUserById(int userId) {
        return userId > 0 ? userRepository.getUserById(userId) : null;
    }

    @Override
    public User getUserByUsername(String username) {
        return isBlank(username) ? null : userRepository.getUserByUsername(username.trim());
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public boolean updateUser(User user) {
        return user != null && user.getUserId() > 0 && !isBlank(user.getRole())
                && !isBlank(user.getEmail()) && userRepository.updateUser(user);
    }

    @Override
    public boolean changePassword(int userId, String newPassword) {
        if (userId <= 0 || isBlank(newPassword)) return false;
        return userRepository.updatePassword(userId, BCrypt.hashpw(newPassword, BCrypt.gensalt()));
    }

    @Override
    public boolean deleteUser(int userId) {
        return userId > 0 && userRepository.deleteUser(userId);
    }

    private boolean matchesPassword(String password, String storedPassword) {
        if (isBlank(storedPassword)) return false;
        if (storedPassword.startsWith("$2a$") || storedPassword.startsWith("$2b$") || storedPassword.startsWith("$2y$")) {
            try {
                return BCrypt.checkpw(password, storedPassword);
            } catch (IllegalArgumentException ex) {
                return false;
            }
        }
        return storedPassword.equals(password);
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
