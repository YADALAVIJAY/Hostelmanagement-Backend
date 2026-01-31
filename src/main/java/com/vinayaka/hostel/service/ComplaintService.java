package com.vinayaka.hostel.service;

import com.vinayaka.hostel.entity.Complaint;
import com.vinayaka.hostel.entity.Student;
import com.vinayaka.hostel.repository.ComplaintRepository;
import com.vinayaka.hostel.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ComplaintService {

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private StudentRepository studentRepository;

    public Complaint raiseComplaint(String pin, Complaint complaint) {
        Optional<Student> studentOpt = studentRepository.findByCollegePin(pin);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            complaint.setStudent(student);
            complaint.setSubmittedDate(LocalDateTime.now());
            complaint.setStatus(Complaint.ComplaintStatus.OPEN);
            
            // Auto-fill defaults if not provided (though frontend should provide them)
            if (complaint.getHostelId() == null || complaint.getHostelId().isEmpty()) {
                complaint.setHostelId(student.getHostelId());
            }

            return complaintRepository.save(complaint);
        }
        throw new RuntimeException("Student not found");
    }

    public Complaint updateStatus(Long id, String status) {
        Optional<Complaint> complaintOpt = complaintRepository.findById(id);
        if (complaintOpt.isPresent()) {
            Complaint c = complaintOpt.get();
            try {
                c.setStatus(Complaint.ComplaintStatus.valueOf(status));
            } catch (IllegalArgumentException e) {
                // Ignore invalid status
            }
            return complaintRepository.save(c);
        }
        throw new RuntimeException("Complaint not found");
    }

    public List<Complaint> getStudentComplaints(String pin) {
        return complaintRepository.findByStudentCollegePinOrderBySubmittedDateDesc(pin);
    }

    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAllByOrderBySubmittedDateDesc();
    }
}
