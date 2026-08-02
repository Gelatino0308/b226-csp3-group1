package com.joysistvi.sigsys.service;
import com.joysistvi.sigsys.model.OverloadRequest;
import java.util.List;
public interface OverloadRequestService {
    boolean request(int studentId, int requestedUnits);
    List<OverloadRequest> getPendingRequests();
    List<OverloadRequest> getStudentRequests(int studentId);
    Integer getApprovedMaxUnits(int studentId);
    boolean review(int requestId, String status, int adminId);
}
