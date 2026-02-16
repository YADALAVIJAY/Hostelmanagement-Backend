package com.vinayaka.hostel.controller;

import com.vinayaka.hostel.entity.Attendance;
import com.vinayaka.hostel.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "*") // Allow frontend access
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping
    public ResponseEntity<List<com.vinayaka.hostel.dto.AttendanceDTO>> getAttendance(
            @RequestParam("date") String dateStr,
            @RequestParam String gender) {
        try {
            LocalDate date = LocalDate.parse(dateStr);
            return ResponseEntity.ok(attendanceService.getAttendanceSheet(date, gender));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> saveAttendance(@RequestBody List<com.vinayaka.hostel.dto.AttendanceDTO> attendanceList) {
        try {
            attendanceService.saveAttendance(attendanceList);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/history")
    public ResponseEntity<List<Object[]>> getHistory() {
        return ResponseEntity.ok(attendanceService.getHistory());
    }

    @GetMapping("/student/{pin}")
    public List<Attendance> getStudentHistory(@PathVariable String pin) {
        return attendanceService.getStudentHistory(pin);
    }
}
