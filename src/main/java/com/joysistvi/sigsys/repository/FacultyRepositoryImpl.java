package com.joysistvi.sigsys.repository;

import com.joysistvi.sigsys.dao.FacultyDao;
import com.joysistvi.sigsys.model.Faculty;
import java.util.List;

public class FacultyRepositoryImpl implements FacultyRepository {
    private final FacultyDao facultyDao = new FacultyDao();

    @Override
    public boolean createFaculty(Faculty faculty) {
        return facultyDao.createFaculty(faculty);
    }

    @Override
    public Faculty getFacultyById(int facultyId) {
        return facultyDao.getFacultyById(facultyId);
    }

    @Override
    public List<Faculty> getAllFaculty() {
        return facultyDao.getAllFaculty();
    }
}