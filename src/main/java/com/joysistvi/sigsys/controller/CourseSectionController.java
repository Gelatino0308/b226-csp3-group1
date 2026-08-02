package com.joysistvi.sigsys.controller;

import com.joysistvi.sigsys.model.CourseSection;
import com.joysistvi.sigsys.service.CourseSectionService;
import com.joysistvi.sigsys.service.CourseSectionServiceImpl;
import java.util.List;

public class CourseSectionController {
    private final CourseSectionService sectionService = new CourseSectionServiceImpl();
    public boolean createSection(CourseSection section) { return sectionService.createSection(section); }
    public List<CourseSection> getSectionsByPeriod(int periodId) { return sectionService.getSectionsByPeriod(periodId); }
    public CourseSection getSectionById(int sectionId) { return sectionService.getSectionById(sectionId); }
    public List<CourseSection> getSectionsByFaculty(int facultyId) { return sectionService.getSectionsByFaculty(facultyId); }
    public List<CourseSection> getSectionsByCourse(int courseId) { return sectionService.getSectionsByCourse(courseId); }
    public boolean deleteSection(int sectionId) { return sectionService.deleteSection(sectionId); }
}
