package com.vinayaka.hostel.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "scan_logs")
public class ScanLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    private Long adminId; // Who scanned (optional)

    @Enumerated(EnumType.STRING)
    private ScanType scanType; // EXIT, ENTRY

    private LocalDateTime scanTime;

    public enum ScanType {
        EXIT,
        ENTRY
    }

    public ScanLog() {}

    public ScanLog(Long id, Student student, Long adminId, ScanType scanType, LocalDateTime scanTime) {
        this.id = id;
        this.student = student;
        this.adminId = adminId;
        this.scanType = scanType;
        this.scanTime = scanTime;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Long getAdminId() { return adminId; }
    public void setAdminId(Long adminId) { this.adminId = adminId; }

    public ScanType getScanType() { return scanType; }
    public void setScanType(ScanType scanType) { this.scanType = scanType; }

    public LocalDateTime getScanTime() { return scanTime; }
    public void setScanTime(LocalDateTime scanTime) { this.scanTime = scanTime; }
}
