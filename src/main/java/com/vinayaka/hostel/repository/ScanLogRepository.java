package com.vinayaka.hostel.repository;

import com.vinayaka.hostel.entity.ScanLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScanLogRepository extends JpaRepository<ScanLog, Long> {
    List<ScanLog> findByStudentId(Long studentId);
    List<ScanLog> findByAdminId(Long adminId);
}
