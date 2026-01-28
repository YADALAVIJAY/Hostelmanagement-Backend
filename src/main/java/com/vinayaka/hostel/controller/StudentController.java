package com.vinayaka.hostel.controller;

import com.vinayaka.hostel.entity.Student;
import com.vinayaka.hostel.entity.WebPermission;
import com.vinayaka.hostel.service.PermissionService;
import com.vinayaka.hostel.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private PermissionService permissionService;

    @GetMapping("/{collegePin}")
    public ResponseEntity<Student> getStudent(@PathVariable String collegePin) {
        return studentService.getStudentByCollegePin(collegePin)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/apply/{collegePin}")
    public ResponseEntity<WebPermission> applyPermission(
            @PathVariable String collegePin,
            @RequestBody WebPermission permission) {
        try {
            return ResponseEntity.ok(permissionService.applyPermission(collegePin, permission));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/history/{collegePin}")
    public ResponseEntity<List<WebPermission>> getHistory(@PathVariable String collegePin) {
        try {
            return ResponseEntity.ok(permissionService.getStudentHistory(collegePin));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
