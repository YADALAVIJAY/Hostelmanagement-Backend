package com.vinayaka.hostel.service;

import com.vinayaka.hostel.entity.Student;
import com.vinayaka.hostel.entity.WebPermission;
import com.vinayaka.hostel.repository.StudentRepository;
import com.vinayaka.hostel.repository.WebPermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PermissionService {

    @Autowired
    private WebPermissionRepository permissionRepository;

    @Autowired
    private StudentRepository studentRepository;

    public WebPermission applyPermission(String collegePin, WebPermission request) {
        Student student = studentRepository.findByCollegePin(collegePin)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        request.setStudent(student);
        request.setRequestDate(LocalDateTime.now());

        // Auto-approve RETURN requests
        if (request.getType() == WebPermission.PermissionType.RETURN) {
            request.setStatus(WebPermission.PermissionStatus.APPROVED);
            request.setApprovalDate(LocalDateTime.now());
            request.setReason(request.getReason() + " (Auto-Approved Return)");
        } else {
            request.setStatus(WebPermission.PermissionStatus.PENDING);
        }
        
        return permissionRepository.save(request);
    }

    public List<WebPermission> getPendingPermissions() {
        return permissionRepository.findByStatus(WebPermission.PermissionStatus.PENDING);
    }

    public List<WebPermission> getStudentHistory(String collegePin) {
        Student student = studentRepository.findByCollegePin(collegePin)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return permissionRepository.findByStudentId(student.getId());
    }

    public WebPermission approvePermission(Long permissionId) {
        WebPermission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not found"));
        
        permission.setStatus(WebPermission.PermissionStatus.APPROVED);
        permission.setApprovalDate(LocalDateTime.now());
        
        return permissionRepository.save(permission);
    }

    public WebPermission rejectPermission(Long permissionId) {
        WebPermission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not found"));

        permission.setStatus(WebPermission.PermissionStatus.REJECTED);
        permission.setApprovalDate(LocalDateTime.now());

        return permissionRepository.save(permission);
    }

    @org.springframework.transaction.annotation.Transactional
    public void markPermissionAsUsed(Long permissionId) {
        WebPermission permission = permissionRepository.findById(permissionId).orElse(null);
        if (permission != null) {
            permission.setStatus(WebPermission.PermissionStatus.USED);
            permissionRepository.save(permission);
        }
    }

    public void createReturnEntry(Student student) {
        WebPermission permission = new WebPermission();
        permission.setStudent(student);
        permission.setType(WebPermission.PermissionType.RETURN);
        permission.setStatus(WebPermission.PermissionStatus.USED); // Automatically approved/used
        permission.setReason("Return from Outing/Home");
        permission.setRequestDate(LocalDateTime.now());
        permission.setApprovalDate(LocalDateTime.now());
        
        permissionRepository.save(permission);
    }

    public WebPermission getLatestApprovedPermission(Long studentId) {
        List<WebPermission> list = permissionRepository.findTopByStudentIdAndStatusOrderByApprovalDateDesc(
                studentId, WebPermission.PermissionStatus.APPROVED);
        
        if (list.isEmpty()) return null;

        WebPermission latest = list.get(0);
        
        // 24-Hour Expiration Check
        // If the permission was approved more than 24 hours ago, mark it as invalid/rejected
        if (latest.getApprovalDate() != null && 
            latest.getApprovalDate().isBefore(LocalDateTime.now().minusHours(24))) {
            
            latest.setStatus(WebPermission.PermissionStatus.REJECTED);
            latest.setReason(latest.getReason() + " (Expired >24h)");
            permissionRepository.save(latest);
            return null;
        }

        return latest;
    }

    public WebPermission getLatestUsedPermission(Long studentId) {
        List<WebPermission> list = permissionRepository.findTopByStudentIdAndStatusOrderByApprovalDateDesc(
                studentId, WebPermission.PermissionStatus.USED);
        if (list.isEmpty()) return null;
        return list.get(0);
    }

    public List<WebPermission> getAllPermissionHistory() {
        return permissionRepository.findAllByOrderByRequestDateDesc();
    }
}
