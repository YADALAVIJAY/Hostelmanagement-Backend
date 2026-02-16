package com.vinayaka.hostel.service;

import com.vinayaka.hostel.dto.AttendanceDTO;
import com.vinayaka.hostel.dto.StudentSummaryDTO;
import com.vinayaka.hostel.entity.Attendance;
import com.vinayaka.hostel.entity.Student;
import com.vinayaka.hostel.repository.AttendanceRepository;
import com.vinayaka.hostel.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private StudentRepository studentRepository;

    public List<AttendanceDTO> getAttendanceSheet(LocalDate date, String gender) {
        // 1. Fetch all students of the specific gender
        List<Student> students = studentRepository.findAll().stream()
                .filter(s -> s.getGender() != null && s.getGender().equalsIgnoreCase(gender))
                .sorted((s1, s2) -> s1.getName().compareToIgnoreCase(s2.getName())) // Optional: Sort by name
                .collect(Collectors.toList());
        
        // 2. Fetch existing attendance records
        List<Attendance> existingRecords = attendanceRepository.findByDateAndStudentGender(date, gender);
        
        // 3. Map records by Student ID for fast lookup
        Map<Long, Attendance> attendanceMap = existingRecords.stream()
                .collect(Collectors.toMap(a -> a.getStudent().getId(), a -> a));
        
        // 4. Build final list: Use existing record if present, else create PENDING
        List<AttendanceDTO> finalSheet = new ArrayList<>();
        for (Student s : students) {
            AttendanceDTO dto = new AttendanceDTO();
            dto.setDate(date);
            dto.setStudent(mapToStudentSummary(s));

            if (attendanceMap.containsKey(s.getId())) {
                Attendance existing = attendanceMap.get(s.getId());
                dto.setId(existing.getId());
                dto.setStatus(existing.getStatus().name());
                dto.setRemarks(existing.getRemarks());
            } else {
                dto.setStatus("PENDING");
            }
            finalSheet.add(dto);
        }
        
        return finalSheet;
    }

    private StudentSummaryDTO mapToStudentSummary(Student s) {
        return new StudentSummaryDTO(
            s.getId(),
            s.getName(),
            s.getCollegePin(),
            s.getStatus() != null ? s.getStatus().name() : "IN_HOSTEL",
            s.getHostelId(),
            s.getMobileNumber(),
            s.getParentMobileNumber(),
            s.getBlock(),
            s.getRoomNo()
        );
    }

    // Handles saving DTOs back to Entities
    public void saveAttendance(List<AttendanceDTO> attendanceListDTO) {
        List<Attendance> toSave = new ArrayList<>();

        for (AttendanceDTO dto : attendanceListDTO) {
            Attendance attendance;
            if (dto.getId() != null) {
                // Update existing
                attendance = attendanceRepository.findById(dto.getId()).orElse(new Attendance());
            } else {
                // Create new
                attendance = new Attendance();
                attendance.setDate(dto.getDate());
                // Fetch student reference
                Student student = studentRepository.findById(dto.getStudent().getId())
                        .orElseThrow(() -> new RuntimeException("Student not found: " + dto.getStudent().getId()));
                attendance.setStudent(student);
            }
            
            // Update fields
            if (dto.getStatus() != null) {
                attendance.setStatus(Attendance.AttendanceStatus.valueOf(dto.getStatus()));
            }
            attendance.setRemarks(dto.getRemarks());
            
            toSave.add(attendance);
        }

        attendanceRepository.saveAll(toSave);
    }

    public List<Object[]> getHistory() {
        return attendanceRepository.findAttendanceSummary();
    }

    public List<Attendance> getStudentHistory(String pin) {
        return attendanceRepository.findByStudentCollegePin(pin);
    }
}

