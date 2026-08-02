package com.joysistvi.sigsys.model;

public class CourseSection {
    private int sectionId;
    private int courseId; // FK to courses table
    private int facultyId; // FK to faculty table
    private int periodId; // FK to academic_periods table
    private String scheduleDays;
    private String scheduleTime;
    private String room;
    private int capacity;
    private int courseTitle;
    private int facultyFirstName;
    private int facultyLastName;
    private int termName;

    public CourseSection(int sectionId, int courseId, int facultyId, int periodId, String scheduleDays, String scheduleTime, String room, int capacity) {
        this.sectionId = sectionId;
        this.courseId = courseId;
        this.facultyId = facultyId;
        this.periodId = periodId;
        this.scheduleDays = scheduleDays;
        this.scheduleTime = scheduleTime;
        this.room = room;
        this.capacity = capacity;
    }

    public CourseSection(int sectionId, String scheduleDays, String scheduleTime, String room, int capacity, int courseTitle, int facultyFirstName, int facultyLastName, int termName) {
        this.sectionId = sectionId;
        this.scheduleDays = scheduleDays;
        this.scheduleTime = scheduleTime;
        this.room = room;
        this.capacity = capacity;
        this.courseTitle = courseTitle;
        this.facultyFirstName = facultyFirstName;
        this.facultyLastName = facultyLastName;
        this.termName = termName;
    }

    public CourseSection(int courseId, int facultyId, int periodId, String scheduleDays, String scheduleTime, String room, int capacity) {
        this.courseId = courseId;
        this.facultyId = facultyId;
        this.periodId = periodId;
        this.scheduleDays = scheduleDays;
        this.scheduleTime = scheduleTime;
        this.room = room;
        this.capacity = capacity;
    }

    public CourseSection() {
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(int facultyId) {
        this.facultyId = facultyId;
    }

    public int getPeriodId() {
        return periodId;
    }

    public void setPeriodId(int periodId) {
        this.periodId = periodId;
    }

    public String getScheduleDays() {
        return scheduleDays;
    }

    public void setScheduleDays(String scheduleDays) {
        this.scheduleDays = scheduleDays;
    }

    public String getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(String scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(int courseTitle) {
        this.courseTitle = courseTitle;
    }

    public int getFacultyFirstName() {
        return facultyFirstName;
    }

    public void setFacultyFirstName(int facultyFirstName) {
        this.facultyFirstName = facultyFirstName;
    }

    public int getFacultyLastName() {
        return facultyLastName;
    }

    public void setFacultyLastName(int facultyLastName) {
        this.facultyLastName = facultyLastName;
    }

    public int getTermName() {
        return termName;
    }

    public void setTermName(int termName) {
        this.termName = termName;
    }
}
