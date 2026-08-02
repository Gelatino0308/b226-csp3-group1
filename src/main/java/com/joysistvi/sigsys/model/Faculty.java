package com.joysistvi.sigsys.model;

public class Faculty {
    private int facultyId;
    private int userId; // FK to users table
    private String firstName;
    private String lastName;
    private String department;
    private String username;

    public Faculty(int facultyId, int userId, String firstName, String lastName, String department) {
        this.facultyId = facultyId;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.department = department;
    }

    public Faculty(int facultyId, String firstName, String lastName, String department, String username) {
        this.facultyId = facultyId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.department = department;
        this.username = username;
    }

    public Faculty(int userId, String firstName, String lastName, String department) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.department = department;
    }

    public Faculty() {
    }

    public int getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(int facultyId) {
        this.facultyId = facultyId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
