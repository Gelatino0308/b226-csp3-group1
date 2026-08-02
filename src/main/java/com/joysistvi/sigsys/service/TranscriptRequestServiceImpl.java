package com.joysistvi.sigsys.service;

import com.joysistvi.sigsys.dao.TranscriptRequestDao;
import com.joysistvi.sigsys.model.TranscriptRequest;
import java.util.List;

public class TranscriptRequestServiceImpl implements TranscriptRequestService {
    private final TranscriptRequestDao transcriptDao = new TranscriptRequestDao();

    @Override
    public boolean requestTranscript(int studentId) {
        return studentId > 0 && transcriptDao.createRequest(studentId);
    }

    @Override
    public boolean updateStatus(int requestId, String status, int registrarUserId) {
        if (requestId <= 0 || registrarUserId <= 0 || status == null
                || !status.trim().matches("PENDING|PROCESSING|COMPLETED|REJECTED")) return false;
        return transcriptDao.updateStatus(requestId, status.trim(), registrarUserId);
    }

    @Override
    public List<TranscriptRequest> getStudentRequests(int studentId) {
        return studentId > 0 ? transcriptDao.getRequestsByStudentId(studentId) : java.util.Collections.emptyList();
    }

    @Override
    public List<TranscriptRequest> getAllRequests() {
        return transcriptDao.getAllRequests();
    }
}
