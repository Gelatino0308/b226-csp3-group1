package com.joysistvi.sigsys.cliview;

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
import com.joysistvi.sigsys.util.ConsoleUIUtil;

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
    private final StudentController studentController = new StudentController();

    public FacultyView(User user, Scanner scanner) {
        this.user = user;
        this.scanner = scanner;
    }

    public void showMenu() {
        boolean active = true;
        while (active) {
            ConsoleUIUtil.clearAndPrintHeader("FACULTY DASHBOARD");
            ConsoleUIUtil.printCenteredMenuOption(1, "View Assigned Courses & Rosters");
            ConsoleUIUtil.printCenteredMenuOption(2, "View Students by Course and Section");
            ConsoleUIUtil.printCenteredMenuOption(3, "Input Attendance");
            ConsoleUIUtil.printCenteredMenuOption(4, "Input Marks");
            ConsoleUIUtil.printCenteredMenuOption(5, "Update Student Marks");
            ConsoleUIUtil.printCenteredMenuOption(6, "Calculate and Submit Final Grade");
            ConsoleUIUtil.printCenteredMenuOption(7, "Logout");
            System.out.print("Choose option: ");
            switch (scanner.nextLine()) {
                case "1" -> showAssignedCourses();
                case "2" -> showStudentsByCourseAndSection();
                case "3" -> inputAttendance();
                case "4", "5" -> inputMarks();
                case "6" -> calculateAndSubmitGrade();
                case "7" -> active = false;
                default -> {
                    ConsoleUIUtil.printError("Invalid option.");
                    pause();
                }
            }
        }
    }

    private void showStudentsByCourseAndSection() {
        ConsoleUIUtil.clearAndPrintHeader("STUDENTS BY COURSE AND SECTION");
        try {
            ConsoleUIUtil.printCenteredMenuOption(1, "Search by Course");
            ConsoleUIUtil.printCenteredMenuOption(2, "Search by Section");
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
                    pause();
                    return;
                }
                List<CourseSection> sections = sectionController.getSectionsByCourse(course.getCourseId());
                if (sections.isEmpty()) {
                    System.out.println("No sections are available for this course.");
                    pause();
                    return;
                }
                ConsoleUIUtil.printBoxedSectionHeader("SECTIONS FOR " + course.getCourseCode());
                int[] widths = {12, 22, 18, 15};
                ConsoleUIUtil.printTableHeader(widths, "SECTION ID", "SCHEDULE DAYS", "SCHEDULE TIME", "ROOM");
                for (CourseSection availableSection : sections) {
                    ConsoleUIUtil.printTableRow(widths,
                            String.valueOf(availableSection.getSectionId()),
                            String.valueOf(availableSection.getScheduleDays()),
                            String.valueOf(availableSection.getScheduleTime()),
                            String.valueOf(availableSection.getRoom()));
                }
                ConsoleUIUtil.printTableFooter(widths);
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
                    pause();
                    return;
                }
            } else if (searchType == 2) {
                System.out.print("Section ID: ");
                int sectionId = Integer.parseInt(scanner.nextLine());
                section = sectionController.getSectionById(sectionId);
                if (section == null) {
                    System.out.println("No section found with ID " + sectionId + ".");
                    pause();
                    return;
                }
                course = courseController.getCourseById(section.getCourseId());
                if (course == null) {
                    System.out.println("No course is assigned to this section.");
                    pause();
                    return;
                }
                ConsoleUIUtil.printBoxedSectionHeader("COURSES IN SECTION " + sectionId);
                ConsoleUIUtil.printCenteredMenuOption(1, course.getCourseCode() + " - " + course.getCourseTitle());
                System.out.print("Choose course: ");
                if (Integer.parseInt(scanner.nextLine()) != 1) {
                    ConsoleUIUtil.printError("Invalid course choice.");
                    pause();
                    return;
                }
            } else {
                System.out.println("Invalid search type.");
                pause();
                return;
            }

            List<Enrollment> enrollments = enrollmentController.getSectionEnrollments(section.getSectionId());
            ConsoleUIUtil.printBoxedSectionHeader("STUDENTS IN " + course.getCourseCode() + " - "
                    + course.getCourseTitle() + " | SECTION " + section.getSectionId());
            if (enrollments.isEmpty()) {
                System.out.println("No students are enrolled in this section.");
                pause();
                return;
            }
            int[] widths = {12, 25, 16, 15};
            ConsoleUIUtil.printTableHeader(widths, "STUDENT ID", "STUDENT NAME", "ENROLLMENT ID", "STATUS");
            for (Enrollment enrollment : enrollments) {
                Student student = studentController.getStudentById(enrollment.getStudentId());
                if (student != null) {
                    ConsoleUIUtil.printTableRow(widths,
                            String.valueOf(student.getStudentId()),
                            student.getFirstName() + " " + student.getLastName(),
                            String.valueOf(enrollment.getEnrollmentId()),
                            String.valueOf(enrollment.getRegistrationStatus()));
                }
            }
            ConsoleUIUtil.printTableFooter(widths);
            pause();
        } catch (NumberFormatException ex) {
            System.out.println("Invalid course or section ID.");
            pause();
        }
    }

    private void showAssignedCourses() {
        ConsoleUIUtil.clearAndPrintHeader("ASSIGNED COURSES AND ROSTERS");
        Faculty faculty = facultyController.getFacultyByUserId(user.getUserId());
        if (faculty == null) {
            System.out.println("No faculty profile found.");
            pause();
            return;
        }
        List<CourseSection> sections = sectionController.getSectionsByFaculty(faculty.getFacultyId());
        if (sections.isEmpty()) {
            System.out.println("No assigned course sections found.");
            pause();
            return;
        }
        System.out.println("\nFaculty: " + faculty.getFirstName() + " " + faculty.getLastName());
        int[] sectionWidths = {12, 12, 30, 16, 16, 16};
        ConsoleUIUtil.printTableHeader(sectionWidths, "SECTION ID", "COURSE", "TITLE", "DAYS", "TIME", "ROOM");
        for (CourseSection section : sections) {
            Course course = courseController.getCourseById(section.getCourseId());
            if (course == null) continue;
            ConsoleUIUtil.printTableRow(sectionWidths,
                    String.valueOf(section.getSectionId()),
                    String.valueOf(course.getCourseCode()),
                    String.valueOf(course.getCourseTitle()),
                    String.valueOf(section.getScheduleDays()),
                    String.valueOf(section.getScheduleTime()),
                    String.valueOf(section.getRoom()));
        }
        ConsoleUIUtil.printTableFooter(sectionWidths);

        for (CourseSection section : sections) {
            Course course = courseController.getCourseById(section.getCourseId());
            if (course == null) continue;
            ConsoleUIUtil.printBoxedSectionHeader("ROSTER FOR SECTION " + section.getSectionId() + " - " + course.getCourseCode());
            int[] rosterWidths = {12, 25, 22};
            ConsoleUIUtil.printTableHeader(rosterWidths, "STUDENT ID", "STUDENT NAME", "ENROLLED AT");
            for (Enrollment enrollment : enrollmentController.getSectionEnrollments(section.getSectionId())) {
                Student student = studentController.getStudentById(enrollment.getStudentId());
                if (student != null) {
                    ConsoleUIUtil.printTableRow(rosterWidths,
                            String.valueOf(student.getStudentId()),
                            student.getFirstName() + " " + student.getLastName(),
                            String.valueOf(enrollment.getEnrolledAt() == null ? "--" : enrollment.getEnrolledAt()));
                }
            }
            ConsoleUIUtil.printTableFooter(rosterWidths);
        }
        pause();
    }

    private void inputAttendance() {
        ConsoleUIUtil.printBoxedSectionHeader("INPUT ATTENDANCE");
        try {
            System.out.print("Student ID: ");
            int studentId = Integer.parseInt(scanner.nextLine());
            System.out.print("Course Code: ");
            String courseCode = scanner.nextLine().trim();
            Course course = courseController.getCourseByCode(courseCode);
            if (course == null) {
                System.out.println("No course found with code " + courseCode + ".");
                pause();
                return;
            }
            Enrollment enrollment = enrollmentController.getEnrollmentByStudentAndCourse(studentId, course.getCourseId());
            Student student = studentController.getStudentById(studentId);
            CourseSection section = enrollment == null ? null : sectionController.getSectionById(enrollment.getSectionId());
            if (enrollment == null || student == null || section == null) {
                System.out.println("No enrollment found for this student and course.");
                pause();
                return;
            }
            if (isAssignedSection(section)) {
                ConsoleUIUtil.printBoxedSectionHeader("SELECTED ATTENDANCE RECORD");
                int[] widths = {12, 25, 12, 30};
                ConsoleUIUtil.printTableHeader(widths, "STUDENT ID", "STUDENT NAME", "SECTION", "SUBJECT");
                ConsoleUIUtil.printTableRow(widths,
                        String.valueOf(student.getStudentId()),
                        student.getFirstName() + " " + student.getLastName(),
                        String.valueOf(section.getSectionId()),
                        course.getCourseCode() + " - " + course.getCourseTitle());
                ConsoleUIUtil.printTableFooter(widths);
                System.out.print("Confirm this student and subject? (Y/N): ");
                if (!scanner.nextLine().trim().equalsIgnoreCase("Y")) {
                    System.out.println("Attendance input cancelled.");
                    pause();
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
                    pause();
                }
            }
        } catch (IllegalArgumentException ex) {
            System.out.println("Invalid attendance input.");
            pause();
        }
    }

    private void inputMarks() {
        ConsoleUIUtil.printBoxedSectionHeader("INPUT MARKS");
        try {
            System.out.print("Course Section ID: ");
            int sectionId = Integer.parseInt(scanner.nextLine());
            CourseSection section = sectionController.getSectionById(sectionId);
            if (section == null) {
                System.out.println("No course section found with ID " + sectionId + ".");
                pause();
                return;
            }
            if (isAssignedSection(section)) {
                Course course = courseController.getCourseById(section.getCourseId());
                if (course == null) {
                    System.out.println("No subject is assigned to this section.");
                    pause();
                    return;
                }

                ConsoleUIUtil.printBoxedSectionHeader("AVAILABLE SUBJECTS FOR SECTION " + sectionId);
                ConsoleUIUtil.printCenteredMenuOption(1, course.getCourseCode() + " - " + course.getCourseTitle());
                System.out.print("Choose subject: ");
                int subjectChoice = Integer.parseInt(scanner.nextLine());
                if (subjectChoice != 1) {
                    System.out.println("Invalid subject choice.");
                    pause();
                    return;
                }

                List<Enrollment> enrollments = enrollmentController.getSectionEnrollments(sectionId);
                if (enrollments.isEmpty()) {
                    System.out.println("No students are enrolled in this subject and section.");
                    pause();
                    return;
                }
                ConsoleUIUtil.printBoxedSectionHeader("STUDENTS ENROLLED IN " + course.getCourseCode());
                int[] widths = {12, 25, 15, 15, 15};
                ConsoleUIUtil.printTableHeader(widths, "STUDENT ID", "STUDENT NAME", "PRELIM", "MIDTERM", "FINALS");
                for (Enrollment item : enrollments) {
                    Student enrolledStudent = studentController.getStudentById(item.getStudentId());
                    if (enrolledStudent != null) {
                        Map<String, GradeDetail> studentMarks = new HashMap<>();
                        for (GradeDetail detail : gradeController.getGradesForEnrollment(item.getEnrollmentId())) {
                            String term = detail.getAssessmentName() == null
                                    ? "" : detail.getAssessmentName().trim().toUpperCase();
                            if (term.matches("PRELIM|MIDTERM|FINALS")) studentMarks.put(term, detail);
                        }
                        ConsoleUIUtil.printTableRow(widths,
                                String.valueOf(enrolledStudent.getStudentId()),
                                enrolledStudent.getFirstName() + " " + enrolledStudent.getLastName(),
                                formatTermMark(studentMarks.get("PRELIM")),
                                formatTermMark(studentMarks.get("MIDTERM")),
                                formatTermMark(studentMarks.get("FINALS")));
                    }
                }
                ConsoleUIUtil.printTableFooter(widths);
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
                    pause();
                    return;
                }

                ConsoleUIUtil.printBoxedSectionHeader("SELECTED MARK RECORD");
                int[] selectedWidths = {12, 25, 12, 30};
                ConsoleUIUtil.printTableHeader(selectedWidths, "STUDENT ID", "STUDENT NAME", "SECTION", "SUBJECT");
                ConsoleUIUtil.printTableRow(selectedWidths,
                        String.valueOf(student.getStudentId()),
                        student.getFirstName() + " " + student.getLastName(),
                        String.valueOf(section.getSectionId()),
                        course.getCourseCode() + " - " + course.getCourseTitle());
                ConsoleUIUtil.printTableFooter(selectedWidths);
                System.out.print("Confirm this student and subject? (Y/N): ");
                if (!scanner.nextLine().trim().equalsIgnoreCase("Y")) {
                    System.out.println("Mark input cancelled.");
                    pause();
                    return;
                }
                int enrollmentId = enrollment.getEnrollmentId();
                ConsoleUIUtil.printCenteredMenuOption(1, "PRELIM");
                ConsoleUIUtil.printCenteredMenuOption(2, "MIDTERM");
                ConsoleUIUtil.printCenteredMenuOption(3, "FINALS");
                System.out.print("Choose term: ");
                int termChoice = Integer.parseInt(scanner.nextLine());
                String term;
                if (termChoice == 1) term = "PRELIM";
                else if (termChoice == 2) term = "MIDTERM";
                else if (termChoice == 3) term = "FINALS";
                else {
                    System.out.println("Invalid term choice.");
                    pause();
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
                    pause();
                }
            }
        } catch (NumberFormatException ex) {
            System.out.println("Invalid marks input.");
            pause();
        }
    }

    private String formatTermMark(GradeDetail detail) {
        if (detail == null || detail.getMaxScore() <= 0) return "--";
        double percentage = detail.getScoreObtained() / detail.getMaxScore() * 100.0;
        return String.format("%.2f%%", percentage);
    }

    private void calculateAndSubmitGrade() {
        ConsoleUIUtil.printBoxedSectionHeader("CALCULATE AND SUBMIT FINAL GRADE");
        try {
            System.out.print("Student ID: ");
            int studentId = Integer.parseInt(scanner.nextLine());
            System.out.print("Course Code: ");
            String courseCode = scanner.nextLine().trim();
            Course course = courseController.getCourseByCode(courseCode);
            if (course == null) {
                System.out.println("No course found with code " + courseCode + ".");
                pause();
                return;
            }
            Enrollment enrollment = enrollmentController.getEnrollmentByStudentAndCourse(studentId, course.getCourseId());
            Student student = studentController.getStudentById(studentId);
            CourseSection section = enrollment == null ? null : sectionController.getSectionById(enrollment.getSectionId());
            if (enrollment == null || student == null || section == null) {
                System.out.println("No enrollment found for this student and course.");
                pause();
                return;
            }
            if (isAssignedSection(section)) {
                ConsoleUIUtil.printBoxedSectionHeader("SELECTED FINAL-GRADE RECORD");
                int[] widths = {12, 25, 12, 30};
                ConsoleUIUtil.printTableHeader(widths, "STUDENT ID", "STUDENT NAME", "SECTION", "SUBJECT");
                ConsoleUIUtil.printTableRow(widths,
                        String.valueOf(student.getStudentId()),
                        student.getFirstName() + " " + student.getLastName(),
                        String.valueOf(section.getSectionId()),
                        course.getCourseCode() + " - " + course.getCourseTitle());
                ConsoleUIUtil.printTableFooter(widths);
                System.out.print("Confirm this student and subject? (Y/N): ");
                if (!scanner.nextLine().trim().equalsIgnoreCase("Y")) {
                    System.out.println("Final-grade calculation cancelled.");
                    pause();
                    return;
                }
                calculateAndSubmitGrade(enrollment.getEnrollmentId());
            }
        } catch (NumberFormatException ex) {
            System.out.println("Invalid student ID.");
            pause();
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
            pause();
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
            pause();
            return;
        }
        percentage = percentage * 0.90
                + ((double) currentEnrollment.getAttendedDays() / currentEnrollment.getMaxAttendance() * 100.0) * 0.10;
        String letterGrade = letterGrade(percentage);
        double gpa = gpaEquivalent(percentage);
        System.out.printf("Calculated result: %.2f%% = %s (GPA %.2f)%n", percentage, letterGrade, gpa);
        if (!enrollmentController.submitGrade(enrollmentId, letterGrade, gpa)) {
            System.out.println("The calculated grade could not be saved.");
            pause();
            return;
        }
        pause();
    }

    private boolean isAssignedSection(CourseSection section) {
        Faculty faculty = facultyController.getFacultyByUserId(user.getUserId());
        if (faculty == null || faculty.getFacultyId() != section.getFacultyId()) {
            System.out.println("You are not assigned to this course section.");
            pause();
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

    private void pause() {
        ConsoleUIUtil.promptEnterToContinue(scanner);
    }
}

