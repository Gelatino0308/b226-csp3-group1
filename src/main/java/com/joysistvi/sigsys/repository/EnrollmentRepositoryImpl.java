package com.joysistvi.sigsys.repository;

import com.joysistvi.sigsys.dao.EnrollmentDao;
import com.joysistvi.sigsys.model.Enrollment;
import java.util.List;

public class EnrollmentRepositoryImpl implements EnrollmentRepository {
    private final EnrollmentDao enrollmentDao = new EnrollmentDao();

    @Override
    public boolean createEnrollment(int studentId, int sectionId) {
        return enrollmentDao.createEnrollment(studentId, sectionId);
    }

    @Override
    public boolean isAlreadyEnrolled(int studentId, int sectionId) {
        return enrollmentDao.isAlreadyEnrolled(studentId, sectionId);
    }

    @Override
    public List<Enrollment> getEnrollmentsByStudentId(int studentId) {
        return enrollmentDao.getEnrollmentsByStudentId(studentId);
    }

    @Override
    public boolean updateGrade(int enrollmentId, String finalGrade, double gpaPoints) {
        return enrollmentDao.updateGrade(enrollmentId, finalGrade, gpaPoints);
    }
}