package com.joysistvi.sigsys.repository;

import com.joysistvi.sigsys.model.OverloadRequest;
import java.util.List;

public interface OverloadRequestRepository {
    boolean create(OverloadRequest request);
    List<OverloadRequest> getPending();
    List<OverloadRequest> getByStudent(int studentId);
    Integer getApprovedUnits(int studentId);
    boolean hasPending(int studentId);
    boolean updateStatus(int requestId, String status, int reviewedBy);
}
