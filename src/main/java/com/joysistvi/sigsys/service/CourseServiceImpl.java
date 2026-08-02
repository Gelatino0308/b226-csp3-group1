package com.joysistvi.sigsys.service;

import com.joysistvi.sigsys.dao.CourseDao;
import com.joysistvi.sigsys.model.Course;
import java.util.List;

public class CourseServiceImpl implements CourseService {
    private final CourseDao courseDao = new CourseDao();

    @Override
    public boolean addCourse(Course course) {
        if (course == null || course.getCourseCode() == null || course.getCourseCode().trim().isEmpty()
                || course.getCourseTitle() == null || course.getCourseTitle().trim().isEmpty()
                || course.getCredits() <= 0) return false;
        course.setCourseCode(course.getCourseCode().trim());
        course.setCourseTitle(course.getCourseTitle().trim());
        return courseDao.createCourse(course);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseDao.getAllCourses();
    }
}
