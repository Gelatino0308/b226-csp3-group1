package com.joysistvi.sigsys.model;

import java.time.LocalDate;

public class Student {
    private int studentId;
    private int userId;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String phone;
    private String address;
    private double cumulativeGpa;

    // 1. Default No-Arg Constructor (Fixes "no suitable constructor found")
    public Student() {}

    // 2. Parameterized Constructor
    public Student(int studentId, int userId, String firstName, String lastName, LocalDate dob, String phone, String address, double cumulativeGpa) {
        this.studentId = studentId;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.phone = phone;
        this.address = address;
        this.cumulativeGpa = cumulativeGpa;
    }

    // Getters and Setters
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }

    // Fixes "cannot find symbol method getPhone() / setPhone()"
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    // Fixes typo alias methods to prevent errors in DAO/View
    public double getCumulativeGpa() { return cumulativeGpa; }
    public double getCulativeGpa() { return cumulativeGpa; } // Alias for backward compatibility
    public void setCumulativeGpa(double cumulativeGpa) { this.cumulativeGpa = cumulativeGpa; }
}