package com.joysistvi.sigsys.service;

import com.joysistvi.sigsys.model.User;
import com.joysistvi.sigsys.repository.UserRepository;
import com.joysistvi.sigsys.util.InputCheckerUtil;
import org.mindrot.jbcrypt.BCrypt;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository; // Composition

    // Constructor injection
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User login(String username, String password) {
        if (!InputCheckerUtil.isValueProvided(username) || !InputCheckerUtil.isValueProvided(password)) {
            System.out.println("Username and password cannot be empty.");
            return null;
        }
        // prevents non-existent accounts from logging in
        User user = userRepository.findRecordByUsername(username);
        if (user == null) {
            return null;
        }

        // hash password securely
        boolean isMatch;
        try {
            isMatch = BCrypt.checkpw(password, user.getPasswordHash());
        } catch (Exception e) { // fallback in case an unhashed password is in db
            isMatch = password.equals(user.getPasswordHash());
        }

        return isMatch ? user : null;
    }
}
