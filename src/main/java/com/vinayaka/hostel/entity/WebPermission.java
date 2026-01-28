package com.vinayaka.hostel.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "web_permissions")
public class WebPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Enumerated(EnumType.STRING)
    private PermissionType type; // HOME, OUTING

    private String reason;

    @Enumerated(EnumType.STRING)
    private PermissionStatus status; // PENDING, APPROVED, REJECTED

    private LocalDateTime requestDate;
    private LocalDateTime approvalDate;

    // Optional: Date range for permission
    private String fromDate;
    private String toDate;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String proofImage; // Base64 permission letter

    public enum PermissionType {
        HOME,
        OUTING,
        RETURN
    }

    public enum PermissionStatus {
        PENDING,
        APPROVED,
        REJECTED,
        USED
    }

    public WebPermission() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public PermissionType getType() { return type; }
    public void setType(PermissionType type) { this.type = type; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public PermissionStatus getStatus() { return status; }
    public void setStatus(PermissionStatus status) { this.status = status; }

    public LocalDateTime getRequestDate() { return requestDate; }
    public void setRequestDate(LocalDateTime requestDate) { this.requestDate = requestDate; }

    public LocalDateTime getApprovalDate() { return approvalDate; }
    public void setApprovalDate(LocalDateTime approvalDate) { this.approvalDate = approvalDate; }

    public String getFromDate() { return fromDate; }
    public void setFromDate(String fromDate) { this.fromDate = fromDate; }

    public String getToDate() { return toDate; }
    public void setToDate(String toDate) { this.toDate = toDate; }

    public String getProofImage() { return proofImage; }
    public void setProofImage(String proofImage) { this.proofImage = proofImage; }
}
