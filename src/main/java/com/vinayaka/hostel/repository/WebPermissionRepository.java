package com.vinayaka.hostel.repository;

import com.vinayaka.hostel.entity.WebPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WebPermissionRepository extends JpaRepository<WebPermission, Long> {
    List<WebPermission> findByStudentId(Long studentId);
    List<WebPermission> findByStatus(WebPermission.PermissionStatus status);
    // Find latest approved permission for a student
    List<WebPermission> findTopByStudentIdAndStatusOrderByApprovalDateDesc(Long studentId, WebPermission.PermissionStatus status);
    
    // Find all permissions acting as history log
    List<WebPermission> findAllByOrderByRequestDateDesc();
}
