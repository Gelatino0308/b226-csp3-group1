package com.joysistvi.sigsys.controller;

import com.joysistvi.sigsys.dao.FacultyDao;
import com.joysistvi.sigsys.model.Faculty;

import java.util.List;

public class FacultyController {
    private final FacultyDao facultyDao;

    public FacultyController() {
        this.facultyDao = new FacultyDao();
    }

    public boolean registerFaculty(Faculty faculty) {
        if (faculty == null || faculty.getFirstName() == null || faculty.getLastName() == null
                || faculty.getFirstName().trim().isEmpty() || faculty.getLastName().trim().isEmpty()) {
            System.err.println("Error: Faculty name details are required.");
            return false;
        }
        if (faculty.getDepartment() == null || faculty.getDepartment().trim().isEmpty()) {
            System.err.println("Error: Faculty department is required.");
            return false;
        }
        return facultyDao.createFaculty(faculty);
    }

    public Faculty getFacultyById(int facultyId) {
        if (facultyId <= 0) {
            System.err.println("Error: Invalid Faculty ID.");
            return null;
        }
        return facultyDao.getFacultyById(facultyId);
    }

    public Faculty getFacultyByUserId(int userId) {
        return userId > 0 ? facultyDao.getFacultyByUserId(userId) : null;
    }

    public List<Faculty> getAllFaculty() {
        return facultyDao.getAllFaculty();
    }
}
