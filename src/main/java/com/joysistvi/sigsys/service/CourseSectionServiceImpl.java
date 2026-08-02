package com.joysistvi.sigsys.service;

import com.joysistvi.sigsys.repository.CourseSectionRepository;
import com.joysistvi.sigsys.repository.CourseSectionRepositoryImpl;
import com.joysistvi.sigsys.model.CourseSection;
import java.util.List;

public class CourseSectionServiceImpl implements CourseSectionService {
    private final CourseSectionRepository sectionRepository = new CourseSectionRepositoryImpl();

    @Override
    public boolean createSection(CourseSection section) {
        if (section == null || section.getCourseId() <= 0 || section.getFacultyId() <= 0
                || section.getPeriodId() <= 0 || section.getCapacity() <= 0) return false;
        return sectionRepository.createSection(section);
    }

    @Override
    public List<CourseSection> getSectionsByPeriod(int periodId) {
        return periodId > 0 ? sectionRepository.getSectionsByPeriodId(periodId) : java.util.Collections.emptyList();
    }

    @Override public CourseSection getSectionById(int sectionId) { return sectionId > 0 ? sectionRepository.getSectionById(sectionId) : null; }
    @Override public List<CourseSection> getSectionsByFaculty(int facultyId) { return facultyId > 0 ? sectionRepository.getSectionsByFacultyId(facultyId) : java.util.Collections.emptyList(); }
    @Override public List<CourseSection> getSectionsByCourse(int courseId) { return courseId > 0 ? sectionRepository.getSectionsByCourseId(courseId) : java.util.Collections.emptyList(); }
    @Override public boolean deleteSection(int sectionId) { return sectionId > 0 && sectionRepository.deleteSection(sectionId); }
}
