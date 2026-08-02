package com.joysistvi.sigsys.service;

import com.joysistvi.sigsys.model.CourseSection;
import java.util.List;

public interface CourseSectionService {
    boolean createSection(CourseSection section);
    List<CourseSection> getSectionsByPeriod(int periodId);
}