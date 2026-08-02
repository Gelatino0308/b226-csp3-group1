package com.joysistvi.sigsys.cliview;

import com.joysistvi.sigsys.controller.AttendanceController;
import com.joysistvi.sigsys.controller.CourseSectionController;
import com.joysistvi.sigsys.controller.CourseController;
import com.joysistvi.sigsys.controller.EnrollmentController;
import com.joysistvi.sigsys.controller.FacultyController;
import com.joysistvi.sigsys.controller.GradeDetailsController;
import com.joysistvi.sigsys.controller.StudentController;
import com.joysistvi.sigsys.model.CourseSection;
import com.joysistvi.sigsys.model.Course;
import com.joysistvi.sigsys.model.Enrollment;
import com.joysistvi.sigsys.model.Faculty;
import com.joysistvi.sigsys.model.GradeDetail;
import com.joysistvi.sigsys.model.Student;
import com.joysistvi.sigsys.model.User;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class FacultyView {
    private final User user;
    private final Scanner scanner;
    private final FacultyController facultyController = new FacultyController();
    private final CourseSectionController sectionController = new CourseSectionController();
    private final CourseController courseController = new CourseController();
    private final EnrollmentController enrollmentController = new EnrollmentController();
    private final GradeDetailsController gradeController = new GradeDetailsController();
    private final AttendanceController attendanceController = new AttendanceController();
    private final StudentController studentController = new StudentController();

    public FacultyView(User user, Scanner scanner) {
        this.user = user;
        this.scanner = scanner;
    }

    public void showMenu() {
        boolean active = true;
        while (active) {
            System.out.println("\n=================================");
            System.out.println("        FACULTY DASHBOARD        ");
            System.out.println("=================================");
            System.out.println("[1] View Assigned Courses & Rosters");
            System.out.println("[2] View Students by Course and Section");
            System.out.println("[3] Input Attendance");
            System.out.println("[4] Input Marks");
            System.out.println("[5] Update Student Marks");
            System.out.println("[6] Calculate and Submit Final Grade");
            System.out.println("[7] Logout");
            System.out.print("Choose option: ");
            switch (scanner.nextLine()) {
                case "1" -> showAssignedCourses();
                case "2" -> showStudentsByCourseAndSection();
                case "3" -> inputAttendance();
                case "4", "5" -> inputMarks();
                case "6" -> calculateAndSubmitGrade();
                case "7" -> active = false;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void showStudentsByCourseAndSection() {
        try {
            System.out.println("[1] Search by Course");
            System.out.println("[2] Search by Section");
            System.out.print("Choose search type: ");
            int searchType = Integer.parseInt(scanner.nextLine());
            Course course;
            CourseSection section;

            if (searchType == 1) {
                System.out.print("Course Code: ");
                String courseCode = scanner.nextLine().trim();
                course = courseController.getCourseByCode(courseCode);
                if (course == null) {
                    System.out.println("No course found with code " + courseCode + ".");
                    return;
                }
                List<CourseSection> sections = sectionController.getSectionsByCourse(course.getCourseId());
                if (sections.isEmpty()) {
                    System.out.println("No sections are available for this course.");
                    return;
                }
                System.out.println("\n--- SECTIONS FOR " + course.getCourseCode() + " ---");
                for (CourseSection availableSection : sections) {
                    System.out.printf("Section ID: %d | Schedule: %s %s | Room: %s%n",
                            availableSection.getSectionId(), availableSection.getScheduleDays(),
                            availableSection.getScheduleTime(), availableSection.getRoom());
                }
                System.out.print("Enter Section ID: ");
                int sectionId = Integer.parseInt(scanner.nextLine());
                section = null;
                for (CourseSection availableSection : sections) {
                    if (availableSection.getSectionId() == sectionId) {
                        section = availableSection;
                        break;
                    }
                }
                if (section == null) {
                    System.out.println("That section does not belong to the selected course.");
                    return;
                }
            } else if (searchType == 2) {
                System.out.print("Section ID: ");
                int sectionId = Integer.parseInt(scanner.nextLine());
                section = sectionController.getSectionById(sectionId);
                if (section == null) {
                    System.out.println("No section found with ID " + sectionId + ".");
                    return;
                }
                course = courseController.getCourseById(section.getCourseId());
                if (course == null) {
                    System.out.println("No course is assigned to this section.");
                    return;
                }
                System.out.println("\n--- COURSES IN SECTION " + sectionId + " ---");
                System.out.println("[1] " + course.getCourseCode() + " - " + course.getCourseTitle());
                System.out.print("Choose course: ");
                if (Integer.parseInt(scanner.nextLine()) != 1) {
                    System.out.println("Invalid course choice.");
                    return;
                }
            } else {
                System.out.println("Invalid search type.");
                return;
            }
            List<Enrollment> enrollments = enrollmentController.getSectionEnrollments(section.getSectionId());
            System.out.println("\n--- STUDENTS IN " + course.getCourseCode() + " - "
                    + course.getCourseTitle() + " | SECTION " + section.getSectionId() + " ---");
            if (enrollments.isEmpty()) {
                System.out.println("No students are enrolled in this section.");
                return;
            }
            for (Enrollment enrollment : enrollments) {
                Student student = studentController.getStudentById(enrollment.getStudentId());
                if (student != null) {
                    System.out.printf("Student ID: %d | Name: %s %s | Enrollment: %d | Status: %s%n",
                            student.getStudentId(), student.getFirstName(), student.getLastName(),
                            enrollment.getEnrollmentId(), enrollment.getRegistrationStatus());
                }
            }
        } catch (NumberFormatException ex) {
            System.out.println("Invalid course or section ID.");
        }
    }

    private void showAssignedCourses() {
        Faculty faculty = facultyController.getFacultyByUserId(user.getUserId());
        if (faculty == null) {
            System.out.println("No faculty profile found.");
            return;
        }
        List<CourseSection> sections = sectionController.getSectionsByFaculty(faculty.getFacultyId());
        if (sections.isEmpty()) {
            System.out.println("No assigned course sections found.");
            return;
        }
        System.out.println("\nFaculty: " + faculty.getFirstName() + " " + faculty.getLastName());
        System.out.println("\n--- ASSIGNED COURSES AND ROSTERS ---");
        for (CourseSection section : sections) {
            Course course = courseController.getCourseById(section.getCourseId());
            if (course == null) continue;
            System.out.printf("\nSection ID: %d%n", section.getSectionId());
            System.out.printf("Course: %s%n", course.getCourseCode());
            System.out.printf("Title: %s%n", course.getCourseTitle());
            System.out.printf("Schedule: %s%n", section.getScheduleDays());
            System.out.printf("Time: %s%n", section.getScheduleTime());
            System.out.printf("Room: %s%n", section.getRoom());
            System.out.println("Roster:");
            System.out.printf("%-12s %-25s %-22s%n", "Student ID", "Student Name", "Enrolled At");
            for (Enrollment enrollment : enrollmentController.getSectionEnrollments(section.getSectionId())) {
                Student student = studentController.getStudentById(enrollment.getStudentId());
                if (student != null) {
                    System.out.printf("%-12d %-25s %-22s%n", student.getStudentId(),
                            student.getFirstName() + " " + student.getLastName(),
                            enrollment.getEnrolledAt() == null ? "--" : enrollment.getEnrolledAt());
                }
            }
        }
    }

    private void inputAttendance() {
        try {
            System.out.print("Student ID: ");
            int studentId = Integer.parseInt(scanner.nextLine());
            System.out.print("Course Code: ");
            String courseCode = scanner.nextLine().trim();
            Course course = courseController.getCourseByCode(courseCode);
            if (course == null) {
                System.out.println("No course found with code " + courseCode + ".");
                return;
            }
            Enrollment enrollment = enrollmentController.getEnrollmentByStudentAndCourse(studentId, course.getCourseId());
            Student student = studentController.getStudentById(studentId);
            CourseSection section = enrollment == null ? null : sectionController.getSectionById(enrollment.getSectionId());
            if (enrollment == null || student == null || section == null) {
                System.out.println("No enrollment found for this student and course.");
                return;
            }
            if (!isAssignedSection(section)) return;
            System.out.println("\n--- SELECTED ATTENDANCE RECORD ---");
            System.out.println("Student: " + student.getFirstName() + " " + student.getLastName()
                    + " (ID: " + student.getStudentId() + ")");
            System.out.println("Section: " + section.getSectionId());
            System.out.println("Subject: " + course.getCourseCode() + " - " + course.getCourseTitle());
            System.out.print("Confirm this student and subject? (Y/N): ");
            if (!scanner.nextLine().trim().equalsIgnoreCase("Y")) {
                System.out.println("Attendance input cancelled.");
                return;
            }
            System.out.print("Days attended: ");
            int attendedDays = Integer.parseInt(scanner.nextLine());
            System.out.print("Maximum attendance days: ");
            int maxAttendance = Integer.parseInt(scanner.nextLine());
            if (enrollmentController.updateAttendanceSummary(enrollment.getEnrollmentId(), attendedDays, maxAttendance)) {
                System.out.println("Attendance summary saved: " + attendedDays + "/" + maxAttendance + " days.");
                calculateAndSubmitGrade(enrollment.getEnrollmentId());
            } else {
                System.out.println("Attendance summary could not be saved. Check the enrollment and day counts.");
            }
        } catch (IllegalArgumentException ex) {
            System.out.println("Invalid attendance input.");
        }
    }

    private void inputMarks() {
        try {
            System.out.print("Course Section ID: ");
            int sectionId = Integer.parseInt(scanner.nextLine());
            CourseSection section = sectionController.getSectionById(sectionId);
            if (section == null) {
                System.out.println("No course section found with ID " + sectionId + ".");
                return;
            }
            if (!isAssignedSection(section)) return;

            Course course = courseController.getCourseById(section.getCourseId());
            if (course == null) {
                System.out.println("No subject is assigned to this section.");
                return;
            }

            System.out.println("\n--- AVAILABLE SUBJECTS FOR SECTION " + sectionId + " ---");
            System.out.println("[1] " + course.getCourseCode() + " - " + course.getCourseTitle());
            System.out.print("Choose subject: ");
            int subjectChoice = Integer.parseInt(scanner.nextLine());
            if (subjectChoice != 1) {
                System.out.println("Invalid subject choice.");
                return;
            }

            List<Enrollment> enrollments = enrollmentController.getSectionEnrollments(sectionId);
            if (enrollments.isEmpty()) {
                System.out.println("No students are enrolled in this subject and section.");
                return;
            }
            System.out.println("\n--- STUDENTS ENROLLED IN " + course.getCourseCode() + " ---");
            System.out.printf("%-12s %-25s %-15s %-15s %-15s%n",
                    "Student ID", "Student Name", "Prelim", "Midterm", "Finals");
            System.out.println("--------------------------------------------------------------------------");
            for (Enrollment item : enrollments) {
                Student enrolledStudent = studentController.getStudentById(item.getStudentId());
                if (enrolledStudent != null) {
                    Map<String, GradeDetail> studentMarks = new HashMap<>();
                    for (GradeDetail detail : gradeController.getGradesForEnrollment(item.getEnrollmentId())) {
                        String term = detail.getAssessmentName() == null
                                ? "" : detail.getAssessmentName().trim().toUpperCase();
                        if (term.matches("PRELIM|MIDTERM|FINALS")) studentMarks.put(term, detail);
                    }
                    System.out.printf("%-12d %-25s %-15s %-15s %-15s%n",
                            enrolledStudent.getStudentId(),
                            enrolledStudent.getFirstName() + " " + enrolledStudent.getLastName(),
                            formatTermMark(studentMarks.get("PRELIM")),
                            formatTermMark(studentMarks.get("MIDTERM")),
                            formatTermMark(studentMarks.get("FINALS")));
                }
            }
            System.out.print("Enter Student ID: ");
            int studentId = Integer.parseInt(scanner.nextLine());
            Enrollment enrollment = null;
            for (Enrollment item : enrollments) {
                if (item.getStudentId() == studentId) {
                    enrollment = item;
                    break;
                }
            }
            Student student = studentController.getStudentById(studentId);
            if (enrollment == null || student == null) {
                System.out.println("That student is not enrolled in the selected subject and section.");
                return;
            }

            System.out.println("\n--- SELECTED MARK RECORD ---");
            System.out.println("Student: " + student.getFirstName() + " " + student.getLastName()
                    + " (ID: " + student.getStudentId() + ")");
            System.out.println("Section: " + section.getSectionId());
            System.out.println("Subject: " + course.getCourseCode() + " - " + course.getCourseTitle());
            System.out.print("Confirm this student and subject? (Y/N): ");
            if (!scanner.nextLine().trim().equalsIgnoreCase("Y")) {
                System.out.println("Mark input cancelled.");
                return;
            }
            int enrollmentId = enrollment.getEnrollmentId();
            System.out.println("[1] PRELIM");
            System.out.println("[2] MIDTERM");
            System.out.println("[3] FINALS");
            System.out.print("Choose term: ");
            int termChoice = Integer.parseInt(scanner.nextLine());
            String term;
            if (termChoice == 1) term = "PRELIM";
            else if (termChoice == 2) term = "MIDTERM";
            else if (termChoice == 3) term = "FINALS";
            else {
                System.out.println("Invalid term choice.");
                return;
            }
            System.out.print("Score obtained: ");
            double score = Double.parseDouble(scanner.nextLine());
            System.out.print("Maximum score: ");
            double max = Double.parseDouble(scanner.nextLine());
            if (gradeController.saveTermMark(enrollmentId, term, score, max)) {
                System.out.println("Term mark saved. Each term contributes 33.33%.");
                calculateAndSubmitGrade(enrollmentId);
            } else {
                System.out.println("Term mark could not be saved.");
            }
        } catch (NumberFormatException ex) {
            System.out.println("Invalid marks input.");
        }
    }

    private String formatTermMark(GradeDetail detail) {
        if (detail == null || detail.getMaxScore() <= 0) return "--";
        double percentage = detail.getScoreObtained() / detail.getMaxScore() * 100.0;
        return String.format("%.2f%%", percentage);
    }

    private void calculateAndSubmitGrade() {
        try {
            System.out.print("Student ID: ");
            int studentId = Integer.parseInt(scanner.nextLine());
            System.out.print("Course Code: ");
            String courseCode = scanner.nextLine().trim();
            Course course = courseController.getCourseByCode(courseCode);
            if (course == null) {
                System.out.println("No course found with code " + courseCode + ".");
                return;
            }
            Enrollment enrollment = enrollmentController.getEnrollmentByStudentAndCourse(studentId, course.getCourseId());
            Student student = studentController.getStudentById(studentId);
            CourseSection section = enrollment == null ? null : sectionController.getSectionById(enrollment.getSectionId());
            if (enrollment == null || student == null || section == null) {
                System.out.println("No enrollment found for this student and course.");
                return;
            }
            if (!isAssignedSection(section)) return;
            System.out.println("\n--- SELECTED FINAL-GRADE RECORD ---");
            System.out.println("Student: " + student.getFirstName() + " " + student.getLastName()
                    + " (ID: " + student.getStudentId() + ")");
            System.out.println("Section: " + section.getSectionId());
            System.out.println("Subject: " + course.getCourseCode() + " - " + course.getCourseTitle());
            System.out.print("Confirm this student and subject? (Y/N): ");
            if (!scanner.nextLine().trim().equalsIgnoreCase("Y")) {
                System.out.println("Final-grade calculation cancelled.");
                return;
            }
            calculateAndSubmitGrade(enrollment.getEnrollmentId());
        } catch (NumberFormatException ex) {
            System.out.println("Invalid student ID.");
        }
    }

    private void calculateAndSubmitGrade(int enrollmentId) {
        Map<String, GradeDetail> marks = new HashMap<>();
        for (GradeDetail detail : gradeController.getGradesForEnrollment(enrollmentId)) {
            String term = detail.getAssessmentName() == null ? "" : detail.getAssessmentName().trim().toUpperCase();
            if (term.matches("PRELIM|MIDTERM|FINALS")) marks.put(term, detail);
        }
        if (!marks.keySet().containsAll(List.of("PRELIM", "MIDTERM", "FINALS"))) {
            System.out.println("Enter PRELIM, MIDTERM, and FINALS marks before calculating the final grade.");
            return;
        }
        double percentage = 0;
        for (String term : List.of("PRELIM", "MIDTERM", "FINALS")) {
            GradeDetail mark = marks.get(term);
            percentage += mark.getScoreObtained() / mark.getMaxScore() * 100.0 / 3.0;
        }
        Enrollment currentEnrollment = null;
        for (Enrollment candidate : enrollmentController.getAllEnrollments()) {
            if (candidate.getEnrollmentId() == enrollmentId) {
                currentEnrollment = candidate;
                break;
            }
        }
        if (currentEnrollment == null || currentEnrollment.getMaxAttendance() <= 0) {
            System.out.println("Enter the maximum attendance before calculating the final grade.");
            return;
        }
        percentage = percentage * 0.90
                + ((double) currentEnrollment.getAttendedDays() / currentEnrollment.getMaxAttendance() * 100.0) * 0.10;
        String letterGrade = letterGrade(percentage);
        double gpa = gpaEquivalent(percentage);
        System.out.printf("Calculated result: %.2f%% = %s (GPA %.2f)%n", percentage, letterGrade, gpa);
        if (!enrollmentController.submitGrade(enrollmentId, letterGrade, gpa)) {
            System.out.println("The calculated grade could not be saved.");
        }
    }

    private boolean isAssignedSection(CourseSection section) {
        Faculty faculty = facultyController.getFacultyByUserId(user.getUserId());
        if (faculty == null || faculty.getFacultyId() != section.getFacultyId()) {
            System.out.println("You are not assigned to this course section.");
            return false;
        }
        return true;
    }

    private String letterGrade(double percentage) {
        if (percentage >= 97) return "A+";
        if (percentage >= 93) return "A";
        if (percentage >= 90) return "A-";
        if (percentage >= 87) return "B+";
        if (percentage >= 83) return "B";
        if (percentage >= 80) return "B-";
        if (percentage >= 77) return "C+";
        if (percentage >= 73) return "C";
        if (percentage >= 70) return "C-";
        if (percentage >= 60) return "D";
        return "F";
    }

    private double gpaEquivalent(double percentage) {
        if (percentage >= 93) return 4.0;
        if (percentage >= 90) return 3.7;
        if (percentage >= 87) return 3.3;
        if (percentage >= 83) return 3.0;
        if (percentage >= 80) return 2.7;
        if (percentage >= 77) return 2.3;
        if (percentage >= 73) return 2.0;
        if (percentage >= 70) return 1.7;
        if (percentage >= 60) return 1.0;
        return 0.0;
    }

    private void calculateGpa() {
        try {
            System.out.print("Student ID: ");
            int studentId = Integer.parseInt(scanner.nextLine());
            List<Enrollment> enrollments = enrollmentController.getStudentEnrollments(studentId);
            double total = 0;
            int count = 0;
            for (Enrollment enrollment : enrollments) {
                if (enrollment.getGpaPoints() != null) {
                    total += enrollment.getGpaPoints();
                    count++;
                }
            }
            double gpa = count == 0 ? 0 : total / count;
            Student student = studentController.getStudentById(studentId);
            if (student != null) {
                student.setCumulativeGpa(gpa);
                studentController.updateStudentProfile(student);
            }
            System.out.printf("Calculated GPA for student %d: %.2f%n", studentId, gpa);
        } catch (NumberFormatException ex) {
            System.out.println("Invalid student ID.");
        }
    }
}
