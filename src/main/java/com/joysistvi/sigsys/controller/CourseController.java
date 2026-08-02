package com.joysistvi.sigsys.controller;
import com.joysistvi.sigsys.model.Course;
import com.joysistvi.sigsys.service.CourseService;
import com.joysistvi.sigsys.service.CourseServiceImpl;
import java.util.List;
public class CourseController {
    private final CourseService courseService = new CourseServiceImpl();
    public boolean addCourse(Course course) { return courseService.addCourse(course); }
    public List<Course> getAllCourses() { return courseService.getAllCourses(); }
    public Course getCourseById(int courseId) { return courseService.getCourseById(courseId); }
    public Course getCourseByCode(String courseCode) { return courseService.getCourseByCode(courseCode); }
    public boolean deleteCourse(int courseId) { return courseService.deleteCourse(courseId); }
}
