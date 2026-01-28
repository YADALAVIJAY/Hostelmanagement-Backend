package com.vinayaka.hostel.service;

import com.vinayaka.hostel.entity.Student;
import com.vinayaka.hostel.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private QRCodeService qrCodeService;

    public Student registerStudent(Student student) throws Exception {
        // Encode password
        student.setPassword(passwordEncoder.encode(student.getPassword()));

        // Set default status
        student.setStatus(Student.StudentStatus.IN_HOSTEL);

        // Generate QR Code (content = collegePin)
        String qrCode = qrCodeService.generateQRCodeImage(student.getCollegePin(), 250, 250);
        student.setQrCode(qrCode);

        return studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public List<Student> getStudentsByGender(String gender) {
        return studentRepository.findByGender(gender);
    }

    public List<Student> getStudentsByGenderAndStatus(String gender, Student.StudentStatus status) {
        return studentRepository.findByGenderAndStatus(gender, status);
    }

    public Optional<Student> getStudentByCollegePin(String collegePin) {
        return studentRepository.findByCollegePin(collegePin);
    }

    @org.springframework.transaction.annotation.Transactional
    public Student updateStudentStatus(Long studentId, Student.StudentStatus status) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        student.setStatus(status);
        return studentRepository.save(student);
    }
}
