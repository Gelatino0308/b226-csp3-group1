package com.joysistvi.sigsys.repository;

import com.joysistvi.sigsys.dao.UserDao;
import com.joysistvi.sigsys.model.User;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {
    private final UserDao userDao = new UserDao();

    @Override
    public User authenticateUser(String username, String passwordHash) {
        return userDao.authenticateUser(username, passwordHash);
    }

    @Override
    public boolean createUser(User user) {
        return userDao.createUser(user);
    }

    @Override
    public User getUserById(int userId) {
        return userDao.getUserById(userId);
    }

    @Override
    public User getUserByUsername(String username) {
        return userDao.getUserByUsername(username);
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public boolean updateUser(User user) {
        return userDao.updateUser(user);
    }

    @Override
    public boolean updatePassword(int userId, String newPasswordHash) {
        return userDao.updatePassword(userId, newPasswordHash);
    }
}