package com.joysistvi.sigsys.repository;

import com.joysistvi.sigsys.model.CourseSection;
import java.util.List;

public interface CourseSectionRepository {
    boolean createSection(CourseSection section);
    List<CourseSection> getSectionsByPeriodId(int periodId);
    CourseSection getSectionById(int sectionId);
    List<CourseSection> getSectionsByFacultyId(int facultyId);
    List<CourseSection> getSectionsByCourseId(int courseId);
    boolean deleteSection(int sectionId);
}
