package com.joysistvi.sigsys.cliview;

import com.joysistvi.sigsys.controller.UserController;
import com.joysistvi.sigsys.model.User;
import com.joysistvi.sigsys.util.ClearScreenUtil;
import com.joysistvi.sigsys.util.InputCheckerUtil;

import java.util.Scanner;

public class DashboardView {
    private final UserController userController;
    private final Scanner scanner;

    public DashboardView(UserController userController, Scanner scanner) {
        this.userController = userController;
        this.scanner = scanner;
    }

    public void run (User user) {
        boolean loggedIn = true;

        while (loggedIn) {
            ClearScreenUtil.clearScreen();
            System.out.println("\n=============================================");
            System.out.println("      STUDENT INFORMATION & GRADING SYSTEM    ");
            System.out.println("==============================================");
            System.out.println("\nWelcome, " + user.getUsername() + " (" + user.getRole() + ")");

            // Route to specific role menus
            switch (user.getRole().toUpperCase()) {
                case "ADMIN":
                    loggedIn = showAdminMenu();
                    break;
                case "REGISTRAR":
                    loggedIn = showRegistrarMenu();
                    break;
                case "FACULTY":
                    loggedIn = showFacultyMenu();
                    break;
                case "STUDENT":
                    loggedIn = showStudentMenu();
                    break;
                default:
                    System.out.println("System Error: Unknown role.");
                    loggedIn = false;
            }
        }
        System.out.println("> Logging out...");
    }


    private boolean showAdminMenu() {
        System.out.println("[1] Manage Users");
        System.out.println("[2] Manage System Configuration");
        System.out.println("[3] Maintain Security");
        System.out.println("[0] Logout");
        System.out.print("Choice: ");

        int choice = InputCheckerUtil.readIntChoice(scanner);

        // TODO: implement and add admin methods
        switch (choice) {
            case 1:
            case 2:
            case 0:
                return false;
            default:
                System.out.println("\n> Invalid choice. Please try again.");
        }

        return true;
    }

    private boolean showRegistrarMenu() {
        System.out.println("[1] Manage Student Records");
        System.out.println("[2] Process Registration");
        System.out.println("[3] Enroll Student");
        System.out.println("[4] Manage Academic Periods");
        System.out.println("[5] Generate Official Transcripts");
        System.out.println("[0] Logout");
        System.out.print("Choice: ");

        int choice = InputCheckerUtil.readIntChoice(scanner);

        // TODO: implement and add registrar methods
        switch (choice) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 0:
                return false;
            default:
                System.out.println("\n> Invalid choice. Please try again.");
        }

        return true;
    }

    private boolean showFacultyMenu() {
        System.out.println("[1] View Assigned Courses");
        System.out.println("[2] View Assigned Rosters");
        System.out.println("[3] Record Course Grades");
        System.out.println("[4] Input Attendance");
        System.out.println("[0] Logout");
        System.out.print("Choice: ");

        int choice = InputCheckerUtil.readIntChoice(scanner);

        // TODO: implement and add faculty methods
        switch (choice) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 0:
                return false;
            default:
                System.out.println("\n> Invalid choice. Please try again.");
        }

        return true;
    }

    private boolean showStudentMenu() {
        System.out.println("[1] View Personal Information");
        System.out.println("[2] Register for Courses");
        System.out.println("[3] View Course Schedule");
        System.out.println("[4] View Final Grades & GPA");
        System.out.println("[5] Request Academic Transcripts");
        System.out.println("[0] Logout");
        System.out.print("Choice: ");

        int choice = InputCheckerUtil.readIntChoice(scanner);

        // TODO: implement and add student methods
        switch (choice) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 0:
                return false;
            default:
                System.out.println("\n> Invalid choice. Please try again.");
        }

        return true;
    }

}
