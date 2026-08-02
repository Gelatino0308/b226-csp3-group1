package com.joysistvi.sigsys.cliview;

import com.joysistvi.sigsys.controller.UserController;
import com.joysistvi.sigsys.model.User;
import com.joysistvi.sigsys.util.ClearScreenUtil;
import com.joysistvi.sigsys.util.InputCheckerUtil;

import java.util.Scanner;

public class LoginView {
    private final UserController userController;
    private final Scanner scanner;

    public LoginView(UserController userController, Scanner scanner) {
        this.userController = userController;
        this.scanner = scanner;
    }

    public User showWelcomeGate() {
        while (true) {
            ClearScreenUtil.clearScreen();
            System.out.println("\n========================================================");
            System.out.println("      WELCOME TO STUDENT INFORMATION & GRADING SYSTEM    ");
            System.out.println("=========================================================");
            System.out.println("[1] Login");
            System.out.println("[0] Exit System");
            System.out.print("Choice: ");

            int choice = InputCheckerUtil.readIntChoice(scanner);

            switch (choice) {
                case 1:
                    User loggedInUser = handleLogin();
                    if (loggedInUser != null) {
                        return loggedInUser; // passes control back to App with user obj
                    }
                    break;
                case 0:
                    return null; // indicates application exit
                default:
                    System.out.println("\n> Invalid choice. Please try again.");
            }

            System.out.print("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }

    private User handleLogin() {
        System.out.println("\n--- USER LOGIN ---");
        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        User user = userController.handleLogin(username, password);

        if (user == null) {
            System.out.println("\n> Invalid username or password.");
        }

        return user;
    }

}
