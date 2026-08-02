package com.joysistvi.sigsys.service;

import com.joysistvi.sigsys.repository.FacultyRepository;
import com.joysistvi.sigsys.repository.FacultyRepositoryImpl;
import com.joysistvi.sigsys.model.Faculty;
import java.util.List;

public class FacultyServiceImpl implements FacultyService {
    private final FacultyRepository facultyRepository = new FacultyRepositoryImpl();

    @Override
    public boolean registerFaculty(Faculty faculty) {
        if (faculty == null || faculty.getUserId() <= 0 || faculty.getFirstName() == null
                || faculty.getLastName() == null || faculty.getDepartment() == null
                || faculty.getFirstName().trim().isEmpty() || faculty.getLastName().trim().isEmpty()
                || faculty.getDepartment().trim().isEmpty()) return false;
        return facultyRepository.createFaculty(faculty);
    }

    @Override
    public Faculty getFacultyById(int facultyId) {
        return facultyId > 0 ? facultyRepository.getFacultyById(facultyId) : null;
    }

    @Override
    public List<Faculty> getAllFaculty() {
        return facultyRepository.getAllFaculty();
    }

    @Override public Faculty getFacultyByUserId(int userId) { return userId > 0 ? facultyRepository.getFacultyByUserId(userId) : null; }
}
