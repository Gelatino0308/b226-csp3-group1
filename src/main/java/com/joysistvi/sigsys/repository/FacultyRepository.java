package com.joysistvi.sigsys.repository;

import com.joysistvi.sigsys.model.Faculty;
import java.util.List;

public interface FacultyRepository {
    boolean createFaculty(Faculty faculty);
    Faculty getFacultyById(int facultyId);
    Faculty getFacultyByUserId(int userId);
    List<Faculty> getAllFaculty();
}
