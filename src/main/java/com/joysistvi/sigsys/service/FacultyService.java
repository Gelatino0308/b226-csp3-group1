package com.joysistvi.sigsys.service;

import com.joysistvi.sigsys.model.Faculty;
import java.util.List;

public interface FacultyService {
    boolean registerFaculty(Faculty faculty);
    Faculty getFacultyById(int facultyId);
    List<Faculty> getAllFaculty();
}