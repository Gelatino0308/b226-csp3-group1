package com.joysistvi.sigsys.service;

import com.joysistvi.sigsys.dao.FacultyDao;
import com.joysistvi.sigsys.model.Faculty;
import java.util.List;

public class FacultyServiceImpl implements FacultyService {
    private final FacultyDao facultyDao = new FacultyDao();

    @Override
    public boolean registerFaculty(Faculty faculty) {
        if (faculty == null || faculty.getUserId() <= 0 || faculty.getFirstName() == null
                || faculty.getLastName() == null || faculty.getDepartment() == null
                || faculty.getFirstName().trim().isEmpty() || faculty.getLastName().trim().isEmpty()
                || faculty.getDepartment().trim().isEmpty()) return false;
        return facultyDao.createFaculty(faculty);
    }

    @Override
    public Faculty getFacultyById(int facultyId) {
        return facultyId > 0 ? facultyDao.getFacultyById(facultyId) : null;
    }

    @Override
    public List<Faculty> getAllFaculty() {
        return facultyDao.getAllFaculty();
    }
}
