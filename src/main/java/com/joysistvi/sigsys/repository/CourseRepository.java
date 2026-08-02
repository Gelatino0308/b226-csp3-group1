package com.joysistvi.sigsys.repository;

import com.joysistvi.sigsys.model.Course;
import java.util.List;

public interface CourseRepository {
    boolean createCourse(Course course);
    List<Course> getAllCourses();
    Course getCourseById(int courseId);
    Course getCourseByCode(String courseCode);
    boolean deleteCourse(int courseId);
}
