package com.joysistvi.sigsys.controller;

import com.joysistvi.sigsys.dao.CourseSectionDao;
import com.joysistvi.sigsys.model.CourseSection;
import java.util.List;

public class CourseSectionController {
    private final CourseSectionDao sectionDao;

    public CourseSectionController() {
        this.sectionDao = new CourseSectionDao();
    }

    public boolean createSection(CourseSection section) {
        if (section == null || section.getCourseId() <= 0 || section.getFacultyId() <= 0
                || section.getPeriodId() <= 0 || section.getCapacity() <= 0) {
            System.err.println("Error: Capacity must be greater than 0.");
            return false;
        }
        return sectionDao.createSection(section);
    }

    public List<CourseSection> getSectionsByPeriod(int periodId) {
        return sectionDao.getSectionsByPeriodId(periodId);
    }

    public CourseSection getSectionById(int sectionId) {
        return sectionId > 0 ? sectionDao.getSectionById(sectionId) : null;
    }

    public List<CourseSection> getSectionsByFaculty(int facultyId) {
        return facultyId > 0 ? sectionDao.getSectionsByFacultyId(facultyId) : java.util.Collections.emptyList();
    }

    public List<CourseSection> getSectionsByCourse(int courseId) {
        return courseId > 0 ? sectionDao.getSectionsByCourseId(courseId) : java.util.Collections.emptyList();
    }

    public boolean deleteSection(int sectionId) {
        return sectionId > 0 && sectionDao.deleteSection(sectionId);
    }
}
