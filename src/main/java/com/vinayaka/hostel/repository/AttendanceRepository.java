package com.vinayaka.hostel.repository;

import com.vinayaka.hostel.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    @Query("SELECT a FROM Attendance a WHERE a.date = :date AND a.student.gender = :gender")
    List<Attendance> findByDateAndStudentGender(@Param("date") LocalDate date, @Param("gender") String gender);

    @Query("SELECT a.date, " +
           "SUM(CASE WHEN a.status = 'PRESENT' THEN 1 ELSE 0 END), " +
           "SUM(CASE WHEN a.status = 'ABSENT' THEN 1 ELSE 0 END) " +
           "FROM Attendance a GROUP BY a.date ORDER BY a.date DESC")
    List<Object[]> findAttendanceSummary();
    @Query("SELECT a FROM Attendance a WHERE a.student.collegePin = :pin ORDER BY a.date DESC")
    List<Attendance> findByStudentCollegePin(@Param("pin") String pin);
    
    void deleteByStudentCollegePin(String pin);
}
