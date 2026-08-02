package com.joysistvi.sigsys.service;
import com.joysistvi.sigsys.model.OverloadRequest;
import com.joysistvi.sigsys.repository.OverloadRequestRepository;
import com.joysistvi.sigsys.repository.OverloadRequestRepositoryImpl;
import java.util.List;
public class OverloadRequestServiceImpl implements OverloadRequestService {
    private final OverloadRequestRepository requestRepository = new OverloadRequestRepositoryImpl();
    @Override public boolean request(int studentId, int requestedUnits) {
        if (studentId <= 0 || requestedUnits <= 0 || requestRepository.hasPending(studentId)) return false;
        OverloadRequest request = new OverloadRequest(); request.setStudentId(studentId); request.setRequestedUnits(requestedUnits);
        return requestRepository.create(request);
    }
    @Override public List<OverloadRequest> getPendingRequests() { return requestRepository.getPending(); }
    @Override public List<OverloadRequest> getStudentRequests(int studentId) { return studentId > 0 ? requestRepository.getByStudent(studentId) : java.util.Collections.emptyList(); }
    @Override public Integer getApprovedMaxUnits(int studentId) { return studentId > 0 ? requestRepository.getApprovedUnits(studentId) : null; }
    @Override public boolean review(int requestId, String status, int adminId) {
        return requestId > 0 && adminId > 0 && status != null && status.trim().toUpperCase().matches("APPROVED|DISAPPROVED")
                && requestRepository.updateStatus(requestId, status.trim().toUpperCase(), adminId);
    }
}
