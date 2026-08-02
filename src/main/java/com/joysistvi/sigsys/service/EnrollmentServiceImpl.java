package com.joysistvi.sigsys.service;

import com.joysistvi.sigsys.dao.EnrollmentDao;
import com.joysistvi.sigsys.model.Enrollment;
import java.util.List;

public class EnrollmentServiceImpl implements EnrollmentService {
    private final EnrollmentDao enrollmentDao = new EnrollmentDao();

    @Override
    public boolean enrollStudent(int studentId, int sectionId) {
        if (studentId <= 0 || sectionId <= 0) return false;
        if (enrollmentDao.isAlreadyEnrolled(studentId, sectionId)) {
            System.err.println("Student is already enrolled in this section.");
            return false;
        }
        return enrollmentDao.createEnrollment(studentId, sectionId);
    }

    @Override
    public List<Enrollment> getStudentEnrollments(int studentId) {
        return studentId > 0 ? enrollmentDao.getEnrollmentsByStudentId(studentId) : java.util.Collections.emptyList();
    }

    @Override
    public boolean submitGrade(int enrollmentId, String finalGrade, double gpaPoints) {
        if (enrollmentId <= 0 || finalGrade == null || finalGrade.trim().isEmpty()
                || Double.isNaN(gpaPoints) || Double.isInfinite(gpaPoints)
                || gpaPoints < 0.0 || gpaPoints > 4.0) return false;
        finalGrade = finalGrade.trim();
        return enrollmentDao.updateGrade(enrollmentId, finalGrade, gpaPoints);
    }
}
