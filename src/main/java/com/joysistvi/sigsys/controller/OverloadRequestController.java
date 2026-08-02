package com.joysistvi.sigsys.controller;
import com.joysistvi.sigsys.model.OverloadRequest;
import com.joysistvi.sigsys.service.OverloadRequestService;
import com.joysistvi.sigsys.service.OverloadRequestServiceImpl;
import java.util.List;
public class OverloadRequestController {
    private final OverloadRequestService requestService = new OverloadRequestServiceImpl();
    public boolean request(int studentId, int requestedUnits) { return requestService.request(studentId, requestedUnits); }
    public List<OverloadRequest> getPendingRequests() { return requestService.getPendingRequests(); }
    public List<OverloadRequest> getStudentRequests(int studentId) { return requestService.getStudentRequests(studentId); }
    public Integer getApprovedMaxUnits(int studentId) { return requestService.getApprovedMaxUnits(studentId); }
    public boolean review(int requestId, String status, int adminId) { return requestService.review(requestId, status, adminId); }
}
