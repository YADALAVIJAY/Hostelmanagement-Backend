package com.vinayaka.hostel.repository;

import com.vinayaka.hostel.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByCollegePin(String collegePin);
    Optional<Student> findByEmail(String email);

    List<Student> findByGender(String gender);
    List<Student> findByStatus(Student.StudentStatus status);
    List<Student> findByGenderAndStatus(String gender, Student.StudentStatus status);
    Optional<Student> findByHostelId(String hostelId);
}
