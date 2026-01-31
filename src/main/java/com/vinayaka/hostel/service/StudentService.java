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

    @org.springframework.transaction.annotation.Transactional
    public Student registerStudent(Student student) throws Exception {
        // Encode password
        student.setPassword(passwordEncoder.encode(student.getPassword()));

        // Set default status
        student.setStatus(Student.StudentStatus.IN_HOSTEL);

        // Generate QR Code (content = collegePin)
        String qrCode = qrCodeService.generateQRCodeImage(student.getCollegePin(), 250, 250);
        student.setQrCode(qrCode);

        // Update Room Occupancy if room details are provided
        if (student.getHostelId() != null && student.getBlock() != null && student.getRoomNo() != null) {
            com.vinayaka.hostel.entity.Room room = roomRepository.findByHostelIdAndBlockAndRoomNo(
                student.getHostelId(), student.getBlock(), student.getRoomNo());
            
            if (room != null) {
                if (room.getCurrentOccupancy() >= room.getCapacity()) {
                    throw new RuntimeException("Room is already full.");
                }
                room.setCurrentOccupancy(room.getCurrentOccupancy() + 1);
                if (room.getCurrentOccupancy() == room.getCapacity()) {
                    room.setStatus(com.vinayaka.hostel.entity.Room.RoomStatus.FULL);
                }
                roomRepository.save(room);
            }
        }

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

    public Optional<Student> getStudentByHostelId(String hostelId) {
        return studentRepository.findByHostelId(hostelId);
    }

    @org.springframework.transaction.annotation.Transactional
    public Student updateStudentStatus(Long studentId, Student.StudentStatus status) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        student.setStatus(status);
        return studentRepository.save(student);
    }

    @org.springframework.transaction.annotation.Transactional
    public Student assignRoom(String collegePin, String hostelId, String block, String roomNo) {
        Student student = studentRepository.findByCollegePin(collegePin)
                .orElseThrow(() -> new RuntimeException("Student with PIN " + collegePin + " not found."));
        
        // Update Room Occupancy
        com.vinayaka.hostel.entity.Room room = roomRepository.findByHostelIdAndBlockAndRoomNo(hostelId, block, roomNo);
        if (room != null) {
            if (room.getCurrentOccupancy() >= room.getCapacity()) {
                throw new RuntimeException("Room is already full.");
            }
            room.setCurrentOccupancy(room.getCurrentOccupancy() + 1);
            if (room.getCurrentOccupancy() == room.getCapacity()) {
                room.setStatus(com.vinayaka.hostel.entity.Room.RoomStatus.FULL);
            }
            roomRepository.save(room);
        }

        student.setHostelId(hostelId);
        student.setBlock(block);
        student.setRoomNo(roomNo);
        student.setStatus(Student.StudentStatus.IN_HOSTEL); // Auto set status
        
        return studentRepository.save(student);
    }
    @org.springframework.transaction.annotation.Transactional
    public Student vacateRoom(String collegePin) {
        Student student = studentRepository.findByCollegePin(collegePin)
                .orElseThrow(() -> new RuntimeException("Student with PIN " + collegePin + " not found."));
        
        // Update Room Occupancy
        if (student.getHostelId() != null && student.getBlock() != null && student.getRoomNo() != null) {
            com.vinayaka.hostel.entity.Room room = roomRepository.findByHostelIdAndBlockAndRoomNo(
                student.getHostelId(), student.getBlock(), student.getRoomNo());
            
            if (room != null && room.getCurrentOccupancy() > 0) {
                room.setCurrentOccupancy(room.getCurrentOccupancy() - 1);
                if (room.getStatus() == com.vinayaka.hostel.entity.Room.RoomStatus.FULL) {
                    room.setStatus(com.vinayaka.hostel.entity.Room.RoomStatus.AVAILABLE);
                }
                roomRepository.save(room);
            }
        }

        // hostlId should persist even after vacating the room
        // student.setHostelId(null); 
        student.setBlock(null);
        student.setRoomNo(null);
        // Status remains active/in_hostel or can be changed if needed. 
        
        return studentRepository.save(student);
    }
    @Autowired
    private com.vinayaka.hostel.repository.WebPermissionRepository permissionRepository;
    
    @Autowired
    private com.vinayaka.hostel.repository.ComplaintRepository complaintRepository;
    
    @Autowired
    private com.vinayaka.hostel.repository.AttendanceRepository attendanceRepository;
    
    @Autowired
    private com.vinayaka.hostel.repository.ScanLogRepository scanLogRepository;
    
    @Autowired
    private com.vinayaka.hostel.repository.RoomRepository roomRepository;

    @org.springframework.transaction.annotation.Transactional
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Student not found with ID: " + id));
            
        // 1. Maintain Room Consistency: If student is in a room, decrement occupancy
        if (student.getHostelId() != null && student.getBlock() != null && student.getRoomNo() != null) {
            com.vinayaka.hostel.entity.Room room = roomRepository.findByHostelIdAndBlockAndRoomNo(
                student.getHostelId(), student.getBlock(), student.getRoomNo());
            
            if (room != null && room.getCurrentOccupancy() > 0) {
                room.setCurrentOccupancy(room.getCurrentOccupancy() - 1);
                // Update status if needed
                if (room.getStatus() == com.vinayaka.hostel.entity.Room.RoomStatus.FULL) {
                    room.setStatus(com.vinayaka.hostel.entity.Room.RoomStatus.AVAILABLE);
                }
                roomRepository.save(room);
            }
        }
            
        // 2. Delete Permissions
        permissionRepository.deleteByStudentId(id);
        
        // 3. Delete Complaints (linked by PIN)
        complaintRepository.deleteByStudentCollegePin(student.getCollegePin());
        
        // 4. Delete Attendance (linked by PIN)
        attendanceRepository.deleteByStudentCollegePin(student.getCollegePin());
        
        // 5. Delete ScanLogs
        scanLogRepository.deleteByStudentId(id);
        
        // 6. Delete Student
        studentRepository.deleteById(id);
    }
}
