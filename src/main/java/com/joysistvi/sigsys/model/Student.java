package com.joysistvi.sigsys.model;

import java.time.LocalDate; // latest class to map date

public class Student {
    private int studentId;
    private int userId; // FK to users table
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String address;
    private double cumulativeGpa;
    private int username;

    public Student(int studentId, int userId, String firstName, String lastName, LocalDate dob, String address, double cumulativeGpa) {
        this.studentId = studentId;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.address = address;
        this.cumulativeGpa = cumulativeGpa;
    }

    public Student(int studentId, String firstName, String lastName, LocalDate dob, String address, double cumulativeGpa, int username) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.address = address;
        this.cumulativeGpa = cumulativeGpa;
        this.username = username;
    }

    public Student(int userId, String firstName, String lastName, LocalDate dob, String address, double cumulativeGpa) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.address = address;
        this.cumulativeGpa = cumulativeGpa;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
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

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getCumulativeGpa() {
        return cumulativeGpa;
    }

    public void setCumulativeGpa(double cumulativeGpa) {
        this.cumulativeGpa = cumulativeGpa;
    }

    public int getUsername() {
        return username;
    }

    public void setUsername(int username) {
        this.username = username;
    }
}
