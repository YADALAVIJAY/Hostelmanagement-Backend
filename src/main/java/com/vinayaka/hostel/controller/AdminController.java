package com.vinayaka.hostel.controller;

import com.vinayaka.hostel.entity.Student;
import com.vinayaka.hostel.entity.WebPermission;
import com.vinayaka.hostel.service.PermissionService;
import com.vinayaka.hostel.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private PermissionService permissionService;

    @PostMapping("/student")
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        try {
            Student savedStudent = studentService.registerStudent(student);
            return ResponseEntity.ok(savedStudent);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/students")
    public ResponseEntity<List<Student>> getStudents(
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) Student.StudentStatus status) {
        
        if (gender != null && status != null) {
            return ResponseEntity.ok(studentService.getStudentsByGenderAndStatus(gender, status));
        } else if (gender != null) {
            return ResponseEntity.ok(studentService.getStudentsByGender(gender));
        }
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/permissions")
    public ResponseEntity<List<WebPermission>> getPendingPermissions() {
        return ResponseEntity.ok(permissionService.getPendingPermissions());
    }

    @GetMapping("/history")
    public ResponseEntity<List<WebPermission>> getPermissionHistory() {
        return ResponseEntity.ok(permissionService.getAllPermissionHistory());
    }

    @PostMapping("/approve/{permissionId}")
    public ResponseEntity<WebPermission> approvePermission(@PathVariable Long permissionId) {
        return ResponseEntity.ok(permissionService.approvePermission(permissionId));
    }

    @PostMapping("/reject/{permissionId}")
    public ResponseEntity<WebPermission> rejectPermission(@PathVariable Long permissionId) {
        return ResponseEntity.ok(permissionService.rejectPermission(permissionId));
    }

    @PostMapping("/scan")
    public ResponseEntity<?> scanStudent(@RequestParam String collegePin) {
        try {
            Student student = studentService.getStudentByCollegePin(collegePin)
                    .orElseThrow(() -> new RuntimeException("Student not found"));
            
            // Fetch permission FIRST
            WebPermission latestPerm = permissionService.getLatestApprovedPermission(student.getId());

            Student.StudentStatus currentStatus = student.getStatus();
            // Create default 'IN_HOSTEL' if null for logic comparison
            if (currentStatus == null) currentStatus = Student.StudentStatus.IN_HOSTEL;

            Student updatedStudent = student; // Default to current state

            // LOGIC:
            // 1. If currently OUTING/HOME -> ACTION: RETURN
            // 2. If currently IN_HOSTEL -> 
            //      a. If APPROVED permission exists -> ACTION: LEAVE
            //      b. If NO approved permission -> ACTION: NONE (Just show status/history)

            boolean isReturning = (currentStatus == Student.StudentStatus.OUTING || currentStatus == Student.StudentStatus.HOME);

            if (isReturning) {
                 // --- RETURNING TO HOSTEL ---
                updatedStudent = studentService.updateStudentStatus(student.getId(), Student.StudentStatus.IN_HOSTEL);
                
                // Close any open APPROVED permissions (Self-Healing Step)
                boolean returnPermissionUsed = false;
                if (latestPerm != null && latestPerm.getStatus() == WebPermission.PermissionStatus.APPROVED) {
                     permissionService.markPermissionAsUsed(latestPerm.getId());
                     latestPerm.setStatus(WebPermission.PermissionStatus.USED);
                     
                     if (latestPerm.getType() == WebPermission.PermissionType.RETURN) {
                         returnPermissionUsed = true;
                     }
                }
                
                if (!returnPermissionUsed) {
                    permissionService.createReturnEntry(updatedStudent);
                }
                
                // If no active permission was closed, fetch the LAST USED one to show context
                if (latestPerm == null) {
                    latestPerm = permissionService.getLatestUsedPermission(student.getId());
                }

            } else {
                // --- CURRENTLY IN HOSTEL ---
                if (latestPerm != null && latestPerm.getStatus() == WebPermission.PermissionStatus.APPROVED) {
                    // HAS PERMISSION -> EXECUTE LEAVE
                    Student.StudentStatus targetStatus = Student.StudentStatus.OUTING;
                    if (latestPerm.getType() == WebPermission.PermissionType.HOME) {
                        targetStatus = Student.StudentStatus.HOME;
                    }

                    // Mark as USED
                    permissionService.markPermissionAsUsed(latestPerm.getId());
                    latestPerm.setStatus(WebPermission.PermissionStatus.USED);

                    updatedStudent = studentService.updateStudentStatus(student.getId(), targetStatus);
                } else {
                    // NO PERMISSION -> DO NOTHING (Idempotent Scan)
                    // Just fetch last history to show "Previous Permission Letter"
                    latestPerm = permissionService.getLatestUsedPermission(student.getId());
                }
            }
            
            com.vinayaka.hostel.dto.ScanResponse response = new com.vinayaka.hostel.dto.ScanResponse(
                    "Scan Successful. Status updated to " + updatedStudent.getStatus(),
                    updatedStudent,
                    latestPerm
            );
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
