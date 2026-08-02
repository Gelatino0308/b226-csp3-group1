package com.joysistvi.sigsys.repository;

import com.joysistvi.sigsys.dao.CourseDao;
import com.joysistvi.sigsys.model.Course;
import java.util.List;

public class CourseRepositoryImpl implements CourseRepository {
    private final CourseDao courseDao = new CourseDao();

    @Override
    public boolean createCourse(Course course) {
        return courseDao.createCourse(course);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseDao.getAllCourses();
    }
}