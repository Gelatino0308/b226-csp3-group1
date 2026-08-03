package com.joysistvi.sigsys.cliview;

import com.joysistvi.sigsys.controller.AcademicPeriodController;
import com.joysistvi.sigsys.controller.EnrollmentController;
import com.joysistvi.sigsys.controller.CourseController;
import com.joysistvi.sigsys.controller.CourseSectionController;
import com.joysistvi.sigsys.controller.StudentController;
import com.joysistvi.sigsys.controller.TranscriptRequestController;
import com.joysistvi.sigsys.controller.UserController;
import com.joysistvi.sigsys.model.AcademicPeriod;
import com.joysistvi.sigsys.model.Course;
import com.joysistvi.sigsys.model.CourseSection;
import com.joysistvi.sigsys.model.Enrollment;
import com.joysistvi.sigsys.model.Student;
import com.joysistvi.sigsys.model.TranscriptRequest;
import com.joysistvi.sigsys.model.User;
import com.joysistvi.sigsys.util.ConsoleUIUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.List;
import java.util.Scanner;

public class RegistrarView {
    private final User user;
    private final Scanner scanner;
    private final TranscriptRequestController transcriptController = new TranscriptRequestController();
    private final EnrollmentController enrollmentController = new EnrollmentController();
    private final StudentController studentController = new StudentController();
    private final AcademicPeriodController periodController = new AcademicPeriodController();
    private final UserController userController = new UserController();
    private final CourseController courseController = new CourseController();
    private final CourseSectionController sectionController = new CourseSectionController();

    public RegistrarView(User user, Scanner scanner) {
        this.user = user;
        this.scanner = scanner;
    }

    public void showMenu() {
        boolean active = true;
        while (active) {
            ConsoleUIUtil.clearAndPrintHeader("REGISTRAR DASHBOARD");
            ConsoleUIUtil.printCenteredMenuOption(1, "Manage Students");
            ConsoleUIUtil.printCenteredMenuOption(2, "Manage Courses");
            ConsoleUIUtil.printCenteredMenuOption(3, "Process Registration");
            ConsoleUIUtil.printCenteredMenuOption(4, "Enroll Student");
            ConsoleUIUtil.printCenteredMenuOption(5, "Manage Academic Periods");
            ConsoleUIUtil.printCenteredMenuOption(6, "Generate Official Transcripts");
            ConsoleUIUtil.printCenteredMenuOption(7, "Process Transcript Request");
            ConsoleUIUtil.printCenteredMenuOption(8, "Logout");
            System.out.print("Choose option: ");
            switch (scanner.nextLine()) {
                case "1" -> manageStudentsMenu();
                case "2" -> manageCourses();
                case "3" -> processRegistration();
                case "4" -> enrollStudent();
                case "5" -> managePeriods();
                case "6" -> generateTranscript();
                case "7" -> processTranscript();
                case "8" -> active = false;
                default -> ConsoleUIUtil.printError("Invalid option.");
            }
        }
    }

    private void manageCourses() {
        boolean active = true;
        while (active) {
            ConsoleUIUtil.clearAndPrintHeader("MANAGE COURSES");
            ConsoleUIUtil.printCenteredMenuOption(1, "View Courses");
            ConsoleUIUtil.printCenteredMenuOption(2, "Add Course");
            ConsoleUIUtil.printCenteredMenuOption(3, "Remove Course");
            ConsoleUIUtil.printCenteredMenuOption(4, "Manage Course Sections");
            ConsoleUIUtil.printCenteredMenuOption(5, "Back");
            System.out.print("Choose option: ");
            switch (scanner.nextLine()) {
                case "1" -> listCourses();
                case "2" -> addCourse();
                case "3" -> removeCourse();
                case "4" -> manageSections();
                case "5" -> active = false;
                default -> ConsoleUIUtil.printError("Invalid option.");
            }
        }
    }

    private void listCourses() {
        ConsoleUIUtil.printBoxedSectionHeader("COURSE CATALOG");
        List<Course> courses = courseController.getAllCourses();
        if (courses.isEmpty()) {
            System.out.println("No courses found.");
            ConsoleUIUtil.promptEnterToContinue(scanner);
            return;
        }
        int[] widths = {10, 18, 30, 10};
        ConsoleUIUtil.printTableHeader(widths, "Course ID", "Code", "Title", "Credits");
        for (Course course : courses) {
            ConsoleUIUtil.printTableRow(widths,
                    String.valueOf(course.getCourseId()),
                    String.valueOf(course.getCourseCode()),
                    String.valueOf(course.getCourseTitle()),
                    String.valueOf(course.getCredits()));
        }
        ConsoleUIUtil.printTableFooter(widths);
        ConsoleUIUtil.promptEnterToContinue(scanner);
    }

    private void addCourse() {
        ConsoleUIUtil.printBoxedSectionHeader("ADD COURSE");
        try {
            Course course = new Course();
            System.out.print("Course code: ");
            course.setCourseCode(scanner.nextLine().trim());
            System.out.print("Course title: ");
            course.setCourseTitle(scanner.nextLine().trim());
            System.out.print("Credits: ");
            course.setCredits(Integer.parseInt(scanner.nextLine()));
            System.out.println(courseController.addCourse(course)
                    ? "Course added with ID " + course.getCourseId() + "." : "Course could not be added.");
            ConsoleUIUtil.promptEnterToContinue(scanner);
        } catch (NumberFormatException ex) {
            System.out.println("Invalid credits value.");
            ConsoleUIUtil.promptEnterToContinue(scanner);
        }
    }

    private void removeCourse() {
        ConsoleUIUtil.printBoxedSectionHeader("REMOVE COURSE");
        List<Course> courses = courseController.getAllCourses();
        if (courses.isEmpty()) {
            System.out.println("No courses found.");
            ConsoleUIUtil.promptEnterToContinue(scanner);
            return;
        }
        int[] widths = {10, 18, 30, 10};
        ConsoleUIUtil.printTableHeader(widths, "Course ID", "Code", "Title", "Credits");
        for (Course course : courses) {
            ConsoleUIUtil.printTableRow(widths,
                    String.valueOf(course.getCourseId()),
                    String.valueOf(course.getCourseCode()),
                    String.valueOf(course.getCourseTitle()),
                    String.valueOf(course.getCredits()));
        }
        ConsoleUIUtil.printTableFooter(widths);
        try {
            System.out.print("Course ID to remove (0 to cancel): ");
            int courseId = Integer.parseInt(scanner.nextLine());
            if (courseId == 0) {
                ConsoleUIUtil.promptEnterToContinue(scanner);
                return;
            }
            System.out.println(courseController.deleteCourse(courseId)
                    ? "Course removed. Related sections and enrollments may also be removed." : "Course not found.");
            ConsoleUIUtil.promptEnterToContinue(scanner);
        } catch (NumberFormatException ex) {
            System.out.println("Invalid course ID.");
            ConsoleUIUtil.promptEnterToContinue(scanner);
        }
    }

    private void manageSections() {
        AcademicPeriod period = periodController.getActivePeriod();
        if (period == null) {
            System.out.println("Create an active academic period first.");
            return;
        }
        boolean active = true;
        while (active) {
            ConsoleUIUtil.clearAndPrintHeader("SECTIONS FOR " + period.getTermName());
            List<CourseSection> sections = sectionController.getSectionsByPeriod(period.getPeriodId());
            if (sections.isEmpty()) {
                System.out.println("No course sections found for this period.");
            } else {
                int[] widths = {10, 10, 10, 12, 16, 12, 10};
                ConsoleUIUtil.printBoxedSectionHeader("CURRENT SECTIONS");
                ConsoleUIUtil.printTableHeader(widths, "Section", "Course", "Faculty", "Days", "Time", "Room", "Capacity");
                for (CourseSection section : sections) {
                    ConsoleUIUtil.printTableRow(widths,
                            String.valueOf(section.getSectionId()),
                            String.valueOf(section.getCourseId()),
                            String.valueOf(section.getFacultyId()),
                            String.valueOf(section.getScheduleDays()),
                            String.valueOf(section.getScheduleTime()),
                            String.valueOf(section.getRoom()),
                            String.valueOf(section.getCapacity()));
                }
                ConsoleUIUtil.printTableFooter(widths);
            }
            ConsoleUIUtil.printCenteredMenuOption(1, "Add Section");
            ConsoleUIUtil.printCenteredMenuOption(2, "Remove Section");
            ConsoleUIUtil.printCenteredMenuOption(3, "Back");
            System.out.print("Choose option: ");
            switch (scanner.nextLine()) {
                case "1" -> addSection(period.getPeriodId());
                case "2" -> removeSection();
                case "3" -> active = false;
                default -> ConsoleUIUtil.printError("Invalid option.");
            }
        }
    }

    private void addSection(int periodId) {
        ConsoleUIUtil.printBoxedSectionHeader("ADD COURSE SECTION");
        try {
            CourseSection section = new CourseSection();
            section.setPeriodId(periodId);
            System.out.print("Course ID: ");
            section.setCourseId(Integer.parseInt(scanner.nextLine()));
            System.out.print("Faculty ID: ");
            section.setFacultyId(Integer.parseInt(scanner.nextLine()));
            System.out.print("Schedule days (e.g. MWF or TTHS): ");
            String scheduleDays = scanner.nextLine().trim().toUpperCase();
            if (!scheduleDays.matches("MWF|TTHS")) {
                System.out.println("Invalid schedule. Only MWF or TTHS is accepted.");
                return;
            }
            section.setScheduleDays(scheduleDays);
            System.out.print("Schedule time (e.g. 8:30am-10am): ");
            section.setScheduleTime(normalizeTimeRange(scanner.nextLine()));
            System.out.print("Room (optional): ");
            section.setRoom(scanner.nextLine().trim());
            System.out.print("Capacity: ");
            section.setCapacity(Integer.parseInt(scanner.nextLine()));
            System.out.println(sectionController.createSection(section)
                    ? "Course section created with ID " + section.getSectionId() + "." : "Course section could not be created.");
            ConsoleUIUtil.promptEnterToContinue(scanner);
        } catch (NumberFormatException ex) {
            System.out.println("Invalid section input.");
            ConsoleUIUtil.promptEnterToContinue(scanner);
        } catch (IllegalArgumentException ex) {
            System.out.println("Invalid schedule time. Use formats such as 8am, 8:30am, or 8am-10am.");
            ConsoleUIUtil.promptEnterToContinue(scanner);
        }
    }

    private String normalizeTimeRange(String input) {
        String value = input == null ? "" : input.trim();
        if (value.isEmpty()) throw new IllegalArgumentException();
        String[] parts = value.split("\\s*(?:-|\\bto\\b)\\s*", 2);
        String start = normalizeTime(parts[0]);
        if (parts.length == 1) return start + "-" + addTwoHours(start);
        return start + "-" + normalizeTime(parts[1]);
    }

    private String addTwoHours(String normalizedTime) {
        int hour = Integer.parseInt(normalizedTime.substring(0, 2));
        int minute = Integer.parseInt(normalizedTime.substring(2, 4));
        int totalMinutes = (hour * 60 + minute + 120) % (24 * 60);
        return String.format("%02d%02dH", totalMinutes / 60, totalMinutes % 60);
    }

    private String normalizeTime(String input) {
        String value = input.trim().toUpperCase(Locale.ROOT).replaceAll("\\s+", "");
        boolean pm = value.endsWith("PM");
        boolean am = value.endsWith("AM");
        if (am || pm) value = value.substring(0, value.length() - 2);
        value = value.replace("H", "");
        String[] timeParts = value.split(":", 2);
        int hour;
        int minute;
        if (timeParts.length == 2) {
            hour = Integer.parseInt(timeParts[0]);
            minute = Integer.parseInt(timeParts[1]);
        } else if (value.length() > 2) {
            hour = Integer.parseInt(value.substring(0, value.length() - 2));
            minute = Integer.parseInt(value.substring(value.length() - 2));
        } else {
            hour = Integer.parseInt(value);
            minute = 0;
        }
        if (am || pm) {
            if (hour < 1 || hour > 12 || minute > 59) throw new IllegalArgumentException();
            if (pm && hour < 12) hour += 12;
            if (am && hour == 12) hour = 0;
        } else if (hour > 23 || minute > 59) {
            throw new IllegalArgumentException();
        }
        return String.format("%02d%02dH", hour, minute);
    }

    private void removeSection() {
        ConsoleUIUtil.printBoxedSectionHeader("REMOVE COURSE SECTION");
        try {
            System.out.print("Section ID to remove (0 to cancel): ");
            int sectionId = Integer.parseInt(scanner.nextLine());
            if (sectionId == 0) {
                ConsoleUIUtil.promptEnterToContinue(scanner);
                return;
            }
            System.out.println(sectionController.deleteSection(sectionId)
                    ? "Section removed. Related enrollments may also be removed." : "Section not found.");
            ConsoleUIUtil.promptEnterToContinue(scanner);
        } catch (NumberFormatException ex) {
            System.out.println("Invalid section ID.");
            ConsoleUIUtil.promptEnterToContinue(scanner);
        }
    }

    private void registerStudent() {
        ConsoleUIUtil.printBoxedSectionHeader("STUDENT ACCOUNTS AWAITING REGISTRATION");
        List<User> accounts = userController.getAllUsers();
        boolean pendingStudentFound = false;
        int[] widths = {10, 18, 30};
        ConsoleUIUtil.printTableHeader(widths, "User ID", "Username", "Email");
        for (User account : accounts) {
            if ("STUDENT".equals(account.getRole()) && studentController.getStudentByUserId(account.getUserId()) == null) {
                ConsoleUIUtil.printTableRow(widths,
                        String.valueOf(account.getUserId()),
                        String.valueOf(account.getUsername()),
                        String.valueOf(account.getEmail()));
                pendingStudentFound = true;
            }
        }
        ConsoleUIUtil.printTableFooter(widths);
        if (!pendingStudentFound) {
            System.out.println("No student accounts are waiting for registration.");
            System.out.println("Ask the Admin to create a user account with role STUDENT first.");
            ConsoleUIUtil.promptEnterToContinue(scanner);
            return;
        }
        try {
            System.out.print("Student user ID: ");
            int userId = Integer.parseInt(scanner.nextLine());
            User account = userController.getUserById(userId);
            if (account == null) {
                System.out.println("No user exists with ID " + userId + ".");
                return;
            }
            if (!"STUDENT".equals(account.getRole())) {
                System.out.println("User " + userId + " has role " + account.getRole() + ", not STUDENT.");
                return;
            }
            if (!account.isActive()) {
                System.out.println("This student account is inactive.");
                return;
            }
            if (studentController.getStudentByUserId(userId) != null) {
                System.out.println("This student already has a registered profile.");
                return;
            }
            Student student = new Student();
            student.setUserId(userId);
            System.out.print("First name: ");
            student.setFirstName(scanner.nextLine().trim());
            System.out.print("Last name: ");
            student.setLastName(scanner.nextLine().trim());
            System.out.print("Date of birth (YYYY-MM-DD, optional): ");
            String date = scanner.nextLine().trim();
            if (!date.isEmpty()) student.setDob(LocalDate.parse(date));
            System.out.print("Phone (optional): ");
            student.setPhone(scanner.nextLine().trim());
            System.out.print("Address (optional): ");
            student.setAddress(scanner.nextLine().trim());
            System.out.println(studentController.createStudentProfile(student)
                    ? "Student registered successfully." : "Student registration failed.");
            ConsoleUIUtil.promptEnterToContinue(scanner);
        } catch (NumberFormatException | DateTimeParseException ex) {
            System.out.println("Invalid student registration input.");
            ConsoleUIUtil.promptEnterToContinue(scanner);
        }
    }

    private void manageStudentsMenu() {
        boolean active = true;
        while (active) {
            ConsoleUIUtil.clearAndPrintHeader("MANAGE STUDENTS");
            ConsoleUIUtil.printCenteredMenuOption(1, "Create New Student Account");
            ConsoleUIUtil.printCenteredMenuOption(2, "Register Student Profile");
            ConsoleUIUtil.printCenteredMenuOption(3, "View/Update Student Records");
            ConsoleUIUtil.printCenteredMenuOption(4, "Back");
            System.out.print("Choose option: ");
            switch (scanner.nextLine()) {
                case "1" -> createStudentAccount();
                case "2" -> registerStudent();
                case "3" -> viewAndUpdateStudents();
                case "4" -> active = false;
                default -> ConsoleUIUtil.printError("Invalid option.");
            }
        }
    }

    private void createStudentAccount() {
        ConsoleUIUtil.printBoxedSectionHeader("CREATE STUDENT ACCOUNT");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        User studentAccount = new User();
        studentAccount.setUsername(username);
        studentAccount.setPasswordHash(password);
        studentAccount.setRole("STUDENT");
        studentAccount.setEmail(email);
        studentAccount.setActive(true);

        if (userController.registerUser(studentAccount)) {
            System.out.println("Student account created. User ID: " + studentAccount.getUserId());
            System.out.println("Use this User ID in Register Student.");
        } else {
            System.out.println("Student account could not be created.");
        }
        ConsoleUIUtil.promptEnterToContinue(scanner);
    }

    private void viewAndUpdateStudents() {
        ConsoleUIUtil.printBoxedSectionHeader("STUDENT RECORDS");
        List<Student> students = studentController.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No student records found.");
            ConsoleUIUtil.promptEnterToContinue(scanner);
            return;
        }
        int[] widths = {10, 10, 18, 18, 10};
        ConsoleUIUtil.printTableHeader(widths, "Student ID", "User ID", "First Name", "Last Name", "GPA");
        for (Student student : students) {
            ConsoleUIUtil.printTableRow(widths,
                    String.valueOf(student.getStudentId()),
                    String.valueOf(student.getUserId()),
                    String.valueOf(student.getFirstName()),
                    String.valueOf(student.getLastName()),
                    String.format(Locale.ROOT, "%.2f", student.getCumulativeGpa()));
        }
        ConsoleUIUtil.printTableFooter(widths);
        try {
            System.out.print("Student ID to update (0 to cancel): ");
            int studentId = Integer.parseInt(scanner.nextLine());
            if (studentId == 0) {
                ConsoleUIUtil.promptEnterToContinue(scanner);
                return;
            }
            Student student = studentController.getStudentById(studentId);
            if (student == null) {
                System.out.println("Student not found.");
                ConsoleUIUtil.promptEnterToContinue(scanner);
                return;
            }
            System.out.print("New phone (blank keeps current): ");
            String phone = scanner.nextLine();
            System.out.print("New address (blank keeps current): ");
            String address = scanner.nextLine();
            if (!phone.trim().isEmpty()) student.setPhone(phone.trim());
            if (!address.trim().isEmpty()) student.setAddress(address.trim());
            System.out.println(studentController.updateStudentProfile(student)
                    ? "Student details updated." : "Student details could not be updated.");
            ConsoleUIUtil.promptEnterToContinue(scanner);
        } catch (NumberFormatException ex) {
            System.out.println("Invalid student ID.");
            ConsoleUIUtil.promptEnterToContinue(scanner);
        }
    }

    private void processRegistration() {
        ConsoleUIUtil.clearAndPrintHeader("PROCESS REGISTRATION");
        List<Enrollment> enrollments = enrollmentController.getAllEnrollments();
        if (enrollments.isEmpty()) {
            System.out.println("No registration requests found.");
            ConsoleUIUtil.promptEnterToContinue(scanner);
            return;
        }
        int[] widths = {12, 12, 12, 16};
        ConsoleUIUtil.printTableHeader(widths, "Enrollment", "Student ID", "Section ID", "Status");
        for (Enrollment enrollment : enrollments) {
            ConsoleUIUtil.printTableRow(widths,
                    String.valueOf(enrollment.getEnrollmentId()),
                    String.valueOf(enrollment.getStudentId()),
                    String.valueOf(enrollment.getSectionId()),
                    String.valueOf(enrollment.getRegistrationStatus()));
        }
        ConsoleUIUtil.printTableFooter(widths);
        try {
            System.out.print("Enrollment ID: ");
            int id = Integer.parseInt(scanner.nextLine());
            ConsoleUIUtil.printCenteredMenuOption(1, "ENROLLED");
            ConsoleUIUtil.printCenteredMenuOption(2, "DROPPED");
            ConsoleUIUtil.printCenteredMenuOption(0, "Cancel");
            System.out.print("Choose new status: ");
            int statusChoice = Integer.parseInt(scanner.nextLine());
            if (statusChoice == 0) {
                System.out.println("Registration update cancelled.");
                return;
            }
            String status = switch (statusChoice) {
                case 1 -> "ENROLLED";
                case 2 -> "DROPPED";
                default -> "";
            };
            if (status.isEmpty()) {
                System.out.println("Invalid status choice.");
                ConsoleUIUtil.promptEnterToContinue(scanner);
                return;
            }
            System.out.println(enrollmentController.updateRegistrationStatus(id, status)
                    ? "Registration status updated." : "Registration status could not be updated.");
            ConsoleUIUtil.promptEnterToContinue(scanner);
        } catch (NumberFormatException ex) {
            System.out.println("Invalid enrollment ID.");
            ConsoleUIUtil.promptEnterToContinue(scanner);
        }
    }

    private void enrollStudent() {
        ConsoleUIUtil.printBoxedSectionHeader("ENROLL STUDENT");
        try {
            System.out.print("Student ID: ");
            int studentId = Integer.parseInt(scanner.nextLine());
            AcademicPeriod period = periodController.getActivePeriod();
            if (period == null) {
                System.out.println("No active academic period is available.");
                ConsoleUIUtil.promptEnterToContinue(scanner);
                return;
            }
            List<CourseSection> sections = sectionController.getSectionsByPeriod(period.getPeriodId());
            if (sections.isEmpty()) {
                System.out.println("No course sections are available for the active period.");
                ConsoleUIUtil.promptEnterToContinue(scanner);
                return;
            }
            ConsoleUIUtil.clearAndPrintHeader("AVAILABLE COURSE SECTIONS");
            int[] widths = {10, 14, 30, 12, 16, 12, 12};
            ConsoleUIUtil.printTableHeader(widths, "Section", "Course", "Title", "Schedule", "Time", "Capacity", "Enrolled");
            for (CourseSection section : sections) {
                Course course = courseController.getCourseById(section.getCourseId());
                if (course == null) continue;
                int enrolledCount = enrollmentController.getSectionEnrollments(section.getSectionId()).size();
                ConsoleUIUtil.printTableRow(widths,
                        String.valueOf(section.getSectionId()),
                        String.valueOf(course.getCourseCode()),
                        String.valueOf(course.getCourseTitle()),
                        String.valueOf(section.getScheduleDays()),
                        String.valueOf(section.getScheduleTime()),
                        String.valueOf(section.getCapacity()),
                        String.format(Locale.ROOT, "%d%s", enrolledCount, enrolledCount >= section.getCapacity() ? " FULL" : ""));
            }
            ConsoleUIUtil.printTableFooter(widths);
            System.out.print("Course section ID: ");
            int sectionId = Integer.parseInt(scanner.nextLine());
            System.out.println(enrollmentController.enrollStudent(studentId, sectionId)
                    ? "Student enrolled with pending status." : "Student could not be enrolled.");
        } catch (NumberFormatException ex) {
            ConsoleUIUtil.printError("Invalid enrollment input.");
        }
        ConsoleUIUtil.promptEnterToContinue(scanner);
    }

    private void managePeriods() {
        ConsoleUIUtil.clearAndPrintHeader("MANAGE ACADEMIC PERIODS");
        List<AcademicPeriod> periods = periodController.getAllPeriods();
        if (periods.isEmpty()) {
            System.out.println("No academic periods found.");
            ConsoleUIUtil.printDivider("-");
        } else {
            int[] widths = {10, 24, 14, 14, 10};
            ConsoleUIUtil.printTableHeader(widths, "Period ID", "Term Name", "Start Date", "End Date", "Active");
            for (AcademicPeriod period : periods) {
                ConsoleUIUtil.printTableRow(widths,
                        String.valueOf(period.getPeriodId()),
                        String.valueOf(period.getTermName()),
                        String.valueOf(period.getStartDate()),
                        String.valueOf(period.getEndDate()),
                        String.valueOf(period.isActive()));
            }
            ConsoleUIUtil.printTableFooter(widths);
        }
        try {
            System.out.print("Create period? (Y/N): ");
            if (!scanner.nextLine().equalsIgnoreCase("Y")) return;
            AcademicPeriod period = new AcademicPeriod();
            System.out.print("Term name: ");
            period.setTermName(scanner.nextLine().trim());
            System.out.print("Start date (YYYY-MM-DD, MM/DD/YYYY, or M/D/YYYY): ");
            period.setStartDate(parseDate(scanner.nextLine()));
            System.out.print("End date (YYYY-MM-DD, MM/DD/YYYY, or M/D/YYYY): ");
            period.setEndDate(parseDate(scanner.nextLine()));
            System.out.print("Active? (Y/N): ");
            period.setActive(scanner.nextLine().equalsIgnoreCase("Y"));
            System.out.println(periodController.createPeriod(period)
                    ? "Academic period created." : "Academic period could not be created.");
        } catch (Exception ex) {
            System.out.println("Invalid academic period input.");
        }
        ConsoleUIUtil.promptEnterToContinue(scanner);
    }

    private LocalDate parseDate(String input) {
        String value = input.trim();
        DateTimeFormatter[] formats = {
                DateTimeFormatter.ISO_LOCAL_DATE,
                DateTimeFormatter.ofPattern("M/d/yyyy"),
                DateTimeFormatter.ofPattern("MM/dd/yyyy"),
                DateTimeFormatter.ofPattern("yyyy/MM/dd")
        };
        for (DateTimeFormatter format : formats) {
            try {
                return LocalDate.parse(value, format);
            } catch (DateTimeParseException ignored) {
            }
        }
        throw new DateTimeParseException("Unsupported date", value, 0);
    }

    private void generateTranscript() {
        ConsoleUIUtil.printBoxedSectionHeader("GENERATE OFFICIAL TRANSCRIPT");
        try {
            System.out.print("Student ID: ");
            int studentId = Integer.parseInt(scanner.nextLine());
            Student student = studentController.getStudentById(studentId);
            if (student == null) {
                System.out.println("Student not found.");
                ConsoleUIUtil.promptEnterToContinue(scanner);
                return;
            }
            ConsoleUIUtil.clearAndPrintHeader("TRANSCRIPT FOR " + student.getFirstName() + " " + student.getLastName() +
                    " (Student " + studentId + ")");
            List<Enrollment> enrollments = enrollmentController.getStudentEnrollments(studentId);
            if (enrollments.isEmpty()) {
                System.out.println("No enrollment records found.");
            } else {
                int[] widths = {10, 16, 16, 12};
                ConsoleUIUtil.printTableHeader(widths, "Section ID", "Status", "Grade", "GPA");
                for (Enrollment enrollment : enrollments) {
                    ConsoleUIUtil.printTableRow(widths,
                            String.valueOf(enrollment.getSectionId()),
                            String.valueOf(enrollment.getRegistrationStatus()),
                            value(enrollment.getFinalGrade()),
                            enrollment.getGpaPoints() == null ? "N/A" : String.valueOf(enrollment.getGpaPoints()));
                }
                ConsoleUIUtil.printTableFooter(widths);
            }
        } catch (NumberFormatException ex) {
            System.out.println("Invalid student ID.");
        }
        ConsoleUIUtil.promptEnterToContinue(scanner);
    }

    private void processTranscript() {
        ConsoleUIUtil.clearAndPrintHeader("PROCESS TRANSCRIPT REQUEST");
        List<TranscriptRequest> requests = transcriptController.getAllRequests();
        if (requests.isEmpty()) {
            System.out.println("No transcript requests found.");
            ConsoleUIUtil.promptEnterToContinue(scanner);
            return;
        }
        int[] widths = {12, 12, 16, 16};
        ConsoleUIUtil.printTableHeader(widths, "Request ID", "Student ID", "Status", "Request Date");
        for (TranscriptRequest request : requests) {
            ConsoleUIUtil.printTableRow(widths,
                    String.valueOf(request.getRequestId()),
                    String.valueOf(request.getStudentId()),
                    String.valueOf(request.getStatus()),
                    String.valueOf(request.getRequestDate()));
        }
        ConsoleUIUtil.printTableFooter(widths);
        try {
            System.out.print("Request ID: ");
            int requestId = Integer.parseInt(scanner.nextLine());
            System.out.print("New status (PROCESSING/COMPLETED/REJECTED): ");
            String status = scanner.nextLine();
            System.out.println(transcriptController.updateRequestStatus(requestId, status, user.getUserId())
                    ? "Transcript request updated." : "Transcript request could not be updated.");
        } catch (NumberFormatException ex) {
            ConsoleUIUtil.printError("Invalid request ID.");
        }
        ConsoleUIUtil.promptEnterToContinue(scanner);
    }

    private String value(String value) {
        return value == null || value.trim().isEmpty() ? "N/A" : value;
    }
}
