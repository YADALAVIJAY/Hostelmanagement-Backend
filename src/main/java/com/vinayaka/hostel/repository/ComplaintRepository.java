package com.vinayaka.hostel.repository;

import com.vinayaka.hostel.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    List<Complaint> findByStudentCollegePinOrderBySubmittedDateDesc(String collegePin);
    List<Complaint> findAllByOrderBySubmittedDateDesc();
    
    void deleteByStudentCollegePin(String collegePin);
}
