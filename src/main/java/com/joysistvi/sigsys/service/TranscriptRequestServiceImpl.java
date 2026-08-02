package com.joysistvi.sigsys.service;

import com.joysistvi.sigsys.repository.TranscriptRequestRepository;
import com.joysistvi.sigsys.repository.TranscriptRequestRepositoryImpl;
import com.joysistvi.sigsys.model.TranscriptRequest;
import java.util.List;

public class TranscriptRequestServiceImpl implements TranscriptRequestService {
    private final TranscriptRequestRepository transcriptRepository = new TranscriptRequestRepositoryImpl();

    @Override
    public boolean requestTranscript(int studentId) {
        return studentId > 0 && transcriptRepository.createRequest(studentId);
    }

    @Override
    public boolean updateStatus(int requestId, String status, int registrarUserId) {
        if (requestId <= 0 || registrarUserId <= 0 || status == null
                || !status.trim().matches("PENDING|PROCESSING|COMPLETED|REJECTED")) return false;
        return transcriptRepository.updateStatus(requestId, status.trim(), registrarUserId);
    }

    @Override
    public List<TranscriptRequest> getStudentRequests(int studentId) {
        return studentId > 0 ? transcriptRepository.getRequestsByStudentId(studentId) : java.util.Collections.emptyList();
    }

    @Override
    public List<TranscriptRequest> getAllRequests() {
        return transcriptRepository.getAllRequests();
    }
}
