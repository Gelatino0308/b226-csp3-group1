package com.joysistvi.sigsys.controller;

import com.joysistvi.sigsys.dao.CourseDao;
import com.joysistvi.sigsys.model.Course;

import java.util.List;

public class CourseController {
    private final CourseDao courseDao;

    public CourseController() {
        this.courseDao = new CourseDao();
    }

    public boolean addCourse(Course course) {
        if (course == null || course.getCourseCode() == null || course.getCourseCode().trim().isEmpty()) {
            System.err.println("Error: Course code is required.");
            return false;
        }
        if (course.getCredits() <= 0) {
            System.err.println("Error: Course credits must be greater than 0.");
            return false;
        }
        if (getCourseByCode(course.getCourseCode()) != null) {
            System.err.println("Error: A course with this code already exists.");
            return false;
        }
        return courseDao.createCourse(course);
    }

    public List<Course> getAllCourses() {
        return courseDao.getAllCourses();
    }

    public Course getCourseById(int courseId) {
        return courseId > 0 ? courseDao.getCourseById(courseId) : null;
    }

    public Course getCourseByCode(String courseCode) {
        return courseCode == null || courseCode.trim().isEmpty() ? null : courseDao.getCourseByCode(courseCode.trim());
    }

    public boolean deleteCourse(int courseId) {
        return courseId > 0 && courseDao.deleteCourse(courseId);
    }
}
