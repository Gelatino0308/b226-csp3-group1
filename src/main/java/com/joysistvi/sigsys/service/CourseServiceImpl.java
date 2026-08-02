package com.joysistvi.sigsys.service;

import com.joysistvi.sigsys.repository.CourseRepository;
import com.joysistvi.sigsys.repository.CourseRepositoryImpl;
import com.joysistvi.sigsys.model.Course;
import java.util.List;

public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository = new CourseRepositoryImpl();

    @Override
    public boolean addCourse(Course course) {
        if (course == null || course.getCourseCode() == null || course.getCourseCode().trim().isEmpty()
                || course.getCourseTitle() == null || course.getCourseTitle().trim().isEmpty()
                || course.getCredits() <= 0) return false;
        course.setCourseCode(course.getCourseCode().trim());
        course.setCourseTitle(course.getCourseTitle().trim());
        if (courseRepository.getCourseByCode(course.getCourseCode()) != null) return false;
        return courseRepository.createCourse(course);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.getAllCourses();
    }

    @Override public Course getCourseById(int courseId) { return courseId > 0 ? courseRepository.getCourseById(courseId) : null; }
    @Override public Course getCourseByCode(String courseCode) { return courseCode == null || courseCode.trim().isEmpty() ? null : courseRepository.getCourseByCode(courseCode.trim()); }
    @Override public boolean deleteCourse(int courseId) { return courseId > 0 && courseRepository.deleteCourse(courseId); }
}
