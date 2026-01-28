package com.vinayaka.hostel.dto;

import com.vinayaka.hostel.entity.Student;
import com.vinayaka.hostel.entity.WebPermission;

public class ScanResponse {
    private String message;
    private Student student;
    private WebPermission latestPermission;

    public ScanResponse() {}

    public ScanResponse(String message, Student student, WebPermission latestPermission) {
        this.message = message;
        this.student = student;
        this.latestPermission = latestPermission;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public WebPermission getLatestPermission() { return latestPermission; }
    public void setLatestPermission(WebPermission latestPermission) { this.latestPermission = latestPermission; }
}
