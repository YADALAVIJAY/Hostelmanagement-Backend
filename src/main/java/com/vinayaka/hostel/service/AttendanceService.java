package com.vinayaka.hostel.service;

import com.vinayaka.hostel.entity.Attendance;
import com.vinayaka.hostel.entity.Student;
import com.vinayaka.hostel.repository.AttendanceRepository;
import com.vinayaka.hostel.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private StudentRepository studentRepository;

    public List<Attendance> getAttendanceSheet(LocalDate date, String gender) {
        // 1. Fetch all students of the specific gender
        List<Student> students = studentRepository.findAll().stream()
                .filter(s -> s.getGender() != null && s.getGender().equalsIgnoreCase(gender))
                .sorted((s1, s2) -> s1.getName().compareToIgnoreCase(s2.getName())) // Optional: Sort by name
                .collect(Collectors.toList());
        
        // 2. Fetch existing attendance records
        List<Attendance> existingRecords = attendanceRepository.findByDateAndStudentGender(date, gender);
        
        // 3. Map records by Student ID for fast lookup
        java.util.Map<Long, Attendance> attendanceMap = existingRecords.stream()
                .collect(Collectors.toMap(a -> a.getStudent().getId(), a -> a));
        
        // 4. Build final list: Use existing record if present, else create PENDING
        List<Attendance> finalSheet = new ArrayList<>();
        for (Student s : students) {
            if (attendanceMap.containsKey(s.getId())) {
                finalSheet.add(attendanceMap.get(s.getId()));
            } else {
                Attendance newRecord = new Attendance();
                newRecord.setStudent(s);
                newRecord.setDate(date);
                newRecord.setStatus(Attendance.AttendanceStatus.PENDING);
                finalSheet.add(newRecord);
            }
        }
        
        return finalSheet;
    }

    public List<Attendance> saveAttendance(List<Attendance> attendanceList) {
        return attendanceRepository.saveAll(attendanceList);
    }

    public List<Object[]> getHistory() {
        return attendanceRepository.findAttendanceSummary();
    }

    public List<Attendance> getStudentHistory(String pin) {
        return attendanceRepository.findByStudentCollegePin(pin);
    }
}
