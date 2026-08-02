package com.joysistvi.sigsys.controller;

import com.joysistvi.sigsys.service.TranscriptRequestService;
import com.joysistvi.sigsys.service.TranscriptRequestServiceImpl;
import com.joysistvi.sigsys.model.TranscriptRequest;
import java.util.List;

public class TranscriptRequestController {
    private final TranscriptRequestService transcriptService;

    public TranscriptRequestController() {
        this.transcriptService = new TranscriptRequestServiceImpl();
    }

    public boolean updateRequestStatus(int reqId, String status, int userId) {
        if (reqId <= 0 || userId <= 0 || status == null) return false;
        return transcriptService.updateStatus(reqId, status.trim().toUpperCase(), userId);
    }

    public boolean requestTranscript(int studentId) {
        return studentId > 0 && transcriptService.requestTranscript(studentId);
    }

    public List<TranscriptRequest> getAllRequests() {
        return transcriptService.getAllRequests();
    }

    public List<TranscriptRequest> getStudentRequests(int studentId) {
        return transcriptService.getStudentRequests(studentId);
    }
}
