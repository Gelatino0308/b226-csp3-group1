package com.joysistvi.sigsys.repository;

import com.joysistvi.sigsys.dao.CourseSectionDao;
import com.joysistvi.sigsys.model.CourseSection;
import java.util.List;

public class CourseSectionRepositoryImpl implements CourseSectionRepository {
    private final CourseSectionDao courseSectionDao = new CourseSectionDao();

    @Override
    public boolean createSection(CourseSection section) {
        return courseSectionDao.createSection(section);
    }

    @Override
    public List<CourseSection> getSectionsByPeriodId(int periodId) {
        return courseSectionDao.getSectionsByPeriodId(periodId);
    }
}