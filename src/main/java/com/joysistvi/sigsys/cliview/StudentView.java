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
            System.out.println("\n=================================");
            System.out.println("       STUDENT DASHBOARD         ");
            System.out.println("=================================");
            System.out.println("[1] View Personal Information");
            System.out.println("[2] View Enrolled Courses");
            System.out.println("[3] View Course Schedule");
            System.out.println("[4] View Final Grades & GPA");
            System.out.println("[5] Request Overload Units");
            System.out.println("[6] View Official Transcript");
            System.out.println("[7] Request Academic Transcript");
            System.out.println("[8] Logout");
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
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void showEnrolledCourses(int studentId) {
        List<Enrollment> enrollments = enrollmentController.getStudentEnrollments(studentId);
        System.out.println("\n--- MY ENROLLED COURSES ---");
        if (enrollments.isEmpty()) {
            System.out.println("You are not enrolled in any courses.");
            return;
        }
        for (Enrollment enrollment : enrollments) {
            CourseSection section = sectionController.getSectionById(enrollment.getSectionId());
            Course course = section == null ? null : courseController.getCourseById(section.getCourseId());
            if (course == null || section == null) {
                System.out.printf("Enrollment %d | Section %d | Course details unavailable | Status: %s%n",
                        enrollment.getEnrollmentId(), enrollment.getSectionId(), enrollment.getRegistrationStatus());
                continue;
            }
            System.out.printf("%s - %s | %d credits | %s %s | Room: %s | Status: %s | Grade: %s%n",
                    course.getCourseCode(), course.getCourseTitle(), course.getCredits(), section.getScheduleDays(),
                    section.getScheduleTime(), value(section.getRoom()), enrollment.getRegistrationStatus(),
                    value(enrollment.getFinalGrade()));
        }
    }

    private void showPersonalInformation(Student student) {
        System.out.println("\n--- PERSONAL INFORMATION ---");
        System.out.println("Student ID: " + student.getStudentId());
        System.out.println("Name: " + student.getFirstName() + " " + student.getLastName());
        System.out.println("Date of Birth: " + value(student.getDob()));
        System.out.println("Phone: " + value(student.getPhone()));
        System.out.println("Address: " + value(student.getAddress()));
    }

    private void registerForCourse(int studentId) {
        AcademicPeriod period = periodController.getActivePeriod();
        if (period == null) {
            System.out.println("No active academic period is available.");
            return;
        }
        List<CourseSection> sections = sectionController.getSectionsByPeriod(period.getPeriodId());
        if (sections.isEmpty()) {
            System.out.println("No course sections are available.");
            return;
        }
        printSections(sections);
        try {
            System.out.print("Enter Section ID to register: ");
            int sectionId = Integer.parseInt(scanner.nextLine());
            System.out.println(enrollmentController.enrollStudent(studentId, sectionId)
                    ? "Registration submitted for approval." : "Registration failed.");
        } catch (NumberFormatException ex) {
            System.out.println("Invalid section ID.");
        }
    }

    private void requestOverload(int studentId) {
        try {
            System.out.print("Enter requested maximum units: ");
            int requestedUnits = Integer.parseInt(scanner.nextLine());
            String configuredLimit = new com.joysistvi.sigsys.controller.SystemConfigController()
                    .getConfig("MAX_CREDITS_PER_TERM");
            int maximumUnits = configuredLimit == null ? 24 : Integer.parseInt(configuredLimit);
            if (requestedUnits <= maximumUnits) {
                System.out.println("Requested units must be greater than the current maximum of " + maximumUnits + ".");
                return;
            }
            System.out.println(overloadController.request(studentId, requestedUnits)
                    ? "Overload request submitted for Admin approval."
                    : "A pending overload request already exists or the request could not be submitted.");
        } catch (NumberFormatException exception) {
            System.out.println("Invalid units value.");
        }
    }

    private void showSchedule(int studentId) {
        AcademicPeriod period = periodController.getActivePeriod();
        if (period == null) {
            System.out.println("No active academic period is available.");
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
        System.out.println("\n--- COURSE SCHEDULE: " + period.getTermName() + " ---");
        printSections(sections);
    }

    private void showGrades(Student student) {
        List<Enrollment> enrollments = enrollmentController.getStudentEnrollments(student.getStudentId());
        double total = 0;
        int graded = 0;
        System.out.println("\n--- FINAL GRADES & GPA ---");
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
            System.out.printf("Course: %s | Status: %s | Grade: %s | GPA: %s%n",
                    courseName, result, value(enrollment.getFinalGrade()),
                    enrollment.getGpaPoints() == null ? "N/A" : enrollment.getGpaPoints());
        }
        double gpa = graded == 0 ? 0 : total / graded;
        System.out.printf("Calculated GPA: %.2f%n", gpa);
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
        System.out.println(transcriptController.requestTranscript(studentId)
                ? "Academic transcript request submitted." : "Could not submit transcript request.");
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
            return;
        }

        System.out.println("\n===== OFFICIAL TRANSCRIPT RECORD =====");
        System.out.println(student.getFirstName() + " " + student.getLastName()
                + " (Student " + student.getStudentId() + ")");
        System.out.printf("%-12s %-28s %-12s %-10s %-10s%n",
                "Course", "Title", "Status", "Grade", "GPA");
        for (Enrollment enrollment : enrollmentController.getStudentEnrollments(student.getStudentId())) {
            CourseSection section = sectionController.getSectionById(enrollment.getSectionId());
            Course course = section == null ? null : courseController.getCourseById(section.getCourseId());
            if (course != null) {
                double finalPercentage = calculateFinalPercentage(enrollment);
                String result = finalPercentage < 0 ? "N/A" : finalPercentage < 50 ? "FAILED" : "PASSED";
                System.out.printf("%-12s %-28s %-12s %-10s %-10s%n", course.getCourseCode(),
                        course.getCourseTitle(), result,
                        value(enrollment.getFinalGrade()),
                        enrollment.getGpaPoints() == null ? "N/A" : enrollment.getGpaPoints());
            }
        }
        System.out.println("======================================");
    }

    private void printSections(List<CourseSection> sections) {
        if (sections.isEmpty()) {
            System.out.println("No sections found.");
            return;
        }
        for (CourseSection section : sections) {
            System.out.printf("Section %d | Course %d | Faculty %d | Days: %s | Time: %s | Room: %s | Capacity: %d%n",
                    section.getSectionId(), section.getCourseId(), section.getFacultyId(), section.getScheduleDays(),
                    section.getScheduleTime(), value(section.getRoom()), section.getCapacity());
        }
    }

    private String value(Object value) {
        return value == null || value.toString().trim().isEmpty() ? "N/A" : value.toString();
    }
}
