package com.joysistvi.sigsys.service;

import com.joysistvi.sigsys.dao.CourseSectionDao;
import com.joysistvi.sigsys.model.CourseSection;
import java.util.List;

public class CourseSectionServiceImpl implements CourseSectionService {
    private final CourseSectionDao sectionDao = new CourseSectionDao();

    @Override
    public boolean createSection(CourseSection section) {
        if (section == null || section.getCourseId() <= 0 || section.getFacultyId() <= 0
                || section.getPeriodId() <= 0 || section.getCapacity() <= 0) return false;
        return sectionDao.createSection(section);
    }

    @Override
    public List<CourseSection> getSectionsByPeriod(int periodId) {
        return periodId > 0 ? sectionDao.getSectionsByPeriodId(periodId) : java.util.Collections.emptyList();
    }
}
