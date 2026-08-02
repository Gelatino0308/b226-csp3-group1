package com.joysistvi.sigsys.cliview;

import com.joysistvi.sigsys.controller.SystemConfigController;
import com.joysistvi.sigsys.controller.FacultyController;
import com.joysistvi.sigsys.controller.StudentController;
import com.joysistvi.sigsys.model.Faculty;
import com.joysistvi.sigsys.controller.UserController;
import com.joysistvi.sigsys.model.User;
import com.joysistvi.sigsys.model.OverloadRequest;
import com.joysistvi.sigsys.model.SystemConfig;
import com.joysistvi.sigsys.controller.OverloadRequestController;
import com.joysistvi.sigsys.util.ConsoleUIUtil;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.List;

public class AdminView {
    private final User user;
    private final Scanner scanner;
    private final UserController userController;
    private final SystemConfigController configController;
    private final StudentController studentController;
    private final FacultyController facultyController;
    private final OverloadRequestController overloadController;

    public AdminView(User user, Scanner scanner) {
        this.user = user;
        this.scanner = scanner;
        this.userController = new UserController();
        this.configController = new SystemConfigController();
        this.studentController = new StudentController();
        this.facultyController = new FacultyController();
        this.overloadController = new OverloadRequestController();
    }

    public void showMenu() {
        boolean active = true;
        while (active) {
            ConsoleUIUtil.printBoxedSectionHeader("ADMIN DASHBOARD");
            ConsoleUIUtil.printCenteredMenuOption(1, "Create New User Account");
            ConsoleUIUtil.printCenteredMenuOption(2, "Manage Users");
            ConsoleUIUtil.printCenteredMenuOption(3, "Manage System Configuration");
            ConsoleUIUtil.printCenteredMenuOption(4, "Maintain Security (Change Password)");
            ConsoleUIUtil.printCenteredMenuOption(5, "Logout");
            System.out.print("Choose option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> createNewUser();
                case "2" -> manageUsers();
                case "3" -> manageConfiguration();
                case "4" -> changePassword();
                case "5" -> {
                    System.out.println("Logging out from Admin panel...");
                    active = false;
                }
                default -> ConsoleUIUtil.printError("Invalid option. Please try again.");
            }
        }
    }

    private void createNewUser() {
        ConsoleUIUtil.printBoxedSectionHeader("CREATE USER ACCOUNT");
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();

        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        System.out.print("Enter Role (STUDENT / FACULTY / REGISTRAR / ADMIN): ");
        String role = scanner.nextLine().toUpperCase();

        System.out.print("Enter Email Address: ");
        String email = scanner.nextLine();

        if (!role.matches("STUDENT|FACULTY|REGISTRAR|ADMIN")) {
            System.out.println("Error: Invalid role entered.");
            return;
        }
        if ("STUDENT".equals(role)) {
            System.out.println("Student accounts must be created by the Registrar.");
            return;
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPasswordHash(password); // Note: Hash with BCrypt/SHA-256 in production
        newUser.setRole(role);
        newUser.setEmail(email);
        newUser.setActive(true);

        if (userController.registerUser(newUser)) {
            if (createProfile(newUser)) {
                System.out.println("User created successfully with User ID: " + newUser.getUserId());
            } else {
                userController.deleteUser(newUser.getUserId());
                System.out.println("User creation cancelled because the profile could not be created.");
            }
        } else {
            System.out.println("Failed to create user account.");
        }
    }

    private boolean createProfile(User newUser) {
        if ("STUDENT".equals(newUser.getRole())) {
            System.out.println("Student account created. The Registrar must complete the student registry.");
            return true;
        }

        if ("FACULTY".equals(newUser.getRole())) {
            Faculty faculty = new Faculty();
            faculty.setUserId(newUser.getUserId());
            System.out.print("First Name: ");
            faculty.setFirstName(scanner.nextLine().trim());
            System.out.print("Last Name: ");
            faculty.setLastName(scanner.nextLine().trim());
            System.out.print("Department: ");
            faculty.setDepartment(scanner.nextLine().trim());
            return facultyController.registerFaculty(faculty);
        }

        return true;
    }

    private void updateConfig() {
        ConsoleUIUtil.printBoxedSectionHeader("UPDATE SYSTEM CONFIG");
        System.out.print("Setting Key (e.g., MAX_CREDITS_PER_TERM): ");
        String key = scanner.nextLine();

        System.out.print("Setting Value: ");
        String value = scanner.nextLine();

        if (configController.updateConfig(key, value)) {
            System.out.println("Configuration updated successfully!");
        } else {
            System.out.println("Failed to update configuration.");
        }
    }

    private void manageConfiguration() {
        ConsoleUIUtil.printCenteredMenuOption(1, "Set Maximum Units");
        ConsoleUIUtil.printCenteredMenuOption(2, "Set Class Conflict Rule");
        ConsoleUIUtil.printCenteredMenuOption(3, "View All Settings");
        ConsoleUIUtil.printCenteredMenuOption(4, "Review Overload Requests");
        System.out.print("Choose option: ");
        switch (scanner.nextLine()) {
            case "1" -> updateMaximumUnits();
            case "2" -> updateClassConflictRule();
            case "3" -> viewAllConfigs();
            case "4" -> reviewOverloadRequests();
            default -> ConsoleUIUtil.printError("Invalid option.");
        }
    }

    private void updateMaximumUnits() {
        System.out.print("Maximum units allowed per student: ");
        try {
            int units = Integer.parseInt(scanner.nextLine());
            if (units <= 0) {
                System.out.println("Maximum units must be greater than zero.");
                return;
            }
            System.out.println(configController.updateConfig("MAX_CREDITS_PER_TERM", String.valueOf(units))
                    ? "Maximum units updated successfully."
                    : "Maximum units could not be updated.");
        } catch (NumberFormatException exception) {
            System.out.println("Invalid units value.");
        }
    }

    private void updateClassConflictRule() {
        System.out.print("Allow class conflicts? (true/false): ");
        String value = scanner.nextLine().trim().toLowerCase();
        if (!value.equals("true") && !value.equals("false")) {
            System.out.println("Enter only true or false.");
            return;
        }
        System.out.println(configController.updateConfig("allow_class_conflicts", value)
                ? "Class conflict rule updated successfully."
                : "Class conflict rule could not be updated.");
    }

    private void reviewOverloadRequests() {
        List<OverloadRequest> requests = overloadController.getPendingRequests();
        ConsoleUIUtil.printBoxedSectionHeader("PENDING OVERLOAD REQUESTS");
        if (requests.isEmpty()) {
            System.out.println("No pending overload requests.");
            return;
        }
        System.out.printf("%-12s %-12s %-16s %-22s%n", "Request ID", "Student ID", "Requested Units", "Requested At");
        for (OverloadRequest request : requests) {
            System.out.printf("%-12d %-12d %-16d %-22s%n", request.getRequestId(), request.getStudentId(),
                    request.getRequestedUnits(), request.getRequestedAt() == null ? "--" : request.getRequestedAt());
        }
        try {
            System.out.print("Request ID to review (0 to cancel): ");
            int requestId = Integer.parseInt(scanner.nextLine());
            if (requestId == 0) return;
            System.out.print("Approve or disapprove (A/D): ");
            String decision = scanner.nextLine().trim().toUpperCase();
            String status = "A".equals(decision) ? "APPROVED" : "D".equals(decision) ? "DISAPPROVED" : "";
            if (status.isEmpty()) {
                ConsoleUIUtil.printError("Invalid decision.");
                return;
            }
            System.out.println(overloadController.review(requestId, status, user.getUserId())
                    ? "Overload request " + status.toLowerCase() + "."
                    : "Overload request could not be reviewed.");
        } catch (NumberFormatException exception) {
            ConsoleUIUtil.printError("Invalid request ID.");
        }
    }

    private void manageUsers() {
        boolean viewing = true;
        while (viewing) {
            ConsoleUIUtil.printBoxedSectionHeader("MANAGE USERS");
            ConsoleUIUtil.printCenteredMenuOption(1, "Manage Students");
            ConsoleUIUtil.printCenteredMenuOption(2, "Manage Faculties");
            ConsoleUIUtil.printCenteredMenuOption(3, "Manage Registrars");
            ConsoleUIUtil.printCenteredMenuOption(4, "Manage Admins");
            ConsoleUIUtil.printCenteredMenuOption(5, "Manage All Roles");
            ConsoleUIUtil.printCenteredMenuOption(6, "Back");
            System.out.print("Choose option: ");
            switch (scanner.nextLine()) {
                case "1" -> manageUsersByRole("STUDENT", "STUDENTS");
                case "2" -> manageUsersByRole("FACULTY", "FACULTIES");
                case "3" -> manageUsersByRole("REGISTRAR", "REGISTRARS");
                case "4" -> manageUsersByRole("ADMIN", "ADMINS");
                case "5" -> manageAllUsers();
                case "6" -> viewing = false;
                default -> ConsoleUIUtil.printError("Invalid option.");
            }
        }
    }

    private void manageUsersByRole(String role, String title) {
        showUsersByRole(role, title);
        manageAccount(role);
    }

    private void manageAllUsers() {
        showAllUsersByRole();
        manageAccount(null);
    }

    private void showUsersByRole(String role, String title) {
        List<User> users = userController.getAllUsers();
        ConsoleUIUtil.printBoxedSectionHeader(title);
        System.out.printf("%-8s %-20s %-32s %-8s%n", "ID", "USERNAME", "EMAIL", "ACTIVE");
        ConsoleUIUtil.printDivider("=");
        boolean found = false;
        for (User account : users) {
            if (role.equalsIgnoreCase(account.getRole())) {
                System.out.printf("%-8d %-20s %-32s %-8s%n", account.getUserId(), account.getUsername(),
                        account.getEmail(), account.isActive() ? "YES" : "NO");
                found = true;
            }
        }
        if (!found) System.out.println("No users found for this role.");
        ConsoleUIUtil.printDivider("=");
    }

    private void showAllUsersByRole() {
        showUsersByRole("STUDENT", "STUDENTS");
        showUsersByRole("FACULTY", "FACULTIES");
        showUsersByRole("REGISTRAR", "REGISTRARS");
        showUsersByRole("ADMIN", "ADMINS");
    }

    private void manageAccount(String requiredRole) {
        ConsoleUIUtil.printBoxedSectionHeader("ACCOUNT MANAGEMENT");
        System.out.print("Enter User ID (0 to cancel): ");
        int userId;
        try {
            userId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException ex) {
            System.out.println("Invalid User ID.");
            return;
        }
        if (userId == 0) return;

        User account = userController.getUserById(userId);
        if (account == null || (requiredRole != null && !requiredRole.equalsIgnoreCase(account.getRole()))) {
            System.out.println("User account was not found in the selected role.");
            return;
        }

        ConsoleUIUtil.printCenteredMenuOption(1, "Update Email and Role");
        ConsoleUIUtil.printCenteredMenuOption(2, account.isActive() ? "Deactivate Account" : "Activate Account");
        ConsoleUIUtil.printCenteredMenuOption(3, "Reset Password");
        ConsoleUIUtil.printCenteredMenuOption(4, "Delete Account");
        ConsoleUIUtil.printCenteredMenuOption(5, "Cancel");
        System.out.print("Choose action: ");
        String action = scanner.nextLine();

        switch (action) {
            case "1" -> updateAccount(account);
            case "2" -> {
                account.setActive(!account.isActive());
                System.out.println(userController.updateUser(account)
                        ? "Account status updated successfully."
                        : "Account status could not be updated.");
            }
            case "3" -> {
                System.out.print("New password: ");
                String password = scanner.nextLine();
                System.out.println(userController.changePassword(account.getUserId(), password)
                        ? "Password reset successfully."
                        : "Password could not be reset.");
            }
            case "4" -> deleteAccount(account);
            case "5" -> System.out.println("Account action cancelled.");
            default -> ConsoleUIUtil.printError("Invalid action.");
        }
    }

    private void updateAccount(User account) {
        System.out.print("Email (press Enter to keep " + account.getEmail() + "): ");
        String email = scanner.nextLine().trim();
        if (!email.isEmpty()) account.setEmail(email);

        System.out.print("Role (STUDENT/FACULTY/REGISTRAR/ADMIN, press Enter to keep "
                + account.getRole() + "): ");
        String role = scanner.nextLine().trim().toUpperCase();
        if (!role.isEmpty()) {
            if (!role.matches("STUDENT|FACULTY|REGISTRAR|ADMIN")) {
                ConsoleUIUtil.printError("Invalid role. Account was not updated.");
                return;
            }
            account.setRole(role);
        }
        System.out.println(userController.updateUser(account)
                ? "Account updated successfully."
                : "Account could not be updated.");
    }

    private void deleteAccount(User account) {
        if (account.getUserId() == user.getUserId()) {
            System.out.println("You cannot delete the account currently logged in.");
            return;
        }
        System.out.print("Delete " + account.getUsername() + " permanently? (Y/N): ");
        if (!scanner.nextLine().trim().equalsIgnoreCase("Y")) {
            System.out.println("Account deletion cancelled.");
            return;
        }
        System.out.println(userController.deleteUser(account.getUserId())
                ? "Account deleted successfully."
                : "Account could not be deleted.");
    }

    private void changePassword() {
        System.out.print("New password: ");
        String password = scanner.nextLine();
        System.out.println(userController.changePassword(user.getUserId(), password)
                ? "Password changed successfully." : "Password could not be changed.");
    }

    private void viewConfig() {
        ConsoleUIUtil.printBoxedSectionHeader("VIEW SYSTEM CONFIG");
        System.out.print("Setting Key: ");
        String key = scanner.nextLine();

        String val = configController.getConfig(key);
        if (val != null) {
            System.out.println("Value for [" + key + "]: " + val);
        } else {
            System.out.println("Setting key not found.");
        }
    }

    private void viewAllConfigs() {
        ConsoleUIUtil.printBoxedSectionHeader("SYSTEM CONFIGURATION");
        List<SystemConfig> configs = configController.getAllConfigs();
        boolean hasConflictSetting = false;
        for (SystemConfig config : configs) {
            System.out.printf("%-28s : %s%n", config.getSettingKey(), config.getSettingValue());
            if ("allow_class_conflicts".equalsIgnoreCase(config.getSettingKey())) hasConflictSetting = true;
        }
        if (!hasConflictSetting) {
            System.out.println("allow_class_conflicts        : false (default)");
        }
        if (configs.isEmpty()) System.out.println("No saved system settings found.");
    }
}
