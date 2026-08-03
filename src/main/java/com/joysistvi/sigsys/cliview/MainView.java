package com.joysistvi.sigsys.cliview;

import com.joysistvi.sigsys.controller.UserController;
import com.joysistvi.sigsys.model.User;
import com.joysistvi.sigsys.util.ConsoleUIUtil;

import java.util.Scanner;

public class MainView {
    private final UserController userController;
    private final Scanner scanner;

    public MainView() {
        this.userController = new UserController();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        ConsoleUIUtil.clearScreen();
        ConsoleUIUtil.printBigTitle("WELCOME TO...");
        ConsoleUIUtil.printBannerTitle();

        boolean running = true;
        while (running) {
            ConsoleUIUtil.printBoxedSectionHeader("MAIN MENU");
            ConsoleUIUtil.printCenteredMenuOption(1, "Login");
            ConsoleUIUtil.printCenteredMenuOption(2, "Exit System");
            System.out.print("Select an option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> handleLogin();
                case "2" -> {
                    System.out.println("Exiting system. Goodbye!");
                    running = false;
                }
                default -> ConsoleUIUtil.printError("Invalid selection. Please try again.");
            }
        }
    }

    private void handleLogin() {
        ConsoleUIUtil.printBoxedSectionHeader("USER LOGIN");
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        User loggedInUser = userController.login(username, password);

        if (loggedInUser != null) {
            ConsoleUIUtil.printBigTitle("Welcome, " + loggedInUser.getUsername() + " [" + loggedInUser.getRole() + "]");
            routeUserByRole(loggedInUser);
        } else {
            System.out.println("Login failed! Please check your credentials.");
            ConsoleUIUtil.promptEnterToContinue(scanner);
        }
    }

    private void routeUserByRole(User user) {
        switch (user.getRole().toUpperCase()) {
            case "STUDENT" -> new StudentView(user, scanner).showMenu();
            case "FACULTY" -> new FacultyView(user, scanner).showMenu();
            case "REGISTRAR" -> new RegistrarView(user, scanner).showMenu();
            case "ADMIN" -> new AdminView(user, scanner).showMenu();
            default -> System.out.println("Error: Unrecognized user role.");
        }
    }
}