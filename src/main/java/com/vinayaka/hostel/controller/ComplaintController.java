package com.vinayaka.hostel.controller;

import com.vinayaka.hostel.entity.Complaint;
import com.vinayaka.hostel.service.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/complaints")
@CrossOrigin(origins = "*")
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    @PostMapping("/{pin}")
    public ResponseEntity<Complaint> raiseComplaint(@PathVariable String pin, @RequestBody Complaint complaint) {
        try {
            return ResponseEntity.ok(complaintService.raiseComplaint(pin, complaint));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/student/{pin}")
    public ResponseEntity<List<Complaint>> getMyComplaints(@PathVariable String pin) {
        return ResponseEntity.ok(complaintService.getStudentComplaints(pin));
    }

    @GetMapping
    public ResponseEntity<List<Complaint>> getAllComplaints() {
        return ResponseEntity.ok(complaintService.getAllComplaints());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Complaint> updateStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            return ResponseEntity.ok(complaintService.updateStatus(id, status));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
