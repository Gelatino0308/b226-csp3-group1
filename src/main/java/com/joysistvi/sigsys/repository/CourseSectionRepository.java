package com.joysistvi.sigsys.repository;

import com.joysistvi.sigsys.model.CourseSection;
import java.util.List;

public interface CourseSectionRepository {
    boolean createSection(CourseSection section);
    List<CourseSection> getSectionsByPeriodId(int periodId);
}