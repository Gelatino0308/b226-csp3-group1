package com.joysistvi.sigsys.model;

import java.time.LocalDateTime;// latest class to map timestamp

public class TranscriptRequest {
    private int requestId;
    private int studentId; // FK to students table
    private LocalDateTime requestDate;
    private String status; // Enum: 'PENDING', 'PROCESSING', 'COMPLETED', 'REJECTED'
    private int processedById; // FK to users table (Registrar)
    private String studentFirstName;
    private String studentLastName;
    private String processedByUsername;

    public TranscriptRequest(int requestId, int studentId, LocalDateTime requestDate, String status, int processedById) {
        this.requestId = requestId;
        this.studentId = studentId;
        this.requestDate = requestDate;
        this.status = status;
        this.processedById = processedById;
    }

    public TranscriptRequest(int requestId, LocalDateTime requestDate, String status, String studentFirstName, String studentLastName, String processedByUsername) {
        this.requestId = requestId;
        this.requestDate = requestDate;
        this.status = status;
        this.studentFirstName = studentFirstName;
        this.studentLastName = studentLastName;
        this.processedByUsername = processedByUsername;
    }

    public TranscriptRequest(int studentId, LocalDateTime requestDate, String status, int processedById) {
        this.studentId = studentId;
        this.requestDate = requestDate;
        this.status = status;
        this.processedById = processedById;
    }

    public TranscriptRequest() {

    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getProcessedById() {
        return processedById;
    }

    public void setProcessedById(int processedById) {
        this.processedById = processedById;
    }

    public String getStudentFirstName() {
        return studentFirstName;
    }

    public void setStudentFirstName(String studentFirstName) {
        this.studentFirstName = studentFirstName;
    }

    public String getStudentLastName() {
        return studentLastName;
    }

    public void setStudentLastName(String studentLastName) {
        this.studentLastName = studentLastName;
    }

    public String getProcessedByUsername() {
        return processedByUsername;
    }

    public void setProcessedByUsername(String processedByUsername) {
        this.processedByUsername = processedByUsername;
    }

    public void setProcessedBy(Integer processedBy) {
        this.processedById = processedBy == null ? 0 : processedBy;
    }
}
