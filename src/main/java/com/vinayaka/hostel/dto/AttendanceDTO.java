package com.vinayaka.hostel.dto;

import java.time.LocalDate;

public class AttendanceDTO {

    private Long id;
    private LocalDate date;
    private String status; // PRESENT, ABSENT, PENDING
    private String remarks;
    private StudentSummaryDTO student;

    // Default Constructor
    public AttendanceDTO() {}

    // Constructor with fields
    public AttendanceDTO(Long id, LocalDate date, String status, String remarks, StudentSummaryDTO student) {
        this.id = id;
        this.date = date;
        this.status = status;
        this.remarks = remarks;
        this.student = student;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public StudentSummaryDTO getStudent() { return student; }
    public void setStudent(StudentSummaryDTO student) { this.student = student; }
}
