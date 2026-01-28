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
    public ResponseEntity<List<Attendance>> getAttendance(
            @RequestParam("date") String dateStr,
            @RequestParam("gender") String gender) {
        try {
            LocalDate date = LocalDate.parse(dateStr);
            return ResponseEntity.ok(attendanceService.getAttendanceSheet(date, gender));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    public ResponseEntity<List<Attendance>> saveAttendance(@RequestBody List<Attendance> attendanceList) {
        try {
            return ResponseEntity.ok(attendanceService.saveAttendance(attendanceList));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
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
