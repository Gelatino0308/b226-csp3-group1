package com.joysistvi.sigsys.service;

import com.joysistvi.sigsys.model.Course;
import java.util.List;

public interface CourseService {
    boolean addCourse(Course course);
    List<Course> getAllCourses();
}