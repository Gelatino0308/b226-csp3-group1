package com.joysistvi.sigsys.controller;

import com.joysistvi.sigsys.dao.OverloadRequestDao;
import com.joysistvi.sigsys.model.OverloadRequest;

import java.util.List;

public class OverloadRequestController {
    private final OverloadRequestDao requestDao = new OverloadRequestDao();

    public boolean request(int studentId, int requestedUnits) {
        if (studentId <= 0 || requestedUnits <= 0 || requestDao.hasPending(studentId)) return false;
        OverloadRequest request = new OverloadRequest();
        request.setStudentId(studentId);
        request.setRequestedUnits(requestedUnits);
        return requestDao.create(request);
    }

    public List<OverloadRequest> getPendingRequests() { return requestDao.getPending(); }

    public List<OverloadRequest> getStudentRequests(int studentId) { return requestDao.getByStudent(studentId); }

    public Integer getApprovedMaxUnits(int studentId) { return requestDao.getApprovedUnits(studentId); }

    public boolean review(int requestId, String status, int adminId) {
        if (requestId <= 0 || adminId <= 0 || status == null
                || !status.matches("APPROVED|DISAPPROVED")) return false;
        return requestDao.updateStatus(requestId, status, adminId);
    }
}
