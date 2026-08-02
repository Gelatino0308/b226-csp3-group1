package com.joysistvi.sigsys.repository;

import com.joysistvi.sigsys.model.TranscriptRequest;
import java.util.List;

public interface TranscriptRequestRepository {
    boolean createRequest(int studentId);
    boolean updateStatus(int requestId, String status, int processedByUserId);
    List<TranscriptRequest> getRequestsByStudentId(int studentId);
}