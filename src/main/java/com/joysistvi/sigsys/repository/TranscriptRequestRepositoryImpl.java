package com.joysistvi.sigsys.repository;

import com.joysistvi.sigsys.dao.TranscriptRequestDao;
import com.joysistvi.sigsys.model.TranscriptRequest;
import java.util.List;

public class TranscriptRequestRepositoryImpl implements TranscriptRequestRepository {
    private final TranscriptRequestDao transcriptRequestDao = new TranscriptRequestDao();

    @Override
    public boolean createRequest(int studentId) {
        return transcriptRequestDao.createRequest(studentId);
    }

    @Override
    public boolean updateStatus(int requestId, String status, int processedByUserId) {
        return transcriptRequestDao.updateStatus(requestId, status, processedByUserId);
    }

    @Override
    public List<TranscriptRequest> getRequestsByStudentId(int studentId) {
        return transcriptRequestDao.getRequestsByStudentId(studentId);
    }
}