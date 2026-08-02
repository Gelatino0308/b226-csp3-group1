package com.joysistvi.sigsys.model;

import java.time.LocalDateTime;

public class OverloadRequest {
    private int requestId;
    private int studentId;
    private int requestedUnits;
    private String status;
    private LocalDateTime requestedAt;
    private LocalDateTime reviewedAt;
    private Integer reviewedBy;

    public int getRequestId() { return requestId; }
    public void setRequestId(int requestId) { this.requestId = requestId; }
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public int getRequestedUnits() { return requestedUnits; }
    public void setRequestedUnits(int requestedUnits) { this.requestedUnits = requestedUnits; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getRequestedAt() { return requestedAt; }
    public void setRequestedAt(LocalDateTime requestedAt) { this.requestedAt = requestedAt; }
    public LocalDateTime getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }
    public Integer getReviewedBy() { return reviewedBy; }
    public void setReviewedBy(Integer reviewedBy) { this.reviewedBy = reviewedBy; }
}
