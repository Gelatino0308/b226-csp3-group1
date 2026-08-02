package com.joysistvi.sigsys.service;

import com.joysistvi.sigsys.model.TranscriptRequest;
import java.util.List;

public interface TranscriptRequestService {
    boolean requestTranscript(int studentId);
    boolean updateStatus(int requestId, String status, int registrarUserId);
    List<TranscriptRequest> getStudentRequests(int studentId);
    List<TranscriptRequest> getAllRequests();
}
