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
            System.out.println("\n=================================");
            System.out.println("       REGISTRAR DASHBOARD       ");
            System.out.println("=================================");
            System.out.println("[1] Manage Students");
            System.out.println("[2] Manage Courses");
            System.out.println("[3] Process Registration");
            System.out.println("[4] Enroll Student");
            System.out.println("[5] Manage Academic Periods");
            System.out.println("[6] Generate Official Transcripts");
            System.out.println("[7] Process Transcript Request");
            System.out.println("[8] Logout");
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
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void manageCourses() {
        boolean active = true;
        while (active) {
            System.out.println("\n--- MANAGE COURSES ---");
            System.out.println("[1] View Courses");
            System.out.println("[2] Add Course");
            System.out.println("[3] Remove Course");
            System.out.println("[4] Manage Course Sections");
            System.out.println("[5] Back");
            System.out.print("Choose option: ");
            switch (scanner.nextLine()) {
                case "1" -> listCourses();
                case "2" -> addCourse();
                case "3" -> removeCourse();
                case "4" -> manageSections();
                case "5" -> active = false;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void listCourses() {
        List<Course> courses = courseController.getAllCourses();
        System.out.println("\n--- COURSE CATALOG ---");
        if (courses.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }
        for (Course course : courses) {
            System.out.printf("Course %d | %s | %s | Credits: %d%n", course.getCourseId(),
                    course.getCourseCode(), course.getCourseTitle(), course.getCredits());
        }
    }

    private void addCourse() {
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
        } catch (NumberFormatException ex) {
            System.out.println("Invalid credits value.");
        }
    }

    private void removeCourse() {
        listCourses();
        try {
            System.out.print("Course ID to remove (0 to cancel): ");
            int courseId = Integer.parseInt(scanner.nextLine());
            if (courseId == 0) return;
            System.out.println(courseController.deleteCourse(courseId)
                    ? "Course removed. Related sections and enrollments may also be removed." : "Course not found.");
        } catch (NumberFormatException ex) {
            System.out.println("Invalid course ID.");
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
            System.out.println("\n--- SECTIONS FOR " + period.getTermName() + " ---");
            for (CourseSection section : sectionController.getSectionsByPeriod(period.getPeriodId())) {
                System.out.printf("Section %d | Course %d | Faculty %d | %s %s | Room %s | Capacity %d%n",
                        section.getSectionId(), section.getCourseId(), section.getFacultyId(), section.getScheduleDays(),
                        section.getScheduleTime(), section.getRoom(), section.getCapacity());
            }
            System.out.println("[1] Add Section");
            System.out.println("[2] Remove Section");
            System.out.println("[3] Back");
            System.out.print("Choose option: ");
            switch (scanner.nextLine()) {
                case "1" -> addSection(period.getPeriodId());
                case "2" -> removeSection();
                case "3" -> active = false;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void addSection(int periodId) {
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
        } catch (NumberFormatException ex) {
            System.out.println("Invalid section input.");
        } catch (IllegalArgumentException ex) {
            System.out.println("Invalid schedule time. Use formats such as 8am, 8:30am, or 8am-10am.");
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
        try {
            System.out.print("Section ID to remove (0 to cancel): ");
            int sectionId = Integer.parseInt(scanner.nextLine());
            if (sectionId == 0) return;
            System.out.println(sectionController.deleteSection(sectionId)
                    ? "Section removed. Related enrollments may also be removed." : "Section not found.");
        } catch (NumberFormatException ex) {
            System.out.println("Invalid section ID.");
        }
    }

    private void registerStudent() {
        List<User> accounts = userController.getAllUsers();
        System.out.println("\n--- STUDENT ACCOUNTS AWAITING REGISTRATION ---");
        boolean pendingStudentFound = false;
        for (User account : accounts) {
            if ("STUDENT".equals(account.getRole()) && studentController.getStudentByUserId(account.getUserId()) == null) {
                System.out.printf("User %d | %s | %s%n", account.getUserId(), account.getUsername(), account.getEmail());
                pendingStudentFound = true;
            }
        }
        if (!pendingStudentFound) {
            System.out.println("No student accounts are waiting for registration.");
            System.out.println("Ask the Admin to create a user account with role STUDENT first.");
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
        } catch (NumberFormatException | DateTimeParseException ex) {
            System.out.println("Invalid student registration input.");
        }
    }

    private void manageStudentsMenu() {
        boolean active = true;
        while (active) {
            System.out.println("\n--- MANAGE STUDENTS ---");
            System.out.println("[1] Create New Student Account");
            System.out.println("[2] Register Student Profile");
            System.out.println("[3] View/Update Student Records");
            System.out.println("[4] Back");
            System.out.print("Choose option: ");
            switch (scanner.nextLine()) {
                case "1" -> createStudentAccount();
                case "2" -> registerStudent();
                case "3" -> viewAndUpdateStudents();
                case "4" -> active = false;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void createStudentAccount() {
        System.out.println("\n--- CREATE STUDENT ACCOUNT ---");
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
    }

    private void viewAndUpdateStudents() {
        List<Student> students = studentController.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No student records found.");
            return;
        }
        for (Student student : students) {
            System.out.printf("Student %d | User %d | %s %s | GPA %.2f%n", student.getStudentId(),
                    student.getUserId(), student.getFirstName(), student.getLastName(), student.getCumulativeGpa());
        }
        try {
            System.out.print("Student ID to update (0 to cancel): ");
            int studentId = Integer.parseInt(scanner.nextLine());
            if (studentId == 0) return;
            Student student = studentController.getStudentById(studentId);
            if (student == null) {
                System.out.println("Student not found.");
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
        } catch (NumberFormatException ex) {
            System.out.println("Invalid student ID.");
        }
    }

    private void processRegistration() {
        List<Enrollment> enrollments = enrollmentController.getAllEnrollments();
        if (enrollments.isEmpty()) {
            System.out.println("No registration requests found.");
            return;
        }
        for (Enrollment enrollment : enrollments) {
            System.out.printf("Enrollment %d | Student %d | Section %d | Status %s%n", enrollment.getEnrollmentId(),
                    enrollment.getStudentId(), enrollment.getSectionId(), enrollment.getRegistrationStatus());
        }
        try {
            System.out.print("Enrollment ID: ");
            int id = Integer.parseInt(scanner.nextLine());
            System.out.println("[1] ENROLLED");
            System.out.println("[2] DROPPED");
            System.out.println("[0] Cancel");
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
                return;
            }
            System.out.println(enrollmentController.updateRegistrationStatus(id, status)
                    ? "Registration status updated." : "Registration status could not be updated.");
        } catch (NumberFormatException ex) {
            System.out.println("Invalid enrollment ID.");
        }
    }

    private void enrollStudent() {
        try {
            System.out.print("Student ID: ");
            int studentId = Integer.parseInt(scanner.nextLine());
            AcademicPeriod period = periodController.getActivePeriod();
            if (period == null) {
                System.out.println("No active academic period is available.");
                return;
            }
            List<CourseSection> sections = sectionController.getSectionsByPeriod(period.getPeriodId());
            if (sections.isEmpty()) {
                System.out.println("No course sections are available for the active period.");
                return;
            }
            System.out.println("\n--- AVAILABLE COURSE SECTIONS ---");
            System.out.printf("%-10s %-12s %-28s %-12s %-16s %-12s %-12s%n",
                    "Section", "Course", "Title", "Schedule", "Time", "Capacity", "Enrolled");
            for (CourseSection section : sections) {
                Course course = courseController.getCourseById(section.getCourseId());
                if (course == null) continue;
                int enrolledCount = enrollmentController.getSectionEnrollments(section.getSectionId()).size();
                System.out.printf("%-10d %-12s %-28s %-12s %-16s %-12d %-12d%s%n",
                        section.getSectionId(), course.getCourseCode(), course.getCourseTitle(),
                        section.getScheduleDays(), section.getScheduleTime(), section.getCapacity(),
                        enrolledCount, enrolledCount >= section.getCapacity() ? " FULL" : "");
            }
            System.out.print("Course section ID: ");
            int sectionId = Integer.parseInt(scanner.nextLine());
            System.out.println(enrollmentController.enrollStudent(studentId, sectionId)
                    ? "Student enrolled with pending status." : "Student could not be enrolled.");
        } catch (NumberFormatException ex) {
            System.out.println("Invalid enrollment input.");
        }
    }

    private void managePeriods() {
        for (AcademicPeriod period : periodController.getAllPeriods()) {
            System.out.printf("Period %d | %s | %s to %s | Active: %s%n", period.getPeriodId(), period.getTermName(),
                    period.getStartDate(), period.getEndDate(), period.isActive());
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
        try {
            System.out.print("Student ID: ");
            int studentId = Integer.parseInt(scanner.nextLine());
            Student student = studentController.getStudentById(studentId);
            if (student == null) {
                System.out.println("Student not found.");
                return;
            }
            System.out.println("\n===== OFFICIAL TRANSCRIPT =====");
            System.out.println(student.getFirstName() + " " + student.getLastName() + " (Student " + studentId + ")");
            for (Enrollment enrollment : enrollmentController.getStudentEnrollments(studentId)) {
                System.out.printf("Section %d | Status %s | Grade %s | GPA %s%n", enrollment.getSectionId(),
                        enrollment.getRegistrationStatus(), value(enrollment.getFinalGrade()),
                        enrollment.getGpaPoints() == null ? "N/A" : enrollment.getGpaPoints());
            }
            System.out.println("===============================");
        } catch (NumberFormatException ex) {
            System.out.println("Invalid student ID.");
        }
    }

    private void processTranscript() {
        List<TranscriptRequest> requests = transcriptController.getAllRequests();
        if (requests.isEmpty()) {
            System.out.println("No transcript requests found.");
            return;
        }
        for (TranscriptRequest request : requests) {
            System.out.printf("Request %d | Student %d | %s | %s%n", request.getRequestId(), request.getStudentId(),
                    request.getStatus(), request.getRequestDate());
        }
        try {
            System.out.print("Request ID: ");
            int requestId = Integer.parseInt(scanner.nextLine());
            System.out.print("New status (PROCESSING/COMPLETED/REJECTED): ");
            String status = scanner.nextLine();
            System.out.println(transcriptController.updateRequestStatus(requestId, status, user.getUserId())
                    ? "Transcript request updated." : "Transcript request could not be updated.");
        } catch (NumberFormatException ex) {
            System.out.println("Invalid request ID.");
        }
    }

    private String value(String value) {
        return value == null || value.trim().isEmpty() ? "N/A" : value;
    }
}
