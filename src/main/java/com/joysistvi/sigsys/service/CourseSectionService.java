package com.joysistvi.sigsys.service;

import com.joysistvi.sigsys.model.CourseSection;
import java.util.List;

public interface CourseSectionService {
    boolean createSection(CourseSection section);
    List<CourseSection> getSectionsByPeriod(int periodId);
    CourseSection getSectionById(int sectionId);
    List<CourseSection> getSectionsByFaculty(int facultyId);
    List<CourseSection> getSectionsByCourse(int courseId);
    boolean deleteSection(int sectionId);
}
