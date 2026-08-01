package com.joysistvi.sigsys;

import com.joysistvi.sigsys.cliview.DashboardView;
import com.joysistvi.sigsys.cliview.LoginView;
import com.joysistvi.sigsys.config.DbConnection;
import com.joysistvi.sigsys.controller.UserController;
import com.joysistvi.sigsys.model.User;
import com.joysistvi.sigsys.repository.UserRepository;
import com.joysistvi.sigsys.repository.UserRepositoryImpl;
import com.joysistvi.sigsys.service.UserService;
import com.joysistvi.sigsys.service.UserServiceImpl;

import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DbConnection dbConnection = new DbConnection();

        UserRepository userRepository = new UserRepositoryImpl(dbConnection);
        UserService userService = new UserServiceImpl(userRepository);
        UserController userController = new UserController(userService);

        // Welcome Gate View
        LoginView loginView = new  LoginView(userController, scanner);

        boolean appRunning =  true;

        // MAIN APPLICATION LOOP
        while (appRunning) {
            User authenticatedUser = loginView.showWelcomeGate();

            if (authenticatedUser == null) {
                appRunning = false;
            }
            else {
                // User Dashboard View
                DashboardView dashboardView = new DashboardView(userController, scanner);
                dashboardView.run(authenticatedUser);
            }
        }

        System.out.println("Exiting Student Information & Grading System...");
        scanner.close();
    }
}
