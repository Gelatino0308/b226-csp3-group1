package com.joysistvi.sigsys.cliview;

import com.joysistvi.sigsys.controller.AcademicPeriodController;
import com.joysistvi.sigsys.controller.CourseSectionController;
import com.joysistvi.sigsys.controller.CourseController;
import com.joysistvi.sigsys.controller.EnrollmentController;
import com.joysistvi.sigsys.controller.GradeDetailsController;
import com.joysistvi.sigsys.controller.StudentController;
import com.joysistvi.sigsys.controller.TranscriptRequestController;
import com.joysistvi.sigsys.controller.OverloadRequestController;
import com.joysistvi.sigsys.model.AcademicPeriod;
import com.joysistvi.sigsys.model.CourseSection;
import com.joysistvi.sigsys.model.Course;
import com.joysistvi.sigsys.model.Enrollment;
import com.joysistvi.sigsys.model.GradeDetail;
import com.joysistvi.sigsys.model.Student;
import com.joysistvi.sigsys.model.User;
import com.joysistvi.sigsys.util.ConsoleUIUtil;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class StudentView {
    private final User user;
    private final Scanner scanner;
    private final StudentController studentController = new StudentController();
    private final EnrollmentController enrollmentController = new EnrollmentController();
    private final CourseSectionController sectionController = new CourseSectionController();
    private final CourseController courseController = new CourseController();
    private final AcademicPeriodController periodController = new AcademicPeriodController();
    private final TranscriptRequestController transcriptController = new TranscriptRequestController();
    private final OverloadRequestController overloadController = new OverloadRequestController();
    private final GradeDetailsController gradeController = new GradeDetailsController();

    public StudentView(User user, Scanner scanner) {
        this.user = user;
        this.scanner = scanner;
    }

    public void showMenu() {
        Student student = studentController.getStudentByUserId(user.getUserId());
        if (student == null) {
            System.out.println("Error: No student profile found for this user account.");
            return;
        }
        boolean active = true;
        while (active) {
            ConsoleUIUtil.clearAndPrintHeader("STUDENT DASHBOARD");
            ConsoleUIUtil.printCenteredMenuOption(1, "View Personal Information");
            ConsoleUIUtil.printCenteredMenuOption(2, "View Enrolled Courses");
            ConsoleUIUtil.printCenteredMenuOption(3, "View Course Schedule");
            ConsoleUIUtil.printCenteredMenuOption(4, "View Final Grades & GPA");
            ConsoleUIUtil.printCenteredMenuOption(5, "Request Overload Units");
            ConsoleUIUtil.printCenteredMenuOption(6, "View Official Transcript");
            ConsoleUIUtil.printCenteredMenuOption(7, "Request Academic Transcript");
            ConsoleUIUtil.printCenteredMenuOption(8, "Logout");
            System.out.print("Choose option: ");
            switch (scanner.nextLine()) {
                case "1" -> showPersonalInformation(student);
                case "2" -> showEnrolledCourses(student.getStudentId());
                case "3" -> showSchedule(student.getStudentId());
                case "4" -> showGrades(student);
                case "5" -> requestOverload(student.getStudentId());
                case "6" -> viewOfficialTranscript(student);
                case "7" -> requestTranscript(student.getStudentId());
                case "8" -> active = false;
                default -> ConsoleUIUtil.printError("Invalid option.");
            }
        }
    }

    private void showEnrolledCourses(int studentId) {
        List<Enrollment> enrollments = enrollmentController.getStudentEnrollments(studentId);
        ConsoleUIUtil.clearAndPrintHeader("MY ENROLLED COURSES");
        if (enrollments.isEmpty()) {
            System.out.println("You are not enrolled in any courses.");
            ConsoleUIUtil.promptEnterToContinue(scanner);
            return;
        }
        int[] widths = {12, 20, 8, 12, 10, 8, 12, 8};
        ConsoleUIUtil.printTableHeader(widths, "Code", "Title", "Credits", "Days", "Time", "Room", "Status", "Grade");
        for (Enrollment enrollment : enrollments) {
            CourseSection section = sectionController.getSectionById(enrollment.getSectionId());
            Course course = section == null ? null : courseController.getCourseById(section.getCourseId());
            if (course == null || section == null) {
                ConsoleUIUtil.printTableRow(widths, String.valueOf(enrollment.getEnrollmentId()),
                        "Unavailable", "N/A", "N/A", "N/A", "N/A", enrollment.getRegistrationStatus(), "N/A");
                continue;
            }
            ConsoleUIUtil.printTableRow(widths, course.getCourseCode(), course.getCourseTitle(),
                    String.valueOf(course.getCredits()), section.getScheduleDays(), section.getScheduleTime(),
                    value(section.getRoom()), enrollment.getRegistrationStatus(), value(enrollment.getFinalGrade()));
        }
        ConsoleUIUtil.printTableFooter(widths);
        ConsoleUIUtil.promptEnterToContinue(scanner);
    }

    private void showPersonalInformation(Student student) {
        ConsoleUIUtil.clearAndPrintHeader("PERSONAL INFORMATION");
        System.out.println("Student ID: " + student.getStudentId());
        System.out.println("Name: " + student.getFirstName() + " " + student.getLastName());
        System.out.println("Date of Birth: " + value(student.getDob()));
        System.out.println("Phone: " + value(student.getPhone()));
        System.out.println("Address: " + value(student.getAddress()));
        ConsoleUIUtil.promptEnterToContinue(scanner);
    }

    private void registerForCourse(int studentId) {
        ConsoleUIUtil.printBoxedSectionHeader("REGISTER FOR COURSE");
        AcademicPeriod period = periodController.getActivePeriod();
        if (period == null) {
            System.out.println("No active academic period is available.");
            ConsoleUIUtil.promptEnterToContinue(scanner);
            return;
        }
        List<CourseSection> sections = sectionController.getSectionsByPeriod(period.getPeriodId());
        if (sections.isEmpty()) {
            System.out.println("No course sections are available.");
            ConsoleUIUtil.promptEnterToContinue(scanner);
            return;
        }
        printSectionsTable(sections);
        try {
            System.out.print("Enter Section ID to register: ");
            int sectionId = Integer.parseInt(scanner.nextLine());
            System.out.println(enrollmentController.enrollStudent(studentId, sectionId)
                    ? "Registration submitted for approval." : "Registration failed.");
            ConsoleUIUtil.promptEnterToContinue(scanner);
        } catch (NumberFormatException ex) {
            System.out.println("Invalid section ID.");
            ConsoleUIUtil.promptEnterToContinue(scanner);
        }
    }

    private void requestOverload(int studentId) {
        ConsoleUIUtil.printBoxedSectionHeader("REQUEST OVERLOAD UNITS");
        try {
            System.out.print("Enter requested maximum units: ");
            int requestedUnits = Integer.parseInt(scanner.nextLine());
            String configuredLimit = new com.joysistvi.sigsys.controller.SystemConfigController()
                    .getConfig("MAX_CREDITS_PER_TERM");
            int maximumUnits = configuredLimit == null ? 24 : Integer.parseInt(configuredLimit);
            if (requestedUnits <= maximumUnits) {
                System.out.println("Requested units must be greater than the current maximum of " + maximumUnits + ".");
                ConsoleUIUtil.promptEnterToContinue(scanner);
                return;
            }
            System.out.println(overloadController.request(studentId, requestedUnits)
                    ? "Overload request submitted for Admin approval."
                    : "A pending overload request already exists or the request could not be submitted.");

        } catch (NumberFormatException exception) {
            System.out.println("Invalid units value.");
        }
        ConsoleUIUtil.promptEnterToContinue(scanner);
    }

    private void showSchedule(int studentId) {
        ConsoleUIUtil.clearAndPrintHeader("COURSE SCHEDULE");
        AcademicPeriod period = periodController.getActivePeriod();
        if (period == null) {
            System.out.println("No active academic period is available.");
            ConsoleUIUtil.promptEnterToContinue(scanner);
            return;
        }
        List<CourseSection> sections = sectionController.getSectionsByPeriod(period.getPeriodId());
        List<Enrollment> enrollments = enrollmentController.getStudentEnrollments(studentId);
        java.util.Set<Integer> enrolledSectionIds = new java.util.HashSet<>();
        for (Enrollment enrollment : enrollments) {
            if (!"DROPPED".equals(enrollment.getRegistrationStatus())) {
                enrolledSectionIds.add(enrollment.getSectionId());
            }
        }
        sections.removeIf(section -> !enrolledSectionIds.contains(section.getSectionId()));
        System.out.println("Term: " + period.getTermName());
        printSectionsTable(sections);
        ConsoleUIUtil.promptEnterToContinue(scanner);
    }

    private void showGrades(Student student) {
        List<Enrollment> enrollments = enrollmentController.getStudentEnrollments(student.getStudentId());
        double total = 0;
        int graded = 0;
        ConsoleUIUtil.clearAndPrintHeader("FINAL GRADES & GPA");
        int[] widths = {30, 12, 10, 10};
        ConsoleUIUtil.printTableHeader(widths, "Course", "Status", "Grade", "GPA");
        for (Enrollment enrollment : enrollments) {
            if (enrollment.getGpaPoints() != null) {
                total += enrollment.getGpaPoints();
                graded++;
            }
            CourseSection section = sectionController.getSectionById(enrollment.getSectionId());
            Course course = section == null ? null : courseController.getCourseById(section.getCourseId());
            String courseName = course == null ? "Course unavailable" : course.getCourseCode() + " - " + course.getCourseTitle();
            double finalPercentage = calculateFinalPercentage(enrollment);
            String result = finalPercentage < 0 ? "N/A" : finalPercentage < 50 ? "FAILED" : "PASSED";
            ConsoleUIUtil.printTableRow(widths, courseName, result, value(enrollment.getFinalGrade()),
                    enrollment.getGpaPoints() == null ? "N/A" : String.valueOf(enrollment.getGpaPoints()));
        }
        ConsoleUIUtil.printTableFooter(widths);
        double gpa = graded == 0 ? 0 : total / graded;
        System.out.printf("Calculated GPA: %.2f%n", gpa);
        ConsoleUIUtil.promptEnterToContinue(scanner);
    }

    private double calculateFinalPercentage(Enrollment enrollment) {
        if (enrollment.getMaxAttendance() <= 0) return -1;
        Map<String, GradeDetail> marks = new HashMap<>();
        for (GradeDetail detail : gradeController.getGradesForEnrollment(enrollment.getEnrollmentId())) {
            String term = detail.getAssessmentName() == null ? "" : detail.getAssessmentName().trim().toUpperCase();
            if (term.matches("PRELIM|MIDTERM|FINALS")) marks.put(term, detail);
        }
        if (!marks.keySet().containsAll(List.of("PRELIM", "MIDTERM", "FINALS"))) return -1;
        double termPercentage = 0;
        for (String term : List.of("PRELIM", "MIDTERM", "FINALS")) {
            GradeDetail mark = marks.get(term);
            termPercentage += mark.getScoreObtained() / mark.getMaxScore() * 100.0 / 3.0;
        }
        return termPercentage * 0.90
                + ((double) enrollment.getAttendedDays() / enrollment.getMaxAttendance() * 100.0) * 0.10;
    }

    private void requestTranscript(int studentId) {
        ConsoleUIUtil.clearAndPrintHeader("REQUEST ACADEMIC TRANSCRIPT");
        System.out.println(transcriptController.requestTranscript(studentId)
                ? "Academic transcript request submitted." : "Could not submit transcript request.");
        ConsoleUIUtil.promptEnterToContinue(scanner);
    }

    private void viewOfficialTranscript(Student student) {
        List<com.joysistvi.sigsys.model.TranscriptRequest> requests =
                transcriptController.getStudentRequests(student.getStudentId());
        boolean completed = false;
        for (com.joysistvi.sigsys.model.TranscriptRequest request : requests) {
            if ("COMPLETED".equalsIgnoreCase(request.getStatus())) {
                completed = true;
                break;
            }
        }
        if (!completed) {
            System.out.println("No official transcript is available yet. The Registrar must complete your request first.");
            ConsoleUIUtil.promptEnterToContinue(scanner);
            return;
        }

        ConsoleUIUtil.clearAndPrintHeader("OFFICIAL TRANSCRIPT RECORD");
        System.out.println(student.getFirstName() + " " + student.getLastName()
                + " (Student " + student.getStudentId() + ")");
        int[] widths = {12, 28, 12, 10, 10};
        ConsoleUIUtil.printTableHeader(widths, "Course", "Title", "Status", "Grade", "GPA");
        for (Enrollment enrollment : enrollmentController.getStudentEnrollments(student.getStudentId())) {
            CourseSection section = sectionController.getSectionById(enrollment.getSectionId());
            Course course = section == null ? null : courseController.getCourseById(section.getCourseId());
            if (course != null) {
                double finalPercentage = calculateFinalPercentage(enrollment);
                String result = finalPercentage < 0 ? "N/A" : finalPercentage < 50 ? "FAILED" : "PASSED";
                ConsoleUIUtil.printTableRow(widths, course.getCourseCode(), course.getCourseTitle(), result,
                        value(enrollment.getFinalGrade()),
                        enrollment.getGpaPoints() == null ? "N/A" : String.valueOf(enrollment.getGpaPoints()));
            }
        }
        ConsoleUIUtil.printTableFooter(widths);
        ConsoleUIUtil.promptEnterToContinue(scanner);
    }

    private void printSections(List<CourseSection> sections) {
        if (sections.isEmpty()) {
            System.out.println("No sections found.");
            return;
        }
        printSectionsTable(sections);
    }

    private void printSectionsTable(List<CourseSection> sections) {
        if (sections.isEmpty()) {
            System.out.println("No sections found.");
            return;
        }
        int[] widths = {10, 10, 10, 12, 10, 8, 10};
        ConsoleUIUtil.printTableHeader(widths, "Section", "Course", "Faculty", "Days", "Time", "Room", "Capacity");
        for (CourseSection section : sections) {
            ConsoleUIUtil.printTableRow(widths, String.valueOf(section.getSectionId()),
                    String.valueOf(section.getCourseId()), String.valueOf(section.getFacultyId()),
                    section.getScheduleDays(), section.getScheduleTime(), value(section.getRoom()),
                    String.valueOf(section.getCapacity()));
        }
        ConsoleUIUtil.printTableFooter(widths);
    }

    private String value(Object value) {
        return value == null || value.toString().trim().isEmpty() ? "N/A" : value.toString();
    }
}
