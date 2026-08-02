package com.joysistvi.sigsys.controller;

import com.joysistvi.sigsys.model.Faculty;
import com.joysistvi.sigsys.service.FacultyService;
import com.joysistvi.sigsys.service.FacultyServiceImpl;
import java.util.List;

public class FacultyController {
    private final FacultyService facultyService = new FacultyServiceImpl();
    public boolean registerFaculty(Faculty faculty) { return facultyService.registerFaculty(faculty); }
    public Faculty getFacultyById(int facultyId) { return facultyService.getFacultyById(facultyId); }
    public Faculty getFacultyByUserId(int userId) { return facultyService.getFacultyByUserId(userId); }
    public List<Faculty> getAllFaculty() { return facultyService.getAllFaculty(); }
}
